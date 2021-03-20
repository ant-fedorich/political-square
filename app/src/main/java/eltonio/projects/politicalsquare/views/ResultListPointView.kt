package eltonio.projects.politicalsquare.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import eltonio.projects.politicalsquare.util.AppUtil.convertDpToPx

class ResultListPointView(context: Context, var horScore: Int, var verScore: Int) : View(context) {

    private var paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val horResultScore = horScore
        val verResultScore = verScore

        val compassX =
            convertDpToPx((horResultScore + 40).toFloat())
        val compassY =
            convertDpToPx((verResultScore + 40).toFloat())
        val step = convertDpToPx(1f)
        val radius = convertDpToPx(6f)
        val strokeWidthPx =
            convertDpToPx(2f)

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