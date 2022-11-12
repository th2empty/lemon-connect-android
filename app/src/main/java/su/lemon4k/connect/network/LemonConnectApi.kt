package su.lemon4k.connect.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import su.lemon4k.connect.network.service.LemonConnectApiService

class LemonConnectApi {
    private val constants = Constants()
    private val gson = GsonBuilder().setLenient().create()
    private val retrofit = Retrofit.Builder()
        .baseUrl(constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getApi(): LemonConnectApiService = retrofit.create(LemonConnectApiService::class.java)
}