package ru.nordclan.myapplication.dagger

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.nordclan.myapplication.api.RetrofitApi
import javax.inject.Singleton

@Module(includes = [TokenModule::class])
class ApiModule(
    private val mobileUrl: String,
//    private val cityUrl: String,
    private val trace: Boolean = false
) {

    @Provides
    @Singleton
    fun mapper(): ObjectMapper =
        ObjectMapper()
            .registerModule(KotlinModule())
            .registerModule(JavaTimeModule())
            .registerModule(JodaModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    @Provides
    @Singleton
//    @Named("mobile")
    fun mobileHttpClient(
//        tokenSupplier: TokenSupplier
    ): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            val level = if (trace)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
            setLevel(level)
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(object : Interceptor {

                override fun intercept(chain: Interceptor.Chain): Response {
                    val builder = chain.request().newBuilder()
//                    tokenSupplier.get()?.let { token ->
//                        builder.addHeader("JWT", token)
//                    }
                    return chain.proceed(builder.build())
                }
            })
            .build()
    }

//    @Provides
//    @Singleton
//    @Named("city")
//    fun cityHttpClient(): OkHttpClient {
//        val interceptor = HttpLoggingInterceptor().apply {
//            val level = if (trace)
//                HttpLoggingInterceptor.Level.BODY
//            else
//                HttpLoggingInterceptor.Level.NONE
//            setLevel(level)
//        }
//        return OkHttpClient.Builder()
//            .addInterceptor(interceptor)
//            .addInterceptor(object : Interceptor {
//
//                override fun intercept(chain: Interceptor.Chain): Response {
//                    val url = URL(cityUrl)
//                    val builder = chain.request().newBuilder()
//                        .addHeader("Origin", "${url.protocol}://${url.host}")
//                    return chain.proceed(builder.build())
//                }
//            })
//            .build()
//    }

    @Provides
    @Singleton
//    @Named("mobile")
    fun mobileRetrofit(
        mapper: ObjectMapper,
//        @Named("mobile")
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(mobileUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

//    @Provides
//    @Singleton
//    @Named("city")
//    fun cityRetrofit(
//        mapper: ObjectMapper,
//        @Named("city") client: OkHttpClient
//    ): Retrofit =
//        Retrofit.Builder()
//            .baseUrl(cityUrl)
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(JacksonConverterFactory.create(mapper))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .client(client)
//            .build()

    @Provides
    @Singleton
    fun api(
//        @Named("mobile")
        retrofit: Retrofit
    ): RetrofitApi =
        retrofit.create(RetrofitApi::class.java)

//    @Provides
//    @Singleton
//    fun cityApi(@Named("city") retrofit: Retrofit): CityApi =
//        retrofit.create(CityApi::class.java)
}
