package eltonio.projects.politicalcompassquiz.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View
import eltonio.projects.politicalcompassquiz.util.AppUtil.convertDpToPx
import eltonio.projects.politicalcompassquiz.util.TAG

class ChoosePointView(context: Context, var inputX: Float, var inputY: Float, var radiusInDp: Float): View(context) {
    private var paint = Paint()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val pointX = inputX
        val pointY = inputY

        Log.i(TAG, "Canvas: " + canvas?.height + " " + canvas?.width)

        val radius = convertDpToPx(context, radiusInDp) //10f
        val strokeWidthPx = convertDpToPx(context, 2f)

        paint.apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }
        canvas?.drawCircle(pointX, pointY, radius, paint)

        paint.apply {
            style = Paint.Style.STROKE
            color = Color.BLACK
            strokeWidth = strokeWidthPx
        }
        canvas?.drawCircle(pointX, pointY, radius, paint)

        fun getCanvas(): Canvas? {
            return canvas
        }
    }
}