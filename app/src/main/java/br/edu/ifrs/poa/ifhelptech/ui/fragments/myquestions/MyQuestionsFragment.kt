package br.edu.ifrs.poa.ifhelptech.ui.fragments.myquestions

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.database.FirebaseManager
import br.edu.ifrs.poa.ifhelptech.model.Question
import br.edu.ifrs.poa.ifhelptech.ui.recyclerview.adapter.MyQuestionAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyQuestionsFragment : Fragment() {

    private lateinit var myQuestionAdapter: MyQuestionAdapter
    private val questionList: MutableList<Question> = mutableListOf()
    private lateinit var currentUserEmail: String
    private lateinit var myRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_questions, container, false)

        currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: "null"

        if (currentUserEmail == "null") {
            showErrorModal("Erro ao carregar os dados, por favor tente novamente depois.")
        }

        val database = FirebaseManager.getDatabase()
        myRef = database.getReference("perguntas")

        val recyclerView = view.findViewById<RecyclerView>(R.id.my_questions_recycler_view)
        myQuestionAdapter = MyQuestionAdapter(questionList,this@MyQuestionsFragment::onDeleteQuestion,this@MyQuestionsFragment::onUpdateQuestion)
        recyclerView.adapter = myQuestionAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                questionList.clear()

                for (questionSnapshot in dataSnapshot.children) {
                    val question = questionSnapshot.getValue(Question::class.java)
                    question?.let {
                        if (it.authorEmail == currentUserEmail) {
                            it.id = questionSnapshot.key!!
                            questionList.add(it)
                        }
                    }
                }

                myQuestionAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showErrorModal("Erro ao carregar os dados, por favor tente novamente depois.")
            }
        })
        return view
    }

    private fun onUpdateQuestion(question: Question) {
        val intent = Intent(requireContext(), UpdateMyQuestionActivity::class.java)
        intent.putExtra(UpdateMyQuestionActivity.EXTRA_QUESTION_ID, question.id)
        intent.putExtra("questionTitle", question.questionTitle)
        intent.putExtra("questionDescription", question.questionDescription)
        intent.putExtra("questionTopic", question.questionTopic)
        startActivity(intent)
    }

    private fun onDeleteQuestion(question: Question) {
        val questionId = question.id

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Excluir pergunta")
        alertDialogBuilder.setMessage("Deseja realmente excluir a pergunta?")
        alertDialogBuilder.setPositiveButton("Sim") { _, _ ->

            myRef.child(questionId).removeValue()
                .addOnSuccessListener {
                    questionList.remove(question)
                    myQuestionAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        requireContext(),
                        "Pergunta apagada com sucesso",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { error ->
                    showErrorModal("Erro ao apagar a pergunta: ${error.message}")
                }
        }
        alertDialogBuilder.setNegativeButton("Não") { _, _ ->
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showErrorModal(errorMessage: String) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Ops! Algo deu errado!")
            .setMessage(errorMessage)
            .setPositiveButton("OK", null)
            .create()
        alertDialog.show()
    }
}