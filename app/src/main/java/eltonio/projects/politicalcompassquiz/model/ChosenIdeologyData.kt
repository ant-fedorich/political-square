package eltonio.projects.politicalcompassquiz.model

data class ChosenIdeologyData(
    var chosenViewX: Float = -1f,
    var chosenViewY: Float = -1f,
    var horStartScore: Int = -1,
    var verStartScore: Int = -1,
    var chosenIdeologyStringId: String = "",
    var chosenQuizId: Int = -1,
    var startedAt: String = "",
    var horEndScore: Int = -1,
    var verEndScore: Int = -1
)


