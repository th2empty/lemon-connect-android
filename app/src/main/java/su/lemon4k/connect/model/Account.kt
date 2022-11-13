package su.lemon4k.connect.model

import android.util.Log
import su.lemon4k.connect.model.exceptions.InternalServerError
import su.lemon4k.connect.model.exceptions.UnauthorizedException
import su.lemon4k.connect.network.LemonConnectApi
import su.lemon4k.connect.network.response.GetProfileInfoResponse

open class Account {

    fun getProfileInfo(accessToken: String): GetProfileInfoResponse? {
        val api = LemonConnectApi().getApi()
        Log.i("Account", api.toString())
        val response = api.getProfileInfo("Bearer $accessToken").execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            when (response.code()) {
                401 -> throw UnauthorizedException(response.message())
                500 -> throw InternalServerError(response.message())
                else -> throw Exception(response.message())
            }
        }
    }
}