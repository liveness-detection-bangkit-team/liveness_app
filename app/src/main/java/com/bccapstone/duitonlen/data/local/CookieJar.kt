package com.bccapstone.duitonlen.data.local

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class DuitOnlenCookieJar: CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>() // domain to cookies

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    fun clearCookies() {
        cookieStore.clear()
    }
}