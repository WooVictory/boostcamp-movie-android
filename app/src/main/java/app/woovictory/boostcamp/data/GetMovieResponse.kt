package app.woovictory.boostcamp.data

/**
 * Created by VictoryWoo
 */
data class GetMovieResponse(
    var lastBuildDate: String,
    var total: Int,
    var start: Int,
    var display: Int,
    var items: ArrayList<MovieResponseData>
)

