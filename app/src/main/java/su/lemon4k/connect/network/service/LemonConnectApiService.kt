package su.lemon4k.connect.network.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import su.lemon4k.connect.network.response.RegisteredResponse
import su.lemon4k.connect.network.input.SignInInput
import su.lemon4k.connect.network.input.SignUpInput
import su.lemon4k.connect.network.response.GetProfileInfoResponse
import su.lemon4k.connect.network.response.SignInResponse
import su.lemon4k.connect.network.response.SignUpResponse

interface LemonConnectApiService {
    @POST("auth/sign-up")
    fun signUp(@Body signUpInput: SignUpInput): Call<SignUpResponse>

    @POST("auth/sign-in")
    fun signIn(@Body signInInput: SignInInput): Call<SignInResponse>

    @GET("auth/find-email")
    fun findEmail(@Query("email") email: String): Call<RegisteredResponse>

    @GET("auth/find-username")
    fun findUsername(@Query("username") username: String): Call<RegisteredResponse>

    @GET("account/getProfileInfo")
    fun getProfileInfo(@Header("Authorization") accessToken: String): Call<GetProfileInfoResponse>
}