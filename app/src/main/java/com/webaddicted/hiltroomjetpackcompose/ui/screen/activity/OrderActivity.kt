package com.webaddicted.hiltroomjetpackcompose.ui.screen.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.webaddicted.hiltroomjetpackcompose.R
import com.webaddicted.hiltroomjetpackcompose.data.apiutils.ApiResponse
import com.webaddicted.hiltroomjetpackcompose.data.model.EcomModel
import com.webaddicted.hiltroomjetpackcompose.viewmodel.MainViewModel

class OrderActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListItemScreen()
        }
    }

    @Composable
    fun ListItemScreen() {
        val ecomState by viewModel?.ecomState!!.collectAsState()

        LaunchedEffect(Unit) {
            viewModel?.getEcomData()
        }

        when (val state = ecomState.status) {
            ApiResponse.Status.LOADING -> {
                Text(text = "Loading...")
            }

            ApiResponse.Status.SUCCESS -> {
                ecomState.data?.let { ecomData ->
                    ListItemData(list = ecomData.items)
                }
            }

            ApiResponse.Status.ERROR -> {
                Text(text = "Error: ${ecomState.errorMessage}")
            }

            ApiResponse.Status.NO_INTERNET_CONNECTION -> {
                Text(text = "No internet connection")
            }
        }
    }

    @Composable
    fun ListItemData(list: List<EcomModel.Item?>?) {
        Column(modifier = Modifier.fillMaxSize()) {
            ConstraintLayout(modifier = Modifier.padding(top = 36.dp, start = 16.dp, end = 16.dp)) {
                val (backBtn, cartTxt) = createRefs()
                Text(
                    text = "Orders",
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(cartTxt) { centerTo(parent) },
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.W600
                )
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier
                        .clickable { finish() }
                        .constrainAs(backBtn) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })
            }
            RecommendationList(list)
        }
    }
    @Composable
    fun RecommendationList(list: List<EcomModel.Item?>?) {
        val context = LocalContext.current
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(start = 8.dp, end = 2.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(list?.size ?: 0) { index ->
                val listItem = list?.get(index)
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            val intent = Intent(context, DetailActivity::class.java)
                            intent.putExtra("item", listItem)
                            ContextCompat.startActivity(context, intent, null)
                        },
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val context = LocalContext.current
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(230.dp)
                            .background(
                                color = colorResource(R.color.lightGrey),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(8.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Column(
                            modifier = Modifier
                        ) {
                            Box {
                                AsyncImage(
                                    model = listItem?.picUrl?.firstOrNull() ?: "",
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .height(175.dp)
                                        .background(
                                            color = colorResource(R.color.lightGrey),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clip(RoundedCornerShape(10.dp)),
                                )

                                // Favorite icon in top right corner
                                Icon(
                                    imageVector = Icons.Outlined.Favorite,
                                    contentDescription = "Favorite",
                                    tint = colorResource(R.color.purple),
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .size(24.dp)
                                        .clickable {
                                            // Favorite functionality can be added here
                                        }
                                )
                            }
                            androidx.compose.material3.Text(
                                listItem?.title ?: "",
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W600,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    Image(
                                        painter = painterResource(R.drawable.star),
                                        contentDescription = "Rating",
                                        modifier = Modifier.align(
                                            Alignment.CenterVertically
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    androidx.compose.material3.Text(
                                        text = listItem?.rating.toString(),
                                        color = Color.Gray,
                                        fontWeight = FontWeight.W500,
                                        fontSize = 15.sp
                                    )
                                }
                                androidx.compose.material3.Text(
                                    text = "₹${listItem?.price.toString()}",
                                    color = colorResource(R.color.purple),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

                            }
                        }
                    }
                }

            }

        }
    }
//    @Composable
//    fun RecommendationFullList(list: List<EcomModel.Item?>?) {
//        val context = LocalContext.current
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier
//                .padding(start = 8.dp, end = 2.dp)
//                .fillMaxSize(),
//            horizontalArrangement = Arrangement.spacedBy(2.dp)
//        ) {
//            items(list?.size ?: 0) { index ->
//                val listItem = list?.get(index)
//                Row(
//                    modifier = Modifier.fillMaxSize().clickable{
//                        val intent =  Intent(context, DetailActivity::class.java)
//                        intent.putExtra("item", listItem)
//                        ContextCompat.startActivity(context, intent, null)
//
//                    },
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    val context = LocalContext.current
//                    Column(modifier = Modifier
//                        .padding(8.dp)
//                        .height(235.dp)) {
//                        AsyncImage(
//                            model = listItem?.picUrl?.firstOrNull() ?: "",
//                            contentDescription = "",
//                            modifier = Modifier
//                                .height(175.dp)
//                                .background(
//                                    color = colorResource(R.color.lightGrey),
//                                    shape = RoundedCornerShape(10.dp)
//                                )
//                                .padding(8.dp)
//                                .clickable {
//
//                                },
//                            contentScale = ContentScale.FillBounds
//                        )
//                        androidx.compose.material3.Text(
//                            listItem?.title ?: "",
//                            color = Color.Black,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.W600,
//                            maxLines = 1,
//                            overflow = TextOverflow.Ellipsis,
//                            modifier = Modifier.padding(top = 3.dp)
//                        )
//                        Row(modifier = Modifier
//                            .padding(top = 2.dp)
//                            .fillMaxSize(),
//                            horizontalArrangement = Arrangement.SpaceBetween) {
//                            Row {
//                                Image(painter = painterResource(R.drawable.star), contentDescription = "Rating", modifier = Modifier.align(
//                                    Alignment.CenterVertically))
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    text = listItem?.rating.toString(),
//                                    color = Color.Gray,
//                                    fontSize = 15.sp
//                                )
//                            }
//                            Text(
//                                text = "₹${listItem?.price.toString()}",
//                                color = colorResource(R.color.purple),
//                                fontSize = 18.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//
//                        }
//                    }
//                }
//
//            }
//
//        }
//    }

}
