package com.example.play

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val uiHandler: Handler = Handler(Looper.getMainLooper())
    private val animFrameMax = 250
    private val cancelToken :CancelToken = CancelToken()
    private var dlJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        animationView.progress = 0.0f

        buttonStart.setOnClickListener {

            GlobalScope.launch {
                cancelToken.cancel = true
                dlJob?.cancel()
                dlJob?.join()

                animationView.progress = 0.0f
                dlJob = GlobalScope.launch {
                    cancelToken.cancel = false
                    downloadDummy(cancelToken)
                }
            }
        }



        buttonCancel.setOnClickListener {

            GlobalScope.launch {
                cancelToken.cancel = true
                dlJob?.cancel()
                dlJob?.join()
            }
        }
    }

    private fun progressViewUpdate(progressPer: Double) {
        if (this.isFinishing) return
        this.animationView.progress = progressPer.toFloat()
        this.animationView.setMaxFrame(animFrameMax)
    }


    private fun downloadDummy(cancelToken : CancelToken) {
        val count = 100
        var index = 0
        while (index < count && !cancelToken.cancel) {
            val progress = index

            uiHandler.post { progressViewUpdate(progress / 100.0) }

            Thread.sleep(100)
            index++
        }
    }
}

class CancelToken {
    var cancel = false
}
