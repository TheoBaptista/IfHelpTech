package br.edu.ifrs.poa.ifhelptech.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import br.edu.ifrs.poa.ifhelptech.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtSenha: TextInputEditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        edtEmail = findViewById(R.id.activity_login_account_login_TextInputEditText)
        edtSenha = findViewById(R.id.activity_login_account_password_TextInputEditText)
        btnLogin = findViewById(R.id.button)

        if (usuarioLogado()) {
            openMainWindow()
        } else {
            btnLogin.setOnClickListener {
                if (!edtEmail.text.toString().isEmpty() && !edtSenha.text.toString().isEmpty()) {
                    validateLogin(edtEmail.text.toString(), edtSenha.text.toString())
                } else {
                    Toast.makeText(this, "Informe email e senha!", Toast.LENGTH_SHORT).show()
                }
            }
            auth = FirebaseAuth.getInstance()
        }



    }

    private fun usuarioLogado(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }

    private fun validateLogin(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    openMainWindow()
                    Toast.makeText(this, "sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Dados de login inválidos!", Toast.LENGTH_SHORT).show()
                    Log.d("LOGIN", "dados inválidos!")
                }
            }
    }

    private fun openMainWindow() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}