package com.example.qrsafe.network

import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import com.google.gson.annotations.SerializedName
import android.util.Base64

data class VirusTotalUrlResponse(
    @SerializedName("data") val data: Data?
)

data class Data(
    @SerializedName("attributes") val attributes: Attributes?
)

data class Attributes(
    @SerializedName("last_analysis_stats") val stats: AnalysisStats?
)

data class AnalysisStats(
    val harmless: Int,
    val malicious: Int,
    val suspicious: Int
)

// --------API Interface---------

interface VirusTotalApi {
    @Headers("x-apikey: 0be61242ed3c91974edc8baa3e0450d8d58a10d4195bf194c61730ff0510ee5e")
    @GET("urls/{encoded_url_id}")
    suspend fun getUrlReport(@Path("encoded_url_id") encodedUrl: String): Response<VirusTotalUrlResponse>

    // endpoint pentru trimiterea URL-urilor la analiza
    @Headers("x-apikey: 0be61242ed3c91974edc8baa3e0450d8d58a10d4195bf194c61730ff0510ee5e")
    @FormUrlEncoded
    @POST("urls")
    suspend fun submitUrl(@Field("url") url: String): Response<VirusTotalUrlResponse>
}

object VirusTotalService {
    private const val BASE_URL = "https://www.virustotal.com/api/v3/"

    val api: VirusTotalApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VirusTotalApi::class.java)
    }

    // Functie utilitara
    fun encodeUrlForVirusTotal(url: String): String {
        val encodedBytes = Base64.encode(url.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
        val encodedString = String(encodedBytes)
        return encodedString.trimEnd('=')
    }
}
