package br.edu.ifrs.poa.ifhelptech.ui.recyclerview.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifrs.poa.ifhelptech.R
import br.edu.ifrs.poa.ifhelptech.model.Question

class AllQuestionAdapter(private val questionList: List<Question>) :
    RecyclerView.Adapter<AllQuestionAdapter.ViewHolder>() {

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
            val descriptionTextView =
                itemView.findViewById<TextView>(R.id.question_description_textview)
            val topicTextView = itemView.findViewById<TextView>(R.id.question_topic_textview)
            val authorTextView = itemView.findViewById<TextView>(R.id.email_author_textview)
            val sharedButton = itemView.findViewById<ImageButton>(R.id.shared_button)

            titleTextView.text = question.questionTitle
            descriptionTextView.text = question.questionDescription
            val topicLabel =
                itemView.context.getString(R.string.topic_label, question.questionTopic)
            topicTextView.text = topicLabel
            val userLabel = itemView.context.getString(R.string.user_label, question.authorEmail)
            authorTextView.text = userLabel

            sharedButton.setOnClickListener {
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    data = Uri.parse("mailto:${question.authorEmail}")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(question.authorEmail))
                    putExtra(Intent.EXTRA_SUBJECT, "Resposta à pergunta: ${question.questionTitle}")
                    putExtra(
                        Intent.EXTRA_TEXT, "A resposta a essa pergunta é "
                    )
                    type = "text/plain"
                }
                itemView.context.startActivity(Intent.createChooser(emailIntent, "Enviar email"))
            }
        }
    }
}