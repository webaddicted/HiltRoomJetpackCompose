package com.webaddicted.composemart.ui.screen.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FilePresent
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.icons.outlined.Person4
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.webaddicted.composemart.R
import com.webaddicted.composemart.data.apiutils.ApiResponse
import com.webaddicted.composemart.data.model.EcomModel
import com.webaddicted.composemart.ui.theme.ComposeMartTheme
import com.webaddicted.composemart.viewmodel.MainViewModel


class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EcomScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun EcomScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel? = null
) {
    val ecomState by viewModel?.ecomState!!.collectAsState()

    LaunchedEffect(Unit) {
        viewModel?.getEcomData()
    }

    when (val state = ecomState.status) {
        ApiResponse.Status.LOADING -> {
            Text(text = "Loading...")
        }

        ApiResponse.Status.SUCCESS -> {
            val context = LocalContext.current
            ecomState.data?.let { ecomData ->
                EcomContent(ecomData = ecomData) {index->
                    var intent = Intent(context, MainActivity::class.java)
                    if (index == 0) {
                        intent = Intent(context, MainActivity::class.java)
                    }else  if (index == 1) {
                        intent = Intent(context, CartActivity::class.java)
                    }else if (index == 2) {
                        intent = Intent(context, FavouriteActivity::class.java)
                    }else if (index == 3) {
                        intent = Intent(context, OrderActivity::class.java)
                    }else if (index == 4) {
                        intent = Intent(context, ProfileActivity::class.java)
                    }
                    startActivity(context, intent, null)
                }
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
fun EcomContent(ecomData: EcomModel? = null, onItemClick: (index:Int) -> Unit) {
    val selectedMenuIndex = remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    ConstraintLayout(modifier = Modifier.background(Color.White)) {
        val (scrollList, bottomMenu) = createRefs()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape.copy(CornerSize(20.dp)))
                .constrainAs(scrollList) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                }) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 48.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Welcome Back", color = Color.Black)
                        Text(
                            "Deepak",
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                    }
                    Row {
                        Image(
                            painter = painterResource(R.drawable.fav_icon),
                            contentDescription = "fav",
                            modifier = Modifier.clickable{
                                startActivity(context, Intent(context, FavouriteActivity::class.java), null)
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Image(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "search"
                        )
                    }
                }
            }

            item {
                if (ecomData == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) { }
                } else {
                    BannerWidget(ecomData.banner)
                }
            }
            item {
                SectionTitle("Categories")
            }
            item {
                CategoryList(ecomData?.category)
            }
            item {
                SectionTitle("Recommendation")
            }
            item {
                RecommendationList(ecomData?.items)
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }

        }
        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bottomMenu) {
                    bottom.linkTo(parent.bottom)
                },
            selectedIndex = selectedMenuIndex.value,
            onMenuItemClick = { index ->
                selectedMenuIndex.value = index
                onItemClick(index)
                // Handle other menu item clicks based on index
                Log.d("BottomMenu", "Selected menu item index: $index")
            }
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerWidget(banner: List<EcomModel.Banner?>?) {
    val pagerState: PagerState = remember { PagerState() }
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(count = banner?.size ?: 0, state = pagerState) { page ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(banner!![page]?.url)
                    .error(R.drawable.banner1).placeholder(R.drawable.banner2).build(),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
        DotIndicator(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.CenterHorizontally),
            totalDots = banner?.size ?: 0,
            selectedIndex = if (isDragged) pagerState.currentPage else pagerState.currentPage,
            dotSize = 8.dp
        )
    }
}

@Composable
fun DotIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int = 0,
    selectedIndex: Int = 0,
    selectedColor: Color = colorResource(R.color.appColor),
    unselectedColor: Color = colorResource(R.color.grey),
    dotSize: Dp = 5.dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) selectedColor else unselectedColor)
            ) { }
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}


@Composable
private fun SectionTitle(title: String, actionTxt: String = "See All") {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            fontSize = 17.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            actionTxt,
            fontSize = 17.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun CategoryList(category: List<EcomModel.Category?>?) {
    val selectedIndex = remember { mutableStateOf(-1) }
    val context = LocalContext.current
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp)
    ) {
        items(category?.size ?: 0) { index ->
            val item = category?.get(index)
            CategoryItem(
                item,
                isSelected = selectedIndex.value == index,
                onItemClick = {
                    selectedIndex.value = index
                    val intent = Intent(context, ListItemActivity::class.java)
                    intent.putExtra("id", item?.id.toString())
                    intent.putExtra("title", item?.title.toString())
                    startActivity(context, intent, null)
                })
        }
    }
}

@Composable
fun CategoryItem(category: EcomModel.Category?, isSelected: Boolean, onItemClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = {
                onItemClick()
            })
            .background(
                color = if (isSelected) colorResource(R.color.appColor) else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val context = LocalContext.current
        val imageResId = remember(category?.picUrl) {
            category?.picUrl?.let {
                context.resources.getIdentifier(it, "drawable", context.packageName)
            }
        }
        Log.d("TAGImage", "Image REes : $imageResId")
        if (imageResId != null && imageResId != 0) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = category?.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        color = if (isSelected) Color.Transparent else colorResource(R.color.lightGrey),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(15.dp),
                colorFilter = if (isSelected) ColorFilter.tint(Color.White) else ColorFilter.tint(
                    Color.Black
                )

            )
        }
        if (isSelected)
            Text(
                category?.title ?: "",
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 12.dp)
            )
    }
}

@Composable
fun RecommendationList(list: List<EcomModel.Item?>?, height: Dp = 500.dp) {
    val context = LocalContext.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(start = 8.dp, end = 2.dp)
            .height(height),
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
                        .height(240.dp)
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
                                tint = colorResource(R.color.grey),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(24.dp)
                                    .clickable {
                                        // Favorite functionality can be added here
                                    }
                            )
                        }
                        Text(
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
                                Text(
                                    text = listItem?.rating.toString(),
                                    color = Color.Gray,
                                    fontWeight = FontWeight.W500,
                                    fontSize = 15.sp
                                )
                            }
                            Text(
                                text = "₹${listItem?.price.toString()}",
                                color = colorResource(R.color.appColor),
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

@Composable
private fun BottomMenu(
    modifier: Modifier,
    selectedIndex: Int = 0,
    onMenuItemClick: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
            .background(
                colorResource(R.color.appColor),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomMenuItem(
            icon = Icons.Outlined.MyLocation,
            text = "Explore",
            isSelected = selectedIndex == 0,
            onClick = { onMenuItemClick(0) }
        )
        BottomMenuItem(
            icon = Icons.Outlined.ShoppingCart,
            text = "Cart",
            isSelected = selectedIndex == 1,
            onClick = { onMenuItemClick(1) }
        )
        BottomMenuItem(
            icon = Icons.Outlined.Favorite,
            text = "Favorite",
            isSelected = selectedIndex == 2,
            onClick = { onMenuItemClick(2) }
        )
        BottomMenuItem(
            icon = Icons.Outlined.FilePresent,
            text = "Orders",
            isSelected = selectedIndex == 3,
            onClick = { onMenuItemClick(3) }
        )
        BottomMenuItem(
            icon = Icons.Outlined.Person4,
            text = "Profile",
            isSelected = selectedIndex == 4,
            onClick = { onMenuItemClick(4) }
        )
    }
}

@Composable
fun BottomMenuItem(
    icon: ImageVector,
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    val selectedColor = if (isSelected) Color.White else Color.LightGray

    Column(
        modifier = Modifier
            .height(60.dp)
            .fillMaxHeight()
//            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = text, tint = selectedColor)
        Text(text, color = selectedColor, fontSize = 10.sp)
    }
}
