package app.woovictory.boostcamp.network

import app.woovictory.boostcamp.data.GetMovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by VictoryWoo
 */
interface NetworkService {

    @GET("v1/search/movie.json")
    fun getMovieInfo(
        @Header("X-Naver-Client-Id") X_Naver_Client_Id : String,
        @Header("X-Naver-Client-Secret") X_Naver_Client_Secret : String,
        @Query("query") query : String,
        @Query("start") start :Int
    ) : Call<GetMovieResponse>
}