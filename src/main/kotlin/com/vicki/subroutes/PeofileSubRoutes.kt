package com.vicki.subroutes

import com.vicki.tables.userProfile
import com.vicki.userModel
import com.vicki.utility.ResponseData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.*

fun Route.getAllUserRoute(db: Database) {

    get("all") {
        val user = db.from(userProfile).select()
            .map {
                JsonObject(
                    mapOf(
                        "userId" to JsonPrimitive(it[userProfile.userId]),
                        "userName" to JsonPrimitive(it[userProfile.userName]),
                        "phoneNumber" to JsonPrimitive(it[userProfile.phoneNumber]),
                        "password" to JsonPrimitive(it[userProfile.password]),
                    )
                )
            }

        call.respond(
            HttpStatusCode.OK,
            ResponseData(true, message = "Hi Mame", data = user)
        )
    }
}

fun Route.getSingleUserRoute(db: Database) {
    get("user") {
        try {
            val jsonData = call.receiveNullable<JsonObject>()

            if (jsonData != null && jsonData.containsKey("phoneNumber")) {

                val mobile: String? = jsonData["phoneNumber"]?.jsonPrimitive?.contentOrNull
                if (mobile?.length == 10) {
                    val user = getUser(db, mobile)
                } else {
                    call.respond(HttpStatusCode.OK, ResponseData(false, "", data = JsonObject(emptyMap())))
                    return@get
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseData(false, e.message, data = JsonObject(emptyMap())))
            return@get
        }
    }
}