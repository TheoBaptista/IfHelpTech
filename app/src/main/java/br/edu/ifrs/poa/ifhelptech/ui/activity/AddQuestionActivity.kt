package br.edu.ifrs.poa.ifhelptech.ui.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.model.Question
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddQuestionActivity : AppCompatActivity() {
    private lateinit var questionNameEditText: TextInputEditText
    private lateinit var questionDescriptionEditText: TextInputEditText
    private lateinit var questionTopicEditText: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        questionNameEditText = findViewById(R.id.question_name_edit_text)
        questionDescriptionEditText = findViewById(R.id.question_description_edit_text)
        questionTopicEditText = findViewById(R.id.question_topic_edit_text)

        configureDropDownMenu()

        val saveButton = findViewById<Button>(R.id.activity_form_save_button)
        saveButton.setOnClickListener {
            if (saveQuestion()) {
                finish()
            }
        }
    }

    private fun configureDropDownMenu() {
        val items = listOf("BD 1", "PW3", "LP 1")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        questionTopicEditText.setAdapter(adapter)
    }

    private fun saveQuestion(): Boolean {
        val questionName = questionNameEditText.text.toString().trim()
        val questionDescription = questionDescriptionEditText.text.toString().trim()
        val questionTopic = questionTopicEditText.text.toString().trim()

        if (!formIsValid()) {
            Toast.makeText(
                this@AddQuestionActivity,
                "Corrija os erros no formulário!!",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        val userEmail = FirebaseAuth.getInstance().currentUser!!.email.orEmpty()
        val database = Firebase.database
        val myRef = database.getReference("perguntas").push()
        val question = Question(
            authorEmail = userEmail,
            questionDescription = questionDescription,
            questionTitle = questionName,
            questionTopic = questionTopic
        )

        val questionId = ""
        question.id = questionId

        myRef.child(questionId).setValue(question)
            .addOnSuccessListener {
                Toast.makeText(this, "pergunta cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { error ->
                showErrorModal("Erro ao salvar a pergunta: ${error.message}")
            }

        return true
    }

    private fun formIsValid(): Boolean {
        var isValid = true

        val questionName = questionNameEditText.text.toString().trim()
        val questionDescription = questionDescriptionEditText.text.toString().trim()
        val questionTopic = questionTopicEditText.text.toString().trim()

        if (questionName.isEmpty()) {
            questionNameEditText.error = "O título da pergunta deve ser preenchido"
            isValid = false
        }

        if (questionName.length > 50) {
            questionNameEditText.error = "O título da pergunta não deve exceder 50 caracteres"
            isValid = false
        }

        if (questionDescription.isEmpty()) {
            questionDescriptionEditText.error = "A descrição da pergunta deve ser preenchida"
            isValid = false
        }

        if (questionDescription.length > 200) {
            questionDescriptionEditText.error =
                "A descrição da pergunta não deve exceder 200 caracteres"
            isValid = false
        }

        if (questionTopic.isEmpty()) {
            questionTopicEditText.error = "Um tópico deve ser selecionado"
            isValid = false
        }

        return isValid
    }

    private fun showErrorModal(errorMessage: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Ops! Algo deu errado!")
            .setMessage(errorMessage)
            .setPositiveButton("OK", null)
            .create()
        alertDialog.show()
    }
}

