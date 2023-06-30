package eltonio.projects.politicalcompassquiz.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import eltonio.projects.politicalcompassquiz.util.AppUtil.convertDpToPx

class ResultListPointView(context: Context, var horScore: Int, var verScore: Int) : View(context) {
    private var paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val horResultScore = horScore
        val verResultScore = verScore

        val compassX =
            convertDpToPx(context, (horResultScore + 40).toFloat())
        val compassY =
            convertDpToPx(context, (verResultScore + 40).toFloat())
        val step = convertDpToPx(context, 1f)
        val radius = convertDpToPx(context, 6f)
        val strokeWidthPx = convertDpToPx(context, 2f)

        paint.apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#51BEF4")
        }
        canvas?.drawCircle(compassX, compassY, radius, paint)

        paint.apply {
            style = Paint.Style.STROKE
            color = Color.parseColor("#23609B")
            strokeWidth = strokeWidthPx
        }
        canvas?.drawCircle(compassX, compassY, radius, paint)
    }

}