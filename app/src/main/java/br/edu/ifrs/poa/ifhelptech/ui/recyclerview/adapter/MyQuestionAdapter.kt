package br.edu.ifrs.poa.ifhelptech.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.model.Question

class MyQuestionAdapter(
    private val questionList: List<Question>,
    private val onDeleteQuestion: (Question) -> Unit,
    private val onUpdateQuestion: (Question) -> Unit
) : RecyclerView.Adapter<MyQuestionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.my_questions_item, parent, false)
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
            val descriptionTextView =
                itemView.findViewById<TextView>(R.id.question_description_textview)
            val topicTextView = itemView.findViewById<TextView>(R.id.question_topic_textview)
            val updateButton = itemView.findViewById<ImageButton>(R.id.update_button)
            val deleteButton = itemView.findViewById<ImageButton>(R.id.delete_button)

            titleTextView.text = question.questionTitle
            descriptionTextView.text = question.questionDescription
            val topicLabel =
                itemView.context.getString(R.string.topic_label, question.questionTopic)
            topicTextView.text = topicLabel

            updateButton.setOnClickListener {
                onUpdateQuestion(question)
            }

            deleteButton.setOnClickListener {
                onDeleteQuestion(question)
            }
        }
    }
}