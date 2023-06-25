package br.edu.ifrs.poa.ifhelptech.ui.fragments.allquestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.database.FirebaseManager
import br.edu.ifrs.poa.ifhelptech.model.Question
import br.edu.ifrs.poa.ifhelptech.ui.recyclerview.adapter.AllQuestionAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllQuestionsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllQuestionAdapter
    private val questionList: MutableList<Question> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_questions_fragment, container, false)

        recyclerView = view.findViewById(R.id.all_questions_recycler_view)
        adapter = AllQuestionAdapter(questionList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val database = FirebaseManager.getDatabase()
        val myRef = database.getReference("perguntas")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                questionList.clear()

                for (questionSnapshot in dataSnapshot.children) {
                    val question = questionSnapshot.getValue(Question::class.java)

                    question?.let {
                        it.id = questionSnapshot.key!!
                        questionList.add(it)
                    }
                }

                adapter.notifyDataSetChanged()
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