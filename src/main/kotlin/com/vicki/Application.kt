package com.vicki

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.FirestoreOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.vicki.database.DBConnector.configureDatabase
import com.vicki.plugins.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val dbKtorm = configureDatabase()
    configureSerialization()
    configureRouting(dbKtorm)

    val serviceAccount = this::class.java.classLoader.getResourceAsStream("service_account_key.json");

    val options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build()

    FirebaseApp.initializeApp(options)
}
