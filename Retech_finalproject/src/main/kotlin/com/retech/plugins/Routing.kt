package com.retech.plugins

import com.retech.route.routeCategory
import com.retech.route.routeProduct
import com.retech.route.routeUser
import io.ktor.server.application.*

fun Application.configureRouting() {
    routeUser()
    routeCategory()
    routeProduct()
}
