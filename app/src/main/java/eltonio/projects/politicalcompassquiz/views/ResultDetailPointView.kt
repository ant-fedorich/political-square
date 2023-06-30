package eltonio.projects.politicalcompassquiz.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import eltonio.projects.politicalcompassquiz.util.AppUtil.convertDpToPx

class ResultDetailPointView(
    context: Context,
    var horStartScore: Int,
    var verStartScore: Int,
    var horResultScore: Int,
    var verResultScore: Int
) : View(context) {

    private var paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val step = convertDpToPx(context, 4f) // a cell size
        val radius = convertDpToPx(context, 10f)
        val radiusResult = convertDpToPx(context, 12f)
        val stokeWidthPx = convertDpToPx(context, 2f)

        val startX = (horStartScore + 40).toFloat()
        val startY = (verStartScore + 40).toFloat()
        val resultX = (horResultScore + 40).toFloat()
        val resultY = (verResultScore + 40).toFloat()
        //val centerCoeff = step * 0.5f // coefficient to center the point in a cell

        // Draw a start point
        paint.apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = stokeWidthPx
        }
        canvas?.drawCircle(startX * step, startY * step, radius, paint)

        paint.apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }
        canvas?.drawCircle(startX * step, startY * step, radius, paint)

        // Draw a result point
        paint.apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#51BEF4")
        }
        canvas?.drawCircle(resultX * step, resultY * step, radiusResult, paint)

        paint.apply {
            style = Paint.Style.STROKE
            color = Color.parseColor("#23609B")
            strokeWidth = stokeWidthPx
        }
        canvas?.drawCircle(resultX * step, resultY * step, radiusResult, paint)
    }

}