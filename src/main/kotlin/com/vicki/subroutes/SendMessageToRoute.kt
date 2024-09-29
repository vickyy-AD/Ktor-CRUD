package com.vicki.subroutes

import com.google.firebase.messaging.FirebaseMessaging
import com.vicki.SendMessageDto
import com.vicki.toMessage
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import org.ktorm.database.Database


fun Route.sendMessageToRoute() {
    post("sendTo") {

        try {
            val body = call.receiveNullable<SendMessageDto>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            FirebaseMessaging.getInstance().send(body.toMessage())
            call.respond(HttpStatusCode.OK)


        } catch (e: Exception) {


        }
    }
}
