package com.example.newsapp.news

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.R
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.ArticlesItem
import com.example.newsapp.api.model.NewsResponse
import com.example.newsapp.api.model.SourcesItem
import com.example.newsapp.ui.theme.black
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SearchScreenContent(modifier: Modifier = Modifier , viewModel: NewsViewModel) {
    val newsList = viewModel.newsList
    val selectedArticle = viewModel.selectedArticle
    val isSheetOpen = viewModel.isSheetOpen

    Scaffold(containerColor = black , topBar = { SearchTopAppBar(){ text ->
        viewModel.getNewsBySearch(text)
    } }) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(black) ,
            horizontalAlignment = Alignment.CenterHorizontally) {
            NewsLazyColumn(articlesList = newsList) { article ->
                selectedArticle.value = article
                isSheetOpen.value = true
            }
        }
    }
    selectedArticle.value?.let { article ->
        if (isSheetOpen.value) {
            BottomSheet(
                articlesItem = article,
                isSheetOpen = isSheetOpen,
                onDismiss = { isSheetOpen.value = false }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopAppBar(modifier: Modifier = Modifier , onSearchIconClick : (String) -> Unit) {
    val text = remember { mutableStateOf("") }
    TextField(value = text.value , modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .border(
            1.dp, Color.White, RoundedCornerShape(16.dp)
        ), onValueChange = { newText ->
        text.value = newText
    } , maxLines = 1 , trailingIcon = {
        IconButton(onClick = {onSearchIconClick(text.value)}) {
            Image(
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search Icon"
            )
        }
    } ,
        placeholder = {
            Text(text = "Search" , modifier = Modifier.padding(start = 8.dp) , color = Color.White , fontSize = 20.sp , fontWeight = FontWeight.W500)
    } ,
        colors = TextFieldDefaults.colors(focusedContainerColor = black , unfocusedContainerColor = black , focusedTextColor = Color.White))
}


@Preview
@Composable
private fun SearchTopAppBarPreview() {
    SearchTopAppBar(){}
}

