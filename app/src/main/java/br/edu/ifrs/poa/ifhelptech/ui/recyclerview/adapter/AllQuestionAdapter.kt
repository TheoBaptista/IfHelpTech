package br.edu.ifrs.poa.ifhelptech.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.model.Question

class AllQuestionAdapter(private val questionList: List<Question>) : RecyclerView.Adapter<AllQuestionAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.all_questions_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = questionList[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(question: Question) {
            val titleTextView = itemView.findViewById<TextView>(R.id.question_title_textview)
            val descriptionTextView = itemView.findViewById<TextView>(R.id.question_description_textview)
            val topicTextView = itemView.findViewById<TextView>(R.id.question_topic_textview)
            val authorTextView = itemView.findViewById<TextView>(R.id.email_author_textview)

            titleTextView.text = question.questionTitle
            descriptionTextView.text = question.questionDescription
            topicTextView.text = question.questionTopic
            authorTextView.text = question.authorEmail
        }
    }
}