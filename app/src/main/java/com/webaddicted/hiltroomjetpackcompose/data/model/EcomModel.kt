package com.webaddicted.hiltroomjetpackcompose.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EcomModel(
 @SerializedName("banner")
 val banner: List<Banner?>? = null,
 @SerializedName("cart")
 val cart: List<Cart?>? = null,
 @SerializedName("category")
 val category: List<Category?>? = null,
 @SerializedName("items")
 val items: List<Item?>? = null
) {
 data class Banner(
 @SerializedName("url")
 val url: String? = null
 )

 data class Cart(
 @SerializedName("categoryId")
 val categoryId: String? = null,
 @SerializedName("color")
 val color: String? = null,
 @SerializedName("count")
 val count: Int? = null,
 @SerializedName("description")
 val description: String? = null,
 @SerializedName("picUrl")
 val picUrl: List<String?>? = null,
 @SerializedName("price")
 val price: Int? = null,
 @SerializedName("rating")
 val rating: Double? = null,
 @SerializedName("showRecommended")
 val showRecommended: Boolean? = null,
 @SerializedName("title")
 val title: String? = null
 ): Serializable

 data class Category(
 @SerializedName("id")
 val id: Int? = null,
 @SerializedName("picUrl")
 val picUrl: String? = null,
 @SerializedName("title")
 val title: String? = null
 )

 data class Item(
 @SerializedName("categoryId")
 val categoryId: String? = null,
 @SerializedName("description")
 val description: String? = null,
 @SerializedName("model")
 val model: List<String?>? = null,
 @SerializedName("picUrl")
 val picUrl: List<String?>? = null,
 @SerializedName("price")
 val price: Int? = null,
 @SerializedName("rating")
 val rating: Double? = null,
 @SerializedName("showRecommended")
 val showRecommended: Boolean? = null,
 @SerializedName("title")
 val title: String? = null
 ): Serializable
}