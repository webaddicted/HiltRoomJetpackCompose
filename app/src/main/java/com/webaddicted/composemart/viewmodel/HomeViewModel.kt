package com.webaddicted.composemart.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.webaddicted.composemart.data.model.character.CharacterRespo
import com.webaddicted.composemart.data.repo.HomeRepository
import com.webaddicted.composemart.data.apiutils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: HomeRepository) :
    BaseViewModel() {
    var getCharacterRespo = MutableLiveData<ApiResponse<CharacterRespo>>()
    private val _characterState = MutableStateFlow<ApiResponse<CharacterRespo>>(ApiResponse.loading())
    val characterState: StateFlow<ApiResponse<CharacterRespo>> = _characterState

    fun getCharacters() {
        viewModelScope.launch {
            repo.getCharacterList().collect { response ->
                _characterState.value = response
            }
        }
    }
//    fun getCharacters() {
//        getCharacterRespo = MutableLiveData<ApiResponse<CharacterRespo>>()
//        repo.getCharacterList(getCharacterRespo)
//    }

}