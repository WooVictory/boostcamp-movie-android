package app.woovictory.boostcamp.network

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by VictoryWoo
 */
class ApplicationController : Application() {


    private var baseUrl : String = "https://openapi.naver.com/"

    lateinit var networkService: NetworkService
    companion object {
        lateinit var instance : ApplicationController
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        buildNetwork()
    }

    private fun buildNetwork(){
        var builder = Retrofit.Builder()
        var retrofit = builder
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        networkService = retrofit.create(NetworkService::class.java)
    }
}