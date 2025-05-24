package com.webaddicted.composemart.viewmodel

import androidx.lifecycle.viewModelScope
import com.webaddicted.composemart.data.model.character.CharacterRespo
import com.webaddicted.composemart.data.apiutils.ApiResponse
import com.webaddicted.composemart.data.repo.MainRepository
import com.webaddicted.composemart.data.model.EcomModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: MainRepository) :
    BaseViewModel() {
    private val _characterState = MutableStateFlow<ApiResponse<CharacterRespo>>(ApiResponse.loading())
    val characterState: StateFlow<ApiResponse<CharacterRespo>> = _characterState

    private val _ecomState = MutableStateFlow<ApiResponse<EcomModel>>(ApiResponse.loading())
    val ecomState: StateFlow<ApiResponse<EcomModel>> = _ecomState

    fun getCharacters() {
        viewModelScope.launch {
            repo.getCharacterList().collect { response ->
                _characterState.value = response
            }
        }
    }

    fun getEcomData() {
        viewModelScope.launch {
            repo.getEcomData().collect { response ->
                _ecomState.value = response
            }
        }
    }

    fun removeCartItem(index: Int) {
        val currentData = _ecomState.value.data
        currentData?.let { ecomData ->
            val mutableCart = ecomData.cart?.toMutableList()
            mutableCart?.let {
                if (index >= 0 && index < it.size) {
                    // Use removeAt extension function from MutableList
                    it.removeAt(index)
                    val updatedEcomData = ecomData.copy(cart = it)
                    _ecomState.update { currentState ->
                        ApiResponse.success(updatedEcomData)
                    }
                }
            }
        }
    }

    fun updateCartItemCount(index: Int, increment: Boolean) {
        val currentData = _ecomState.value.data
        currentData?.let { ecomData ->
            val mutableCart = ecomData.cart?.toMutableList()
            mutableCart?.let {
                if (index >= 0 && index < it.size) {
                    val item = it[index]
                    item?.let { cartItem ->
                        val currentCount = cartItem.count ?: 0
                        val newCount = if (increment) currentCount + 1 else maxOf(1, currentCount - 1)

                        // Create a new cart item with updated count
                        val updatedItem = cartItem.copy(count = newCount)
                        it[index] = updatedItem

                        val updatedEcomData = ecomData.copy(cart = it)
                        _ecomState.update { currentState ->
                            ApiResponse.success(updatedEcomData)
                        }
                    }
                }
            }
        }
    }
}
