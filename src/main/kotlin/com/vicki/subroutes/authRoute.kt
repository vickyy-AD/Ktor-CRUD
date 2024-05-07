package com.vicki.subroutes

import io.ktor.server.routing.*
import org.ktorm.database.Database

fun Route.authRoute(db: Database) {
    route("auth/") {
        loginRoute(db)
        registerRoute(db)
    }
}