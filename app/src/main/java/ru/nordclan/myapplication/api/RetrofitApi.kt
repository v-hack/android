package ru.nordclan.myapplication.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrofitApi {

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Single<String>

    @GET("patientinfo")
    fun patientInfo(@Query("user_id") userId: Long): Single<PatientInfoResponse>

    @GET("visits")
    fun visits(@Query("user_id") userId: Long): Single<List<VisitResponse>>

    @POST("TokenUpdate")
    fun sendToken(@Body tokenRequest: TokenRequest): Single<String>

    @GET("drug")
    fun preparations(@Query("user_id") userId: Long): Single<List<DrugResponse>>
}
