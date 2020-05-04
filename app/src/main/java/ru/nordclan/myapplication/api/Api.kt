package ru.nordclan.myapplication.api

import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class Api @Inject constructor(
    private val retrofitApi: RetrofitApi,
    private val mapper: ObjectMapper
) {

    private val resume: (Throwable) -> ApiResponse = { t ->
        when (t) {
            is HttpException -> {
                val s = t.response()
                    ?.errorBody()
                    ?.string()
                try {
                    mapper.readValue(s, ApiError::class.java)
                } catch (ex: Throwable) {
                    ApiError(t.message ?: "Something error", t.code())
                }
            }
            is UnknownHostException ->
                NetworkError(t.message ?: "", -1)
            else ->
                ApiError("Unknown error", -1)
        }
    }

    fun login(login: String, password: String): Single<ApiResponse> =
        retrofitApi.login(LoginRequest(login, password))
            .map<ApiResponse> { resp ->
                LoginResponse(resp)
            }
            .onErrorReturn(resume)

    fun patientInfo(userId: Long): Single<ApiResponse> =
        retrofitApi.patientInfo(userId)
            .map<ApiResponse> { it }
            .onErrorReturn(resume)

    fun visits(userId: Long): Observable<ApiResponse> =
        retrofitApi.visits(userId)
            .flattenAsObservable<ApiResponse> { it }
            .onErrorReturn(resume)

    fun sendToken(token: String): Single<ApiResponse> =
        retrofitApi.sendToken(TokenRequest(token))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map<ApiResponse> { OkResponse }
            .onErrorReturn(resume)

    fun preparations(userId: Long): Observable<ApiResponse> =
        retrofitApi.preparations(userId)
            .flattenAsObservable<ApiResponse> { it }
            .onErrorReturn(resume)
}
