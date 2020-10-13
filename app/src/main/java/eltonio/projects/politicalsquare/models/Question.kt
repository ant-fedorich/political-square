package eltonio.projects.politicalsquare.models

data class Question (
    var id: Int = -1,
    var question: String = "",
    var scale: String = "",
    var answerList: List<Answer> = mutableListOf()
)