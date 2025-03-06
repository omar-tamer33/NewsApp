package com.example.newsapp.api.model

import com.example.newsapp.R

data class Category(val title : String , val imageId : Int , val apiId : String){
    companion object{
        val General = "general"
        val Business = "business"
        val Entertainment = "entertainment"
        val Health = "health"
        val Science = "science"
        val Technology = "technology"
        val Sports = "sports"

        fun getCategoryList() : List<Category>{
           return listOf(
                Category(title = "General" , R.drawable.img_general , General),
                Category(title = "Business" , R.drawable.img_business , Business),
               Category(title = "Sports" , R.drawable.img_sports , Sports),
               Category(title = "Technology" , R.drawable.img_technology , Technology),
               Category(title = "Entertainment" , R.drawable.img_entertainment , Entertainment),
                Category(title = "Health" , R.drawable.img_health , Health),
                Category(title = "Science" , R.drawable.img_science , Science)
                )
        }


    }
}
