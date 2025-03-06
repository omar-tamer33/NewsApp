package com.example.newsapp.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.newsapp.NewsScreen
import com.example.newsapp.R
import com.example.newsapp.SearchScreen
import com.example.newsapp.api.model.Category
import com.example.newsapp.news.TopAppBar
import com.example.newsapp.ui.theme.black
import com.example.newsapp.ui.theme.blackWithOpacity
import com.example.newsapp.ui.theme.titleColor

@Composable
fun CategoriesContent(navController: NavController , modifier: Modifier = Modifier) {
    val list = Category.getCategoryList()
    Scaffold(containerColor = black , topBar = { TopAppBar("Home"){
        navController.navigate(SearchScreen)
    } }) { paddingValues ->
        LazyColumn(contentPadding = paddingValues){
            item {
                ShowCategoriesTitle()
            }
            items(Category.getCategoryList().size) { position ->
                CategoryCardContent(
                    category = list[position],
                    isRight = position % 2 == 0,
                    onCardClick = { categoryId , title ->
                        navController.navigate(NewsScreen(categoryId , title)) })
            }
        }
    }
}



@Composable
fun CategoryCardContent(modifier: Modifier = Modifier , category: Category , isRight : Boolean , onCardClick : (categoryId : String , title : String) -> Unit) {
    Card(shape = RoundedCornerShape(16.dp) , colors = CardDefaults.cardColors(Color.White) , modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(200.dp) , onClick = {onCardClick(category.apiId , category.title)}) {
        if (isRight){
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(category.imageId) , contentDescription = "Category Image" , modifier = Modifier.fillMaxHeight())
                Column(modifier = Modifier.fillMaxHeight().fillMaxWidth() , verticalArrangement = Arrangement.SpaceAround , horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = category.title , color = titleColor , fontSize = 32.sp , fontWeight = FontWeight.W500)
                    ViewAllArrow(isRight = isRight)
                }
            }
        }else{
            Row(modifier = Modifier.fillMaxWidth() , horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5F) , verticalArrangement = Arrangement.SpaceAround , horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = category.title , color = titleColor , fontSize = 28.sp , fontWeight = FontWeight.W500)
                    ViewAllArrow(isRight = isRight)
                }
                Image(painter = painterResource(category.imageId) , contentDescription = "Category Image" , modifier = Modifier.fillMaxHeight(), contentScale = ContentScale.Crop)
            }
        }

    }
}

@Composable
fun ViewAllArrow(modifier: Modifier = Modifier , isRight: Boolean) {
    if (isRight){
        Row (verticalAlignment = Alignment.CenterVertically , modifier = Modifier.background(blackWithOpacity , shape = CircleShape)){
            Text(text = "View All" , color = Color.White , fontSize = 16.sp , fontWeight = FontWeight.W500 , modifier = Modifier.padding(12.dp))
            Image(painter = painterResource(R.drawable.ic_right_arrow) , contentDescription = "Arrow icon")
        }
    }else{
        Row (verticalAlignment = Alignment.CenterVertically , modifier = Modifier.background(blackWithOpacity , shape = CircleShape)){
            Image(painter = painterResource(R.drawable.ic_left_arrow) , contentDescription = "Arrow icon")
            Text(text = "View All" , color = Color.White , fontSize = 16.sp , fontWeight = FontWeight.W500 , modifier = Modifier.padding(12.dp))
        }
    }
}


@Composable
fun ShowCategoriesTitle(modifier: Modifier = Modifier) {
    Text(text = "Good Morning\nHere is Some News For You" , color = Color.White , fontSize = 24.sp , fontWeight = FontWeight.W500)
    
}