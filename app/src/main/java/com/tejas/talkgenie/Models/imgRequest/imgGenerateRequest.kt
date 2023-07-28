package com.tejas.talkgenie.Models.imgRequest

data class imgGenerateRequest(
    val n: Int,
    val prompt: String,
    val size: String
)