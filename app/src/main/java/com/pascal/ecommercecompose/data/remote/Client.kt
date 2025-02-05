package com.pascal.ecommercecompose.data.remote

import android.util.Log
import com.pascal.ecommercecompose.utils.Constant.TIMEOUT
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val client = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        })
    }

    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Log.e("Ktor Logging", message)
            }
        }
    }

    defaultRequest {
        header("Content-Type", "application/json")
    }

    install(HttpTimeout) {
        connectTimeoutMillis = TIMEOUT
        requestTimeoutMillis = TIMEOUT
        socketTimeoutMillis = TIMEOUT
    }
}