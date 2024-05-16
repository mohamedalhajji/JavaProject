package com.example.javaproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.javaproject.ui.theme.JavaProjectTheme
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : ComponentActivity() {
    private lateinit var chatBotService: ChatBotService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatBotService = ChatBotService(this)

        setContent {
            JavaProjectTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ChatScreen(chatBotService)
                }
            }
        }
    }
}

@Composable
fun ChatScreen(chatBotService: ChatBotService) {
    var inputText by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your message") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            chatBotService.sendMessage(inputText, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    responseText = "Error: ${e.message}"
                }

                override fun onResponse(call: Call, response: Response) {
                    responseText = if (response.isSuccessful) {
                        response.body?.string() ?: "Error: Empty response"
                    } else {
                        "Error: ${response.message}"
                    }
                }
            })
        }) {
            Text("Send")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(responseText)
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    JavaProjectTheme {
        ChatScreen(ChatBotService(LocalContext.current))
    }
}
