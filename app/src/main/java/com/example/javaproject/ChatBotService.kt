package com.example.javaproject

import android.content.Context
import okhttp3.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ChatBotService(private val context: Context) {
    private val apiUrl = "https://api.openai.com/v1/chat/completions"
    private val client = OkHttpClient()
    private val gson = Gson()

    fun sendMessage(message: String, callback: Callback) {
        val apiKey = context.getString(R.string.openai_api_key)
        val json = gson.toJson(MessageRequest(messages = listOf(message), model = "gpt-4"))
        val body = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(apiUrl)
            .header("Authorization", "Bearer $apiKey")
            .post(body)
            .build()

        client.newCall(request).enqueue(callback)
    }

    private data class MessageRequest(val messages: List<String>, val model: String)
}
