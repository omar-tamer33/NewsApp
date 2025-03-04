package com.example.newsapp.news

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil3.compose.AsyncImage
import com.example.newsapp.R
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.ArticlesItem
import com.example.newsapp.api.model.NewsResponse
import com.example.newsapp.api.model.SourcesItem
import com.example.newsapp.api.model.SourcesResponse
import com.example.newsapp.ui.theme.black
import com.example.newsapp.ui.theme.gray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun NewsScreenContent(modifier: Modifier = Modifier , categoryApiId : String) {
    val sourcesList = remember { mutableStateListOf<SourcesItem>() }
    val newsList = remember { mutableStateListOf<ArticlesItem>() }
    val selectedSourceId = remember { mutableStateOf("") }
    val isSheetOpen = remember { mutableStateOf(false) }
    val selectedArticle = remember { mutableStateOf<ArticlesItem?>(null) }
    LaunchedEffect(selectedSourceId.value) {
        getNewsBySource(selectedSourceId.value , onSuccess = {
            newsList.addAll(it)
        } , onFailure = {})
    }
    LaunchedEffect(Unit) {
        getSourcesList(categoryApiId , onSuccess = {
            sourcesList.addAll(it)
        }, onFailure = {
            Log.e("TAG", "onFailure: $it ")
        })
    }
    Column(modifier = Modifier) {
        if (sourcesList.isNotEmpty()) {
            SourceLazyRow(sourcesList, onSelectedTab = { sourceId ->
                newsList.clear()
                selectedSourceId.value = sourceId
            })
        }
        NewsLazyColumn(
            articlesList = newsList,
            onArticleClick = { article ->
                selectedArticle.value = article
                isSheetOpen.value = true
            }
        )
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
fun BottomSheet( articlesItem: ArticlesItem , modifier: Modifier = Modifier ,  isSheetOpen: MutableState<Boolean> , onDismiss : () -> Unit) {
    val bottomSheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    ModalBottomSheet(onDismissRequest = {onDismiss()} , sheetState = bottomSheetState) {
        Column(horizontalAlignment = Alignment.CenterHorizontally , modifier = Modifier.padding(8.dp)) {
            AsyncImage(model = articlesItem.urlToImage, contentDescription = "Article Image")
            Text(
                text = articlesItem.description ?: "",
                color = black,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp),
                overflow = TextOverflow.Ellipsis
            )
            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articlesItem.url))
                context.startActivity(intent)
            } , colors = ButtonDefaults.buttonColors(Color.Black) , shape = RoundedCornerShape(8.dp)) {
                    Text(
                        text = "View Full Article",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W700,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(black) , title = {
        Text(text = title , fontWeight = FontWeight.W500 , color = Color.White , fontSize = 20.sp)
    }, actions = {
        Image(painter = painterResource(R.drawable.ic_search) , contentDescription = "Search Icon")
    }, navigationIcon = {
        Image(painter = painterResource(R.drawable.ic_nav_menu) , contentDescription = "Navigation Icon")
    } , windowInsets = WindowInsets(left = 8.dp , right = 8.dp))
}



@Composable
fun SourceLazyRow(sourcesList: List<SourcesItem>, modifier: Modifier = Modifier, onSelectedTab : (sourceId : String) -> Unit) {
    val selectedStateIndex = remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        onSelectedTab(sourcesList[0].id ?: "")
    }
    val selectedModifier = Modifier.drawBehind {
        val width = size.width
        val height = size.height
        drawLine(
            color = Color.White,
            start = Offset(x = 0F , y = height),
            end = Offset(x = width , y = height),
            strokeWidth = 2.dp.toPx()
        )
    }
    LazyRow (modifier = Modifier.padding(8.dp)){
        itemsIndexed(sourcesList){ index , source ->
            Tab(selected = index == selectedStateIndex.intValue , onClick = {
                selectedStateIndex.intValue = index
                onSelectedTab(source.id ?: "") } , modifier = Modifier.padding(6.dp)) {
                Text(text = source.name.toString() , color = Color.White , modifier = if (selectedStateIndex.intValue == index) selectedModifier else modifier , fontSize = 18.sp , fontWeight = if (selectedStateIndex.intValue == index) {
                    FontWeight.Bold} else FontWeight.Normal)
            }
        }
    }
}

@Preview
@Composable
private fun SourcesLazyRowPreview() {
    SourceLazyRow(
        listOf(
            SourcesItem(name = "ABC News"),
            SourcesItem(name = "Aftenposten"),
            SourcesItem(name = "Al Jazeera English")
        ),
        onSelectedTab = {}
    )
}

@Composable
fun NewsCard(articlesItem: ArticlesItem, modifier: Modifier = Modifier , onArticleClick : (ArticlesItem) -> Unit) {
    Card(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth(0.95F)
        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(16.dp))
        .padding(8.dp) , onClick = { onArticleClick(articlesItem) }) {
        Column(modifier = Modifier.background(black)) {
            AsyncImage(model = articlesItem.urlToImage , contentDescription = "article image" , modifier = Modifier
                .fillMaxWidth())
            Text(text = articlesItem.title ?: "" , color = Color.White , fontSize = 16.sp , fontWeight = FontWeight.W700 , maxLines = 2 , overflow = TextOverflow.Ellipsis)
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "By : ${articlesItem.author}" , color = gray , fontWeight = FontWeight.W500 , fontSize = 12.sp , modifier = Modifier.fillMaxWidth(0.5F), maxLines = 1 , overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.weight(1F))
                Text(text = articlesItem.publishedAt ?: "" , color = gray , fontWeight = FontWeight.W500 , fontSize = 12.sp)
            }
        }
    }
}



@Composable
fun NewsLazyColumn(articlesList : List<ArticlesItem>, modifier: Modifier = Modifier , onArticleClick: (ArticlesItem) -> Unit) {
    LazyColumn {
        items(articlesList){ article ->
            NewsCard(article , onArticleClick = onArticleClick)
        }
    }
}

fun getSourcesList(categoryApiId: String , onSuccess : (List<SourcesItem>) -> Unit, onFailure : (message : String) -> Unit){
    ApiManager.newsService.getSources("e5fabf68a10342c4827408cc84427a89" , categoryApiId ).enqueue(object :
        Callback<SourcesResponse> {
        override fun onResponse(call: Call<SourcesResponse>, response: Response<SourcesResponse>) {
            val sourcesList = response.body()?.sources?.filterNotNull()
            if (!sourcesList.isNullOrEmpty()){
                onSuccess(sourcesList)
            }
        }

        override fun onFailure(call: Call<SourcesResponse>, throwable: Throwable) {
            onFailure(throwable.message.toString())
        }

    })
}

fun getNewsBySource(sourceId : String , onSuccess: (List<ArticlesItem>) -> Unit , onFailure: (message: String) -> Unit){
    ApiManager.newsService.getNewsBySource(sourceId = sourceId , apiKey = "e5fabf68a10342c4827408cc84427a89").enqueue(object :
        Callback<NewsResponse> {
        override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
            val articlesList = response.body()?.articles?.filterNotNull()
            if (!articlesList.isNullOrEmpty()){
                onSuccess(articlesList)
            }
        }

        override fun onFailure(call: Call<NewsResponse>, throwable: Throwable){
            onFailure(throwable.message.toString())
        }

    })
}