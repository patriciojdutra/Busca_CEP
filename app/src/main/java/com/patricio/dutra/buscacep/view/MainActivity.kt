package com.patricio.dutra.buscacep.view

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.animation.doOnEnd
import com.patricio.dutra.buscacep.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        var t = ObjectAnimator.ofFloat(cardView, "translationY", 400f).apply {
            duration = 2000
            start()
        }

        t.doOnEnd {
            var s = ObjectAnimator.ofFloat(btn, "alpha", 0f,1f).apply {
                duration = 2000
                start()
            }
        }

        btn.setOnClickListener {
            startActivity(Intent(this,
                CepActivity::class.java))
            finish()
        }

    }
}