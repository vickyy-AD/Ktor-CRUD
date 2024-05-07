package com.vicki.subroutes

import com.vicki.tables.userProfile
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
import org.ktorm.dsl.*

fun Route.loginRoute(db: Database) {
    post("login") {

        try {
            val jsonData = call.receiveNullable<JsonObject>()

            if (jsonData != null && jsonData.containsKey("phoneNumber") && jsonData.containsKey("password")) {

                val mobile: String? = jsonData["phoneNumber"]?.jsonPrimitive?.contentOrNull
                val password: String? = jsonData["password"]?.jsonPrimitive?.contentOrNull
                if (mobile?.length == 10 && password?.length == 6) {
                    val user = getUser(db, mobile)

                    /*db.from(userProfile).select()
                        .where() { userProfile.phoneNumber eq mobile.toString() }
                        .map {
                            JsonObject(
                                mapOf(
                                    "userId" to JsonPrimitive(it[userProfile.userId]),
                                    "userName" to JsonPrimitive(it[userProfile.userName]),
                                    "phoneNumber" to JsonPrimitive(it[userProfile.phoneNumber]),
                                )
                            )
                         }.firstOrNull()*/


                    val pass = db.from(userProfile).select()
                        .where() { userProfile.phoneNumber eq mobile.toString() }
                        .map {
                            it[userProfile.password]
                        }.firstOrNull()

                    if (user != null) {

                        if (password.toString() == pass) {
                            call.respond(
                                HttpStatusCode.OK,
                                ResponseData(true, "Login Successful", data = user)
                            )
                            return@post
                        } else {
                            call.respond(
                                HttpStatusCode.NotFound,
                                ResponseData(false, "Enter Valid Password", data = JsonObject(emptyMap()))
                            )
                            return@post
                        }


                    } else {
                        call.respond(
                            HttpStatusCode.NotFound,
                            ResponseData(false, "User Not Found", data = JsonObject(emptyMap()))
                        )
                        return@post
                    }
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ResponseData(false, "Enter Proper Data", data = JsonObject(emptyMap()))
                    )
                    return@post
                }


            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseData(false, "Enter Proper Data", data = JsonObject(emptyMap()))
                )
                return@post
            }


        } catch (e: Exception) {
            call.respond(HttpStatusCode.OK, ResponseData(false, e.message, data = JsonObject(emptyMap())))
            return@post
        }
    }
}

fun Route.registerRoute(db: Database) {
    post("register") {
        try {
            val jsonData = call.receiveNullable<JsonObject>()

            if (jsonData != null && jsonData.containsKey("phoneNumber") && jsonData.containsKey("userName") && jsonData.containsKey(
                    "password"
                )
            ) {

                val mobile: String? = jsonData["phoneNumber"]?.jsonPrimitive?.contentOrNull
                val userName: String? = jsonData["userName"]?.jsonPrimitive?.contentOrNull
                val password: String? = jsonData["password"]?.jsonPrimitive?.contentOrNull

                val existingUser = getUser(db, mobile)

                if (existingUser == null) {

                    if (validation(userName, mobile, password)) {

                        val exUserName = db.from(userProfile).select()
                            .where() { userProfile.userName eq userName.toString() }
                            .map {
                                it[userProfile.userName]
                            }.firstOrNull()

                        if (exUserName != userName) {
                            db.insert(userProfile) {
                                set(userProfile.userName, userName.toString())
                                set(userProfile.phoneNumber, mobile.toString())
                                set(userProfile.password, password.toString())
                            }

                            call.respond(
                                HttpStatusCode.OK,
                                ResponseData(true, "User Registered Successfully", data = getUser(db, mobile))
                            )
                        }else{
                            call.respond(
                                HttpStatusCode.OK,
                                ResponseData(false, "User Name Exists", data = JsonObject(emptyMap()))
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            ResponseData(false, "Enter Proper Data", data = JsonObject(emptyMap()))
                        )
                        return@post
                    }

                } else {
                    call.respond(
                        HttpStatusCode.OK,
                        ResponseData(false, "User Already Exists", data = JsonObject(emptyMap()))
                    )
                    return@post
                }


            } else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ResponseData(false, "Enter Proper Data", data = JsonObject(emptyMap()))
                )
                return@post
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ResponseData(false, e.message, JsonObject(emptyMap())))
            return@post
        }
    }
}

fun validation(userName: String?, mobile: String?, password: String?): Boolean {
    return if (userName.isNullOrEmpty()) {
        false
    } else if (mobile?.length != 10) {
        false
    } else if (password?.length != 6) {
        false
    } else
        true
}

fun getUser(db: Database, mobile: String?): JsonObject? {
    return db.from(userProfile).select()
        .where() { userProfile.phoneNumber eq mobile.toString() }
        .map {
            JsonObject(
                mapOf(
                    "userId" to JsonPrimitive(it[userProfile.userId]),
                    "userName" to JsonPrimitive(it[userProfile.userName]),
                    "phoneNumber" to JsonPrimitive(it[userProfile.phoneNumber]),
                )
            )
        }.firstOrNull()
}
