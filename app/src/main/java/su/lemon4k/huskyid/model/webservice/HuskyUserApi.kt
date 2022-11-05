package su.lemon4k.huskyid.model.webservice

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HuskyUserApi {
    @POST("auth/sign-in")
    fun signIn(@Body signInInput: SignInInput): Call<SignInResponse>
}