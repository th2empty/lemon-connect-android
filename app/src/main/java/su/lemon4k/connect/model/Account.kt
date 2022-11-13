package su.lemon4k.connect.model

import su.lemon4k.connect.model.exceptions.InternalServerErrorException
import su.lemon4k.connect.model.exceptions.UnauthorizedException
import su.lemon4k.connect.network.LemonConnectApi
import su.lemon4k.connect.network.response.GetProfileInfoResponse

open class Account {
    private val api = LemonConnectApi().getApi()

    fun getProfileInfo(accessToken: String): GetProfileInfoResponse? {
        val response = api.getProfileInfo("Bearer $accessToken").execute()
        if (response.isSuccessful) {
            return response.body()
        } else {
            when (response.code()) {
                401 -> throw UnauthorizedException(response.message())
                500 -> throw InternalServerErrorException(response.message())
                else -> throw Exception(response.message())
            }
        }
    }

    fun logout(accessToken: String): Boolean? {
        val response = api.logout("Bearer $accessToken").execute()
        return if (response.isSuccessful) {
            true
        } else {
            when (response.code()) {
                401 -> null
                500 -> throw InternalServerErrorException(response.message())
                else -> throw Exception(response.message())
            }
        }
    }
}