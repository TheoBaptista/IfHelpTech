package br.edu.ifrs.poa.ifhelptech.model

data class Question(
    val authorEmail: String = "",
    var questionDescription: String = "",
    var questionTitle: String = "",
    var questionTopic: String = ""
) {

    lateinit var id: String
    constructor() : this("", "", "", "")
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "authorEmail" to authorEmail,
            "questionDescription" to questionDescription,
            "questionTitle" to questionTitle,
            "questionTopic" to questionTopic
        )
    }
}