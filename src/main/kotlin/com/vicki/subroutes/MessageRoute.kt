package com.vicki.subroutes

import io.ktor.server.routing.*

fun Route.sendMessageRoute() {
    route("message/") {
        sendMessageToRoute()
    }
}