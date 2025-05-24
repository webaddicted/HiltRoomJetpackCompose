package com.webaddicted.composemart.ui.screen.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.webaddicted.composemart.BuildConfig
import com.webaddicted.composemart.data.apiutils.ApiResponse
import com.webaddicted.composemart.data.model.character.CharacterResult
import com.webaddicted.composemart.ui.theme.ComposeMartTheme
import com.webaddicted.composemart.utils.common.GlobalUtils.showToast
import com.webaddicted.composemart.viewmodel.HomeViewModel

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMartTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CharacterScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun CharacterScreen(viewModel: HomeViewModel) {
    val context = LocalContext.current
    context.showToast(BuildConfig.API_KEY)
    val characterState = viewModel.characterState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCharacters()
    }
    val value = characterState.value
    when (val state = characterState.value.status) {
        ApiResponse.Status.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        ApiResponse.Status.SUCCESS -> {
            value.data?.let { response ->
                CharacterList(characters = response.results)
            }
        }
        ApiResponse.Status.ERROR -> {
            context.showToast(value.errorMessage)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${value.errorMessage}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        ApiResponse.Status.NO_INTERNET_CONNECTION -> {
            context.showToast("No internet connection")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Internet Connection",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CharacterList(characters: List<CharacterResult>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(characters.size) { index ->
            CharacterItem(character = characters[index])
        }
    }
}

@Composable
fun CharacterItem(character: CharacterResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Character Image
            Image(
                painter = rememberAsyncImagePainter(character.image),
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Character Details
            Column {
                Text(
                    text = character.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Species: ${character.species}",
                    fontSize = 14.sp
                )
                Text(
                    text = "Status: ${character.status}",
                    fontSize = 14.sp
                )
            }
        }
    }
}