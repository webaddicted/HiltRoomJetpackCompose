package com.webaddicted.composemart.ui.screen.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.webaddicted.composemart.R
import com.webaddicted.composemart.data.model.EcomModel
import com.webaddicted.composemart.ui.theme.appColor
import com.webaddicted.composemart.ui.theme.appColor20

class DetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.getSerializableExtra("item") as EcomModel.Item?
        setContent {
            DetailScreen(
                item = data!!,
                onBackClick = {
                    finish()
                },
                addToCart = {},
                onCartClick = {
                    ContextCompat.startActivity(
                        this,
                        Intent(this, CartActivity::class.java),
                        null
                    )

                }
            )
        }
    }

    @Composable
    fun DetailScreen(
        item: EcomModel.Item,
        onBackClick: () -> Unit,
        addToCart: () -> Unit,
        onCartClick: () -> Unit
    ) {
        var selectedImageUrl = remember { mutableStateOf(item.picUrl?.first()) }
        var selectedImageIndex = remember { mutableIntStateOf(0) }
        var selectedIndex = remember { mutableIntStateOf(0) }
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .padding(top = 24.dp, bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                val (back, fav) = createRefs()
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier
                        .clickable { onBackClick() }
                        .constrainAs(back) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)

                        })
                Image(
                    painter = painterResource(R.drawable.fav_icon),
                    contentDescription = "fav",
                    modifier = Modifier
                        .clickable {
                            ContextCompat.startActivity(
                                context,
                                Intent(context, FavouriteActivity::class.java),
                                null
                            )
                        }
                        .constrainAs(fav) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        })
            }
            Image(
                painter = rememberAsyncImagePainter(
                    model = selectedImageUrl.value,
                    error = painterResource(R.drawable.banner1),
                    placeholder = painterResource(R.drawable.banner2)
                ),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = colorResource(R.color.lightGrey),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            )
            LazyRow(modifier = Modifier.padding(vertical = 10.dp)) {
                items(item.picUrl?.size ?: 0) { index ->
                    val result = item.picUrl?.get(index)
                    ImageUrlComp(
                        imageUrl = result ?: "",
                        isSelected = selectedImageUrl.value == (result ?: ""),
                        onClick = {
                            selectedImageUrl.value = result ?: ""
                        }
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
            ) {
                Text(
                    text = item.title?.trim() ?: "",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W600,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                        .padding(end = 16.dp)
                )
                Text(
                    text = "${item.price}",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(end = 16.dp)
                )
            }
            MyRatingBar(rating = item.rating)
            ModelSelector(
                model = item.model!!,
                selectedModelIndex = selectedIndex.intValue,
                omModelSelected = { selectedIndex.intValue = it })
            Text(
                text = item.description ?: "",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = addToCart,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colorResource(R.color.appColor),
                        backgroundColor = appColor
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(50.dp)
                ) {
                    Text(text = "Buy Now", fontSize = 18.sp, color = colorResource(R.color.white))
                }
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.background(
                        colorResource(R.color.lightGrey),
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    Icon(
                        painterResource(R.drawable.btn_2),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }

        }

    }

    @Composable
    fun MyRatingBar(rating: Double?) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Select Model",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "$rating Rating", style = MaterialTheme.typography.body1, fontSize = 16.sp)
        }
    }

    @Composable
    fun ModelSelector(
        model: List<String?>,
        selectedModelIndex: Int,
        omModelSelected: (Int) -> Unit
    ) {
        LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
            itemsIndexed(model) { index, model ->
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(48.dp)
                        .then(
                            if (index == selectedModelIndex) {
                                Modifier.border(
                                    1.dp,
                                    appColor20,
                                    shape = RoundedCornerShape(10.dp)
                                )
                            } else {
                                Modifier
                            }
                        )
                        .background(
                            if (index == selectedModelIndex) colorResource(R.color.appColor) else colorResource(
                                R.color.lightGrey
                            ), shape = RoundedCornerShape(10.dp)
                        )
                        .clickable {
                            omModelSelected(index)
                        }
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = model ?: "",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = if (index == selectedModelIndex) colorResource(R.color.white) else colorResource(
                            R.color.black
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    @Composable
    fun ImageUrlComp(imageUrl: String, isSelected: Boolean, onClick: () -> Unit) {
        val backColor =
            if (isSelected) colorResource(R.color.appColor_light) else colorResource(R.color.lightGrey)
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(55.dp)
                .then(
                    if (isSelected) Modifier.border(
                        1.dp, color = colorResource(R.color.appColor),
                        RoundedCornerShape(10.dp)
                    ) else Modifier
                )
                .background(backColor, shape = RoundedCornerShape(10.dp))
                .clickable(onClick = onClick)
                .padding(4.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl,
                    error = painterResource(R.drawable.banner1),
                    placeholder = painterResource(R.drawable.banner2)
                ),
                contentDescription = "Product Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(10.dp))

            )
// Image(
// painter = rememberAsyncImagePainter(imageUrl,
// error = painterResource(R.drawable.banner1),
// placeholder = painterResource(R.drawable.banner2),
// contentScale = ContentScale.FillBounds),
// contentDescription = "",
// modifier = Modifier
// .size(80.dp)
// .padding(4.dp)
// )
        }
    }
}
