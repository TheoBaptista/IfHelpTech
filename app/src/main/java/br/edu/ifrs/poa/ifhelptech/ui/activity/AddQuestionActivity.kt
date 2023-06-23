package br.edu.ifrs.poa.ifhelptech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
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

        configTheDropDownMenu()

        val saveButton = findViewById<Button>(R.id.activity_form_save_button)
        saveButton.setOnClickListener {
            saveQuestion()
            finish()
        }
    }

    private fun configTheDropDownMenu() {
        val items = listOf("BD 1", "PW3", "LP 1")

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        questionTopicEditText.setAdapter(adapter)
    }

    private fun saveQuestion() {
        val questionName = questionNameEditText.text.toString().trim()
        val questionDescription = questionDescriptionEditText.text.toString().trim()
        val questionTopic = questionTopicEditText.text.toString().trim()

        if (questionName.isEmpty()) {
            questionNameEditText.error = "O título da pergunta deve ser preenchido"
            return
        }

        if (questionName.length > 40) {
            questionNameEditText.error = "O título da pergunta não deve exceder 40 caracteres"
            return
        }

        if (questionDescription.isEmpty()) {
            questionDescriptionEditText.error = "A descrição da pergunta deve ser preenchida"
            return
        }

        if (questionDescription.length > 100) {
            questionDescriptionEditText.error = "A descrição da pergunta não deve exceder 100 caracteres"
            return
        }

        if (questionTopic.isEmpty()) {
            questionTopicEditText.error = "Um tópico deve ser selecionado"
            return
        }

        val userEmail = FirebaseAuth.getInstance().currentUser!!.email.orEmpty()
        val database = Firebase.database
        val myRef = database.getReference("perguntas").push() // Generate a unique key for the new question
        val newQuestion = Question(questionName, questionDescription, questionTopic, userEmail)
        myRef.setValue(newQuestion)
    }
}
