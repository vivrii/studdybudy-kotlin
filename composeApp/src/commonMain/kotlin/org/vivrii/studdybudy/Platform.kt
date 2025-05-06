package org.vivrii.studdybudy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform