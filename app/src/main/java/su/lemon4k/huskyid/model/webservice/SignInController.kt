package su.lemon4k.huskyid.model.webservice

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignInController(private var input: SignInInput, private var userCallback: UserCallback) : Callback<SignInResponse> {
    private val constants = Constants()

    fun auth() {
        try {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                .baseUrl(constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            val huskyUserApi = retrofit.create(HuskyUserApi::class.java)
            val call = huskyUserApi.signIn(this.input)
            call.enqueue(this)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
        if (response.isSuccessful) {
            val res = response.body()
            userCallback.onResponse(res!!)
        } else {
            userCallback.onFailure(Exception(response.message()))
        }
    }

    override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
        t.printStackTrace()
        userCallback.onFailure(Exception(t.message))
    }
}