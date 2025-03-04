package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.newsapp.category.CategoriesContent
import com.example.newsapp.news.NewsScreenContent
import com.example.newsapp.news.TopAppBar
import com.example.newsapp.ui.theme.black


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsAppContent()
        }
    }
}


@Composable
fun NewsAppContent(modifier: Modifier = Modifier) {
    Scaffold(containerColor = black , topBar = { TopAppBar("News App")}) { paddingValues ->
            val navController = rememberNavController()
            NavHost(navController = navController , startDestination = CategoryScreen , modifier = Modifier.padding(paddingValues)){
                composable<CategoryScreen>{
                    CategoriesContent(navController)
                }
                composable<NewsScreen>{ navBackStackEntry ->
                    val newsScreen = navBackStackEntry.toRoute<NewsScreen>()
                    NewsScreenContent(categoryApiId = newsScreen.categoryApiId)

                }
            }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun NewsAppContentPreview() {
    NewsAppContent()
}

