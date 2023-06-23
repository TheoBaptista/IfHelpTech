package br.edu.ifrs.poa.ifhelptech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import br.edu.ifrs.poa.ifhelptech.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val createAccountButton = findViewById<Button>(R.id.createAccountButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        createAccountButton.setOnClickListener {
            val intent = Intent(this@IntroActivity, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            val intent = Intent(this@IntroActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}