package app.woovictory.boostcamp.view

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import app.woovictory.boostcamp.R
import app.woovictory.boostcamp.adapter.MovieAdapter
import app.woovictory.boostcamp.data.GetMovieResponse
import app.woovictory.boostcamp.data.MovieResponseData
import app.woovictory.boostcamp.network.ApplicationController
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.item_movie_list.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class MovieActivity : AppCompatActivity(), View.OnClickListener{
    override fun onClick(v: View?) {
        when (v!!) {
            movieSearchButton -> {
                if(mainSearchEdit.text.toString().length>0)
                    getMovie()
                else
                    toast("영화 제목을 입력해주세요!")
                // 검색 버튼 시 키보드 내리기.
                val imm: InputMethodManager = this!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(movieSearchButton.windowToken, 0)
            }
        }
    }

    private val networkService by lazy {
        ApplicationController.instance.networkService
    }
    lateinit var movieItems: ArrayList<MovieResponseData>

    lateinit var movieAdapter: MovieAdapter
    lateinit var progressDialog: ProgressDialog
    private var startItemNumber = 1
    private var finalItemNumber = 10
    private var overlapNetworking: String = "MY_NETWORKING"

    // 아래의 myID와 mySecret는 변수로 가지고 있으면 보안 상 문제로 인해
    // 외부에서 가지고 있거나 apk 추출 후에도 변수 값을 뽑아내지 못하는 방법을 사용해야 한다고 생각함.
    private val myID: String = "390SRJYyF3b4hOZcyPbh"
    private val mySecret: String = "6O4bk2hxJ8"
    fun init() {
        movieSearchButton.setOnClickListener(this)
        movieList.setHasFixedSize(true)
        movieItems = ArrayList()
        progressDialog = ProgressDialog(this@MovieActivity, R.style.AppCompatAlertDialogStyle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        init()

        movieList.setOnScrollChangeListener(object : View.OnScrollChangeListener{
            override fun onScrollChange(v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                // recyclerview 최하단 스크롤 감지
                if(!movieList.canScrollVertically(1)){
                    if(!(startItemNumber>=finalItemNumber)){
                        movieSwipeRefresh.isRefreshing = false
                        getMovieMore()
                    }else{
                        toast("목록을 모두 불러왔습니다.")
                    }
                }
            }

        })
        movieSwipeRefresh.setOnRefreshListener {
            movieSwipeRefresh.isRefreshing = false
        }

    }

    // 영화 검색 더 불러오기 통신
    fun getMovieMore(){
        var movieMoreInfo = networkService.getMovieInfo(myID,mySecret,mainSearchEdit.text.toString(),startItemNumber)

        if(overlapNetworking == "MY_NETWORKING"){

            createDialog()
            overlapNetworking = mainSearchEdit.text.toString()

            movieMoreInfo.enqueue(object : Callback<GetMovieResponse>{
                override fun onFailure(call: Call<GetMovieResponse>, t: Throwable) {
                    Log.v("error movie info not working", t.message)
                    overlapNetworking = "MY_NETWORKING"
                    progressDialog.dismiss()
                    movieSwipeRefresh.isRefreshing = false
                }

                override fun onResponse(call: Call<GetMovieResponse>, response: Response<GetMovieResponse>) {
                    response?.takeIf { it.isSuccessful.equals(true) }
                        ?.body()
                        ?.let {
                            movieAdapter.addItems(it.items)

                            startItemNumber = it.start + 10
                            finalItemNumber = it.total


                            movieSwipeRefresh.isRefreshing = false
                            progressDialog.dismiss()
                        }


                    overlapNetworking = "MY_NETWORKING"
                }

            })
        }
    }

    private fun getMovie() {

        var movieInfo = networkService.getMovieInfo(myID, mySecret, mainSearchEdit.text.toString(),1)

        if (overlapNetworking == "MY_NETWORKING") {

            createDialog()
            overlapNetworking = mainSearchEdit.text.toString()

            movieInfo.enqueue(object : Callback<GetMovieResponse> {
                override fun onFailure(call: Call<GetMovieResponse>, t: Throwable) {
                    Log.v("error movie info not working", t.message)
                    overlapNetworking = "MY_NETWORKING"
                    progressDialog.dismiss()
                    movieSwipeRefresh.isRefreshing = false
                }

                override fun onResponse(call: Call<GetMovieResponse>, response: Response<GetMovieResponse>) {
                    // response가 null이 아닌 경우에 아래 문장 수행
                    response?.takeIf { it.isSuccessful.equals(true) }
                        ?.body()
                        ?.let {
                            if (it.items.size == 0) {
                                var size = movieItems.size
                                // 검색 결과가 없을 경우 리스트 비움
                                movieItems.clear()
                                movieAdapter.notifyItemRangeRemoved(0,size)
                                progressDialog.dismiss()
                                toast("검색 결과가 존재하지 않습니다.")

                            } else {
                                movieItems.clear()
                                movieItems = it.items
                                startItemNumber = it.start + 10
                                finalItemNumber = it.total

                                movieAdapter = MovieAdapter(movieItems, this@MovieActivity)
                                movieList.layoutManager = LinearLayoutManager(this@MovieActivity)
                                movieList.adapter = movieAdapter




                                movieSwipeRefresh.isRefreshing = false
                                progressDialog.dismiss()


                            }
                            overlapNetworking = "MY_NETWORKING"
                        }
                }

            })
        }

    }

    private fun createDialog() {
        progressDialog.setMessage("로딩 중입니다...")
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }
}
