package su.lemon4k.connect.model

import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import su.lemon4k.connect.network.*
import su.lemon4k.connect.network.Constants
import su.lemon4k.connect.network.HuskyUserApi
import su.lemon4k.connect.network.SignInInput
import su.lemon4k.connect.network.SignInResponse

data class User(
    val id: Long? = null,
    val avatar_url: String? = null,
    val email: String? = null,
    val username: String? = null) {

    private val constants = Constants()

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }

    fun signIn(input: SignInInput): Response<SignInResponse> {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder()
            .baseUrl(constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val api = retrofit.create(HuskyUserApi::class.java)
        return api.signIn(input).execute()
    }
}
