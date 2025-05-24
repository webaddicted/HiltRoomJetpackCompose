package com.webaddicted.composemart.ui.screen.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.webaddicted.composemart.data.apiutils.ApiResponse
import com.webaddicted.composemart.data.model.EcomModel
import com.webaddicted.composemart.viewmodel.MainViewModel
import com.webaddicted.composemart.R
import com.webaddicted.composemart.ui.theme.appColor

class CartActivity : BaseActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            initData(viewModel = viewModel)
        }
    }

    @Composable
    fun initData(
        modifier: Modifier = Modifier,
        viewModel: MainViewModel? = null
    ) {
        val ecomState by viewModel?.ecomState!!.collectAsState()
        val cartState = remember { mutableStateListOf<EcomModel.Cart>() }

        LaunchedEffect(Unit) {
            viewModel?.getEcomData()
        }

        when (val state = ecomState.status) {
            ApiResponse.Status.LOADING -> {
                androidx.compose.material3.Text(text = "Loading...")
            }

            ApiResponse.Status.SUCCESS -> {
                ecomState.data?.let { ecomData ->
                    cartState.clear()
                    cartState.addAll(ecomData.cart?.filterNotNull() ?: emptyList())
                    CartScreen(
                        cart = cartState,
                        onBackClick = { finish() }
                    )
                }
            }

            ApiResponse.Status.ERROR -> {
                androidx.compose.material3.Text(text = "Error: ${ecomState.errorMessage}")
            }

            ApiResponse.Status.NO_INTERNET_CONNECTION -> {
                androidx.compose.material3.Text(text = "No internet connection")
            }
        }
    }


    @Composable
    private fun CartScreen(
        cart: SnapshotStateList<EcomModel.Cart>,
        onBackClick: () -> Unit,
    ) {
        Column(
            modifier = Modifier
             .fillMaxSize()
             .padding(16.dp)
        ) {
            // Top fixed section
            ConstraintLayout(modifier = Modifier.padding(top = 36.dp)) {
                val (backBtn, cartTxt) = createRefs()
                Text(
                    modifier = Modifier
                     .fillMaxWidth()
                     .constrainAs(cartTxt) { centerTo(parent) },
                    text = "Your Cart",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = "back",
                    modifier = Modifier
                     .clickable { onBackClick() }
                     .constrainAs(backBtn) {
                      top.linkTo(parent.top)
                      start.linkTo(parent.start)
                      bottom.linkTo(parent.bottom)
                     })
            }

            // Middle scrollable list with weight to take remaining space
            LazyColumn(
                modifier = Modifier
                 .padding(top = 10.dp)
                 .weight(1f)
            ) {
                itemsIndexed(cart) { index, _ ->
                    CartItem(
                        index = index,
                        cart = cart
                    )
                }
            }
            CartSummary(cart = cart)
        }
    }

    @Composable
    fun CartItem(
        index: Int,
        cart: SnapshotStateList<EcomModel.Cart>
    ) {
        val item = cart[index]
        val itemTotal = (item.price ?: 0) * (item.count ?: 0)
        val context = LocalContext.current
        ConstraintLayout(
            modifier = Modifier
             .fillMaxWidth()
             .padding(vertical = 4.dp)
             .background(
              color = colorResource(R.color.lightGrey),
              shape = RoundedCornerShape(15.dp)
             )
             .clickable {
              // Convert Cart to Item model
              val itemModel = EcomModel.Item(
               categoryId = item.categoryId,
               description = item.description,
               model = listOf(item.color), // Use color as a model option
               picUrl = item.picUrl,
               price = item.price,
               rating = item.rating,
               showRecommended = item.showRecommended,
               title = item.title
              )
              val intent = Intent(context, DetailActivity::class.java)
              intent.putExtra("item", itemModel)
              ContextCompat.startActivity(context, intent, null)
             }
             .padding(8.dp)
        ) {
            val (pic, title, price, totalEachTime, qty) = createRefs()
            Image(
                painter = rememberAsyncImagePainter(
                    model = item.picUrl?.first(),
                    error = painterResource(R.drawable.banner1),
                    placeholder = painterResource(R.drawable.banner2)
                ),
                contentDescription = "Product Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                 .size(80.dp)
                 .constrainAs(pic) {
                  start.linkTo(parent.start)
                  top.linkTo(parent.top)
                  bottom.linkTo(parent.bottom)
                 }
            )
            Text(
                text = item.title ?: "",
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(pic.end, 16.dp)
                    top.linkTo(parent.top, 8.dp)
                    width = Dimension.wrapContent
                }
            )
            Text(
                text = "₹${item.price}",
                fontSize = 16.sp,
                color = colorResource(R.color.appColor),
                modifier = Modifier.constrainAs(price) {
                    start.linkTo(pic.end, 16.dp)
                    top.linkTo(title.bottom, 8.dp)
                    width = Dimension.wrapContent
                }
            )
            Text(
                text = "₹$itemTotal",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(totalEachTime) {
                    start.linkTo(pic.end, 16.dp)
                    top.linkTo(price.bottom, 8.dp)
                    width = Dimension.wrapContent
                }
            )
            ConstraintLayout(
                modifier = Modifier
                 .width(100.dp)
                 .constrainAs(qty) {
                  end.linkTo(parent.end)
                  top.linkTo(price.bottom)
                 }
                 .background(colorResource(R.color.grey), shape = RoundedCornerShape(15.dp))
            ) {
                val (plusCartItem, minusCartItem, numberCartItem) = createRefs()

                // Minus Button
                Box(
                    modifier = Modifier
                     .padding(2.dp)
                     .size(28.dp)
                     .background(colorResource(R.color.white), shape = RoundedCornerShape(15.dp))
                     .constrainAs(minusCartItem) {
                      start.linkTo(parent.start, 2.dp)
                      top.linkTo(parent.top, 2.dp)
                      bottom.linkTo(parent.bottom, 2.dp)
                     }
                     .clickable {
                      if ((item.count ?: 1) > 1) {
                       val updated = item.copy(count = item.count!! - 1)
                       cart[index] = updated
                      } else {
                       cart.removeAt(index)
                      }
                     }
                ) {
                    Text(
                        text = if (item.count!! <= 1) "\uD83D\uDDD1\uFE0F" else "-",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Quantity Text
                Text(
                    text = item.count.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.constrainAs(numberCartItem) {
                        centerTo(parent)
                    }
                )

                // Plus Button
                Box(
                    modifier = Modifier
                     .padding(2.dp)
                     .size(28.dp)
                     .background(
                      colorResource(R.color.appColor),
                      shape = RoundedCornerShape(15.dp)
                     )
                     .constrainAs(plusCartItem) {
                      end.linkTo(parent.end, 2.dp)
                      top.linkTo(parent.top, 2.dp)
                      bottom.linkTo(parent.bottom, 2.dp)
                     }
                     .clickable {
                      val updated = item.copy(count = (item.count ?: 1) + 1)
                      cart[index] = updated
                     }
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }

    @Composable
    fun CartSummary(cart: List<EcomModel.Cart?>) {
        val subtotal = cart.sumOf { (it?.price ?: 0) * (it?.count ?: 0) }
        val delivery = 10
        val gst = (subtotal * 0.09).toInt()
        val total = subtotal + delivery + gst

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier
             .fillMaxWidth()
             .padding(top = 16.dp)) {
                Text(
                    "Item Total:",
                    Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.grey)
                )
                Text(
                    "₹$subtotal",
                    Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = colorResource(R.color.grey),
                    fontWeight = FontWeight.Bold
                )
            }
            Row(modifier = Modifier
             .fillMaxWidth()
             .padding(top = 16.dp)) {
                Text(
                    "GST (9%):",
                    Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.grey)
                )
                Text(
                    "₹$gst",
                    Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = colorResource(R.color.grey),
                    fontWeight = FontWeight.Bold
                )
            }
            Row(modifier = Modifier
             .fillMaxWidth()
             .padding(top = 16.dp)) {
                Text(
                    "Delivery:",
                    Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.grey)
                )
                Text(
                    "₹$delivery",
                    Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = colorResource(R.color.grey),
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                Modifier
                 .fillMaxWidth()
                 .padding(top = 16.dp)
                 .height(1.dp)
                 .background(colorResource(R.color.grey))
            )
            Row(modifier = Modifier
             .fillMaxWidth()
             .padding(top = 16.dp)) {
                Text(
                    "Total:",
                    Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.grey),
                    fontSize = 18.sp
                )
                Text(
                    "₹$total",
                    Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = colorResource(R.color.black),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Button(
                onClick = { /* Proceed to checkout */ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(contentColor = colorResource(R.color.appColor), backgroundColor = appColor),
                modifier = Modifier
                 .fillMaxWidth()
                 .padding(top = 16.dp)
                 .height(50.dp)
            ) {
                Text("Checkout", fontSize = 18.sp, color = colorResource(R.color.white))
            }
        }
    }
}
