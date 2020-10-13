package eltonio.projects.politicalsquare.models

data class QuizResult (
    var id: Int = -1,
    var userId: String = "",
    var quizId: Int = -1,
    var quizTitle: String = "",
    var ideologyId: String = "",
    var horStartScore: Int = -100,
    var verStartScore: Int = -100,
    var horResultScore: Int = -100,
    var verResultScore: Int = -100,
    var startedAt: String = "",
    var endedAt: String = "",
    var duration: Int = -1,
    var zeroAnswerCnt: Int = -1,
    var avgAnswerTime: Double = 0.0
)