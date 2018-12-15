package app.woovictory.boostcamp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import app.woovictory.boostcamp.R
import app.woovictory.boostcamp.Util
import app.woovictory.boostcamp.data.MovieResponseData
import app.woovictory.boostcamp.view.WebViewActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_movie_list.view.*
import org.jetbrains.anko.toast
import retrofit2.http.Url

/**
 * Created by VictoryWoo
 */
class MovieAdapter(var movieItems: ArrayList<MovieResponseData>, var context: Context) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_list, parent, false)

        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int = movieItems.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Glide.with(context).load(movieItems[position].image).apply(RequestOptions().centerCrop())
            .into(holder.movieImage)

        holder.movieTitle.text = Util.strpHtml(movieItems[position].title)
        holder.movieDate.text = movieItems[position].pubDate
        holder.movieDirector.text = movieItems[position].director
        holder.movieActors.text = movieItems[position].actor
        holder.movieRating.rating = movieItems[position].userRating.toFloat() / 2.0f

        holder.movieRoot.setOnClickListener{
            //var intent = Intent(Intent.ACTION_VIEW,Uri.parse(movieItems[position].link))

            var intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("link",movieItems[position].link)
            context.startActivity(intent)
        }

    }


    // 리사이클러뷰 하단 스크롤 위치 시 다음 페이지 로드
    // 아이템 추가
    fun addItems(movieData : ArrayList<MovieResponseData>){
        var size = this.movieItems.count()
        Log.v("700 TAG","size : ${size}")
        Log.v("700 TAG","size : ${movieItems.size}")
        movieItems.addAll(movieData)
        notifyItemRangeChanged(0, movieItems.size)
    }

    fun removeItems(){
        movieItems.clear()
        Log.v("800 TAG","size : ${movieItems.size}")
        notifyItemRangeRemoved(0,movieItems.size)

    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movieImage: ImageView = itemView.movieImageItem
        var movieTitle: TextView = itemView.movieTitleItem
        var movieDate: TextView = itemView.movieDateItem
        var movieDirector: TextView = itemView.movieDirectorItem
        var movieActors: TextView = itemView.movieActorsItem
        var movieRating : RatingBar = itemView.movieRatingItem
        var movieRoot : RelativeLayout = itemView.movieItem
    }


}