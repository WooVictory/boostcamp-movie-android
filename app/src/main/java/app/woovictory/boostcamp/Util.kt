package app.woovictory.boostcamp

import android.text.Html

object Util {

    // 영화 제목에 존재하는 html 태그를 제거하기 위한 함수.
    fun removeHtml(str : String): String {
        return Html.fromHtml(str).toString()
    }
}