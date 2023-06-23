package br.edu.ifrs.poa.ifhelptech.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifrs.poa.ifhelptech.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtSenha: TextInputEditText
    private lateinit var edtConfSenha: TextInputEditText
    private lateinit var btnCadastrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.activity_create_account_login_TextInputEditText)
        edtSenha = findViewById(R.id.activity_create_account_password_TextInputEditText)
        edtConfSenha = findViewById(R.id.activity_create_account_confirm_password_TextInputEditText)
        btnCadastrar = findViewById(R.id.activity_create_account_button)

        btnCadastrar.setOnClickListener {
            val email = edtEmail.text.toString()
            val senha = edtSenha.text.toString()
            val confSenha = edtConfSenha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty() && confSenha.isNotEmpty()) {
                if (senha == confSenha) {
                    if (senha.length <= 8) {
                        auth.createUserWithEmailAndPassword(email, senha)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Erro ao cadastrar usuário!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Senha deve ter até 8 caracteres!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Senha e confirmação de senha devem ser iguais!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Informe os dados para o cadastro!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}