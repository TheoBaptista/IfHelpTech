package br.edu.ifrs.poa.ifhelptech.ui.fragments.myquestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.model.Question
import br.edu.ifrs.poa.ifhelptech.ui.recyclerview.adapter.MyQuestionAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyQuestionsFragment : Fragment() {

    private lateinit var myQuestionAdapter: MyQuestionAdapter
    private val questionList: MutableList<Question> = mutableListOf()
    private lateinit var currentUserEmail: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_questions, container, false)

        currentUserEmail = FirebaseAuth.getInstance().currentUser?.email ?: "null"

        if (currentUserEmail == "null"){
            showErrorModal()
        }

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("perguntas")

        val recyclerView = view.findViewById<RecyclerView>(R.id.my_questions_recycler_view)
        myQuestionAdapter = MyQuestionAdapter(questionList)
        recyclerView.adapter = myQuestionAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                questionList.clear()

                for (questionSnapshot in dataSnapshot.children) {
                    val question = questionSnapshot.getValue(Question::class.java)
                    question?.let {
                        if (it.authorEmail == currentUserEmail){
                        questionList.add(it)
                        }
                    }
                }

                myQuestionAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                showErrorModal()
            }
        })
        return view
    }

    private fun showErrorModal() {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Opa! Algo aconteceu!")
            .setMessage("Erro ao carregar os dados, por favor tente novamente depois.")
            .setPositiveButton("OK", null)
            .create()
        alertDialog.show()
    }
}