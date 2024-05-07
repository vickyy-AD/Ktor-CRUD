package com.vicki.subroutes

import io.ktor.server.routing.*
import org.ktorm.database.Database

fun Route.profileRoute(db: Database) {

    route("profile/") {
        getAllUserRoute(db)
        getSingleUserRoute(db)
    }

}