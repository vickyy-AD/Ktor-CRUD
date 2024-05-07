package com.vicki.plugins

import com.vicki.utility.ResponseData
import com.vicki.subroutes.authRoute
import com.vicki.subroutes.profileRoute
import com.vicki.tables.userProfile
import com.vicki.userModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.configureRouting(db: Database) {
    routing {
        route("api/") {

            authRoute(db)
            profileRoute(db)


            get("/table") {

                //return list of data using model

                var list: List<userModel> = ArrayList()

                list = db.from(userProfile).select().map {
                    userModel(
                        it[userProfile.userId]!!,
                        it[userProfile.userName] ?: "",
                        it[userProfile.phoneNumber]!!
                    )
                }

                //return single data using model

                /* val user = db.from(userProfile).select()
                     .where { userProfile.phoneNumber eq "1234567890" }
                     .map {
                         userModel(
                             it[userProfile.userId]!!,
                             it[userProfile.userName] ?: "",
                             it[userProfile.phoneNumber]!!
                         )
                     }.firstOrNull()*/


                //return single data using JsonObject

                /*val user = db.from(userProfile).select()
                    .where { userProfile.phoneNumber eq "1234567890" }
                    .map {
                        JsonObject(
                            mapOf(
                                "userId" to JsonPrimitive(it[userProfile.userId]),
                                "userName" to JsonPrimitive(it[userProfile.userName]),
                                "phoneNumber" to JsonPrimitive(it[userProfile.phoneNumber]),
                            )
                        )
                    }.firstOrNull()*/

                call.respond(
                    HttpStatusCode.OK,
                    ResponseData(true, message = "Hi Mame", data = list)
                )

            }

        }
    }
}




