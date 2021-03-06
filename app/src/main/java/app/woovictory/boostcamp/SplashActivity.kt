package app.woovictory.boostcamp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import app.woovictory.boostcamp.view.MovieActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, MovieActivity::class.java))
            finish()
        }, 2000)
    }
}
