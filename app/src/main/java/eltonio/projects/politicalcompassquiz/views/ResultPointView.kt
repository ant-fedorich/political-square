package eltonio.projects.politicalcompassquiz.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import eltonio.projects.politicalcompassquiz.util.AppUtil.convertDpToPx

class ResultPointView (
        context: Context,
        var radiusInDp: Float,
        var radiusResultInDp: Float,
        val chosenViewX: Float,
        val chosenViewY: Float,
        val compassX: Int,
        val compassY: Int
    ) : View(context) {

    private var paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val step = convertDpToPx(context, 4f) // a cell size
        val radius = convertDpToPx(context, radiusInDp)
        val radiusResult = convertDpToPx(context, radiusResultInDp)
        val stokeWidthPx = convertDpToPx(context, 2f)
        //val centerCoeff = step * 0.5f // coefficient to center the point in a cell

        // Draw a chosen view point
        paint.apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = stokeWidthPx
        }
        canvas?.drawCircle(chosenViewX, chosenViewY, radius, paint)
        paint.apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }
        canvas?.drawCircle(chosenViewX, chosenViewY, radius, paint)



        // Draw a result point
        if (compassX != null && compassY != null) {
            paint.apply {
                style = Paint.Style.FILL
                color = Color.parseColor("#51BEF4")
            }
            canvas?.drawCircle((compassX * step), (compassY * step), radiusResult, paint)

            paint.apply {
                style = Paint.Style.STROKE
                color = Color.parseColor("#23609B")
                strokeWidth = stokeWidthPx
            }
            canvas?.drawCircle((compassX * step), (compassY * step), radiusResult, paint)
        }
    }
}