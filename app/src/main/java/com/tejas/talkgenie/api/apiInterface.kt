package com.tejas.talkgenie.api

import com.tejas.talkgenie.Models.Chat.ChatResponse
import com.tejas.talkgenie.Models.imgRequest.imgGenerateRequest
import com.tejas.talkgenie.Models.imgResponse.imgGenerateResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface apiInterface {


    @POST ("/v1/completions")
    suspend fun getChat(

        @Header("Content-Type") contentType:String,
        @Header("Authorization") authorization:String,
        @Body requestBody: RequestBody
    ): ChatResponse



    @POST("/v1/images/generations")
    suspend fun generateImage(
        @Header("Content-Type") contentType:String,
        @Header("Authorization") authorization:String,
        @Body requestBody: RequestBody
    ):imgGenerateResponse

}