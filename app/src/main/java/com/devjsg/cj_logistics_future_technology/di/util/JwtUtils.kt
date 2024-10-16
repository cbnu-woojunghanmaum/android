package com.devjsg.cj_logistics_future_technology.di.util

import android.util.Base64
import com.auth0.android.jwt.JWT
import org.json.JSONObject

fun decodeJwt(token: String): Pair<String?, String?> {
    return try {
        val jwt = JWT(token)
        val sub = jwt.getClaim("sub").asString()
        val auth = jwt.getClaim("auth").asString()
        Pair(sub, auth)
    } catch (e: Exception) {
        Pair(null, null)
    }
}

fun extractToken(responseBody: String): String? {
    return try {
        val jsonObject = JSONObject(responseBody)
        jsonObject.getJSONObject("data").getString("token")
    } catch (e: Exception) {
        null
    }
}

fun decodeJwtHeader(token: String): JSONObject? {
    return try {
        val parts = token.split(".")
        if (parts.size == 3) {
            val headerJson = String(Base64.decode(parts[0], Base64.URL_SAFE))
            JSONObject(headerJson)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

fun extractRefreshToken(responseBody: String): String? {
    return try {
        val jsonObject = JSONObject(responseBody)
        jsonObject.getJSONObject("data").getString("refreshToken")
    } catch (e: Exception) {
        null
    }
}