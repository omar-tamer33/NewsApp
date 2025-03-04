package com.example.newsapp

import kotlinx.serialization.Serializable

@Serializable
object CategoryScreen
@Serializable
class NewsScreen(val categoryApiId : String)