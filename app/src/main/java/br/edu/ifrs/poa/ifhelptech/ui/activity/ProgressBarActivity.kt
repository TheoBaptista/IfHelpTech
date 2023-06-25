package br.edu.ifrs.poa.ifhelptech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.database.FirebaseManager

class ProgressBarActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var handler: Handler

    private var progressStatus = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_bar)

        progressBar = findViewById<ProgressBar>(R.id.intro_progress_bar)

        FirebaseManager.initialize()

        handler = Handler(Looper.getMainLooper())

        simulateProgress()
    }

    private fun simulateProgress() {
        Thread {
            while (progressStatus < 100) {
                progressStatus += 10
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                handler.post {
                    progressBar.progress = progressStatus
                }
            }
            val intent = Intent(this@ProgressBarActivity, IntroActivity::class.java)
            startActivity(intent)
            finish()
        }.start()
    }
}