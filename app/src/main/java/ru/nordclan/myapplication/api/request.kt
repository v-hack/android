package ru.nordclan.myapplication.api

data class LoginRequest(val login: String, val password: String)

data class TokenRequest(val token: String)
