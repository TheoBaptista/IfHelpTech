package br.edu.ifrs.poa.ifhelptech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import br.edu.ifrs.poa.ifhelptech.R

class ProgressBarActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var handler: Handler

    private var progressStatus = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_bar)

        progressBar = findViewById<ProgressBar>(R.id.intro_progress_bar)

        handler = Handler(Looper.getMainLooper())

        simulateProgress()
    }

    private fun simulateProgress() {
        Thread {
            while (progressStatus < 100) {
                progressStatus += 10

                // Delay the progress update by 500 milliseconds
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                // Update the progress bar on the main thread
                handler.post {
                    progressBar.progress = progressStatus
                }
            }

            // Progress has finished, start the new activity
            val intent = Intent(this@ProgressBarActivity, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }.start()
    }
}