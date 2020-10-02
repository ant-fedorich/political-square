package eltonio.projects.politicalcompassquiz.models

import android.provider.BaseColumns

object QuizContract {
    // KEY = COLUMNS
    object QuestionsEntry: BaseColumns {
        const val TABLE_NAME = "Questions"
        const val COLUMN_QUIZID = "quizId"
        const val COLUMN_QUESTION_RU = "question_ru"
        const val COLUMN_QUESTION_UK = "question_uk"
        const val COLUMN_QUESTION_EN = "question_en"
        const val COLUMN_SCALE = "scale"
        const val _ID = "id"
    }
    object AnswersEntry: BaseColumns {
        const val TABLE_NAME = "Answers"
        const val COLUMN_ANSWER_RU = "answer_ru"
        const val COLUMN_ANSWER_UK = "answer_uk"
        const val COLUMN_ANSWER_EN = "answer_en"
        const val COLUMN_POINT = "point"
        const val _ID = "id"
    }

    object QuizEntry: BaseColumns {
        const val TABLE_NAME = "Quiz"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val _ID = "id"
    }
    object QuizResultEntry: BaseColumns {
        const val TABLE_NAME = "QuizResult"
        const val COLUMN_QUIZ_ID = "quizId"
        const val COLUMN_IDEOLOGY_ID = "ideologyStringId"
        const val COLUMN_HOR_START_SCORE = "horStartScore"
        const val COLUMN_VER_START_SCORE = "verStartScore"
        const val COLUMN_HOR_RESULT_SCORE = "horResultScore"
        const val COLUMN_VER_RESULT_SCORE = "verResultScore"
        const val COLUMN_STARTEDAT = "startedAt"
        const val COLUMN_ENDEDAT = "endedAt"
        const val COLUMN_DURATION = "duration"
        const val COLUMN_ZERO_ANSWER_CNT = "zeroAnswerCnt"
        const val COLUMN_AVG_ANSWER_TIME = "avgAnswerTime"
        const val _ID = "id"
    }
}