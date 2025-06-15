package com.github.speak2me.app.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
