package br.edu.ifrs.poa.ifhelptech.ui.fragments.myquestions

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.database.FirebaseManager
import br.edu.ifrs.poa.ifhelptech.model.Question
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class UpdateMyQuestionActivity : AppCompatActivity() {

    private lateinit var questionNameEditText: TextInputEditText
    private lateinit var questionDescriptionEditText: TextInputEditText
    private lateinit var questionTopicEditText: AutoCompleteTextView
    private lateinit var updateButton: Button


    companion object {
        const val EXTRA_QUESTION_ID = "extra_question_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_my_question)

        questionNameEditText = findViewById(R.id.edit_text_question_title)
        questionDescriptionEditText = findViewById(R.id.edit_text_question_description)
        questionTopicEditText = findViewById(R.id.question_topic_edit_text)
        updateButton = findViewById(R.id.button_update)

        val questionTitle = intent.getStringExtra("questionTitle")
        val questionDescription = intent.getStringExtra("questionDescription")

        configTheDropDownMenu()

        questionNameEditText.setText(questionTitle)
        questionDescriptionEditText.setText(questionDescription)


        val questionId = intent.getStringExtra(EXTRA_QUESTION_ID)
        if (questionId == null) {
            finish()
            return
        }
        updateButton.setOnClickListener {

            if (!formIsValid()) {
                Toast.makeText(
                    this@UpdateMyQuestionActivity,
                    "Corrija os erros no formulário!!",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            updateQuestion(questionId)

            finish()
        }
    }


    private fun formIsValid(): Boolean {
        var isValid = true

        val questionTitle = questionNameEditText.text.toString().trim()
        if (questionTitle.isEmpty()) {
            questionNameEditText.error = "Nome do produto é obrigatório"
            isValid = false
        }

        val questionDescription = questionDescriptionEditText.text.toString().trim()
        if (questionDescription.isEmpty()) {
            questionDescriptionEditText.error = "Descrição do produto é obrigatória"
            isValid = false
        }

        val questionTopic = questionTopicEditText.text.toString().trim()
        if (questionTopic.isEmpty()) {
            questionTopicEditText.error = "Um tópico deve ser selecionado"
            isValid = false
        }

        return isValid
    }

    private fun updateQuestion(questionId: String?) {
        val database = FirebaseManager.getDatabase()
        val myRef = database.getReference("perguntas")

        if (questionId != null) {
            myRef.child(questionId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val question = dataSnapshot.getValue(Question::class.java)
                    question?.let {

                        it.questionTitle = questionNameEditText.text.toString().trim()
                        it.questionDescription = questionDescriptionEditText.text.toString().trim()
                        it.questionTopic = questionTopicEditText.text.toString().trim()

                        myRef.child(questionId).setValue(it)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Pergunta atualizada com sucesso!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            }
                            .addOnFailureListener { error ->
                                showErrorModal("Erro ao atulizar pergunta: ${error.message}")
                            }
                    } ?: showErrorModal("Pergunta não encontrada")
                }

                override fun onCancelled(error: DatabaseError) {
                    showErrorModal("Error ao tentar recuperar a pergunta: ${error.message}")
                    finish()
                }
            })
        } else {
            showErrorModal("")
        }
    }

    private fun configTheDropDownMenu() {
        val items = listOf("BD 1", "PW3", "LP 1")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        questionTopicEditText.setAdapter(adapter)

        val questionTopic = intent.getStringExtra("questionTopic") ?: ""
        val selectedIndex = items.indexOf(questionTopic)
        questionTopicEditText.setText(questionTopic, false)
        questionTopicEditText.setSelection(selectedIndex)
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
