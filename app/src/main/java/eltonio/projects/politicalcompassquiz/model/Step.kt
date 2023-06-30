package eltonio.projects.politicalcompassquiz.model

data class Step (
    var questionIndex: Int = 0,
    var rbIndex: Int = -1,
    var scale: String = "",
    var point: Float = -100f
)