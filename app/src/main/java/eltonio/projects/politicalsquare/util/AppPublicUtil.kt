package eltonio.projects.politicalsquare.util

import android.content.Context
import android.widget.Toast

fun toast(contex: Context, msg: String) = Toast.makeText(contex, msg, Toast.LENGTH_SHORT).show()
fun toastLong(context: Context, msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

