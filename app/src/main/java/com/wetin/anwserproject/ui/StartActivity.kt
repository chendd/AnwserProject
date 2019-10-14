package com.wetin.anwserproject.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.wetin.anwserproject.MainActivity
import com.wetin.anwserproject.R
import kotlinx.android.synthetic.main.activity_start.*
import java.util.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class StartActivity : AppCompatActivity() {
     var  timerTask:CountDownTimer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        timerTask=object :CountDownTimer(3000L,1000L){
            /**
             * Callback fired when the time is up.
             */
            override fun onFinish() {
                startActivity(Intent(this@StartActivity,MainActivity::class.java))
                timerTask?.cancel()
                this@StartActivity.finish()
            }

            /**
             * Callback fired on regular interval.
             * @param millisUntilFinished The amount of time until finished.
             */
            override fun onTick(millisUntilFinished: Long) {
                wg_time.text = "${(millisUntilFinished+1000)/1000}s"
            }
        }.start()
        wg_time.setOnClickListener {
            timerTask?.onFinish()
            timerTask?.cancel()
        }
    }




}
