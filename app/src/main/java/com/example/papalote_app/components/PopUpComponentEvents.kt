package com.example.papalote_app.components

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class PopUpComponentEvents(
    private val context: Context,
    private val title: String,
    private val message: String,
    private val options: List<String>
) {
    private lateinit var popupWindow: PopupWindow

    //Metodo para crear contenido del popup
    fun createPopUpView(): View {
        val popupLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.White.toArgb())
            setPadding(50,50,50,50)
        }

        //Titulo
        val titleTextView = TextView(context).apply {
            text = title
            textSize = 18f
            setTextColor(Color.Black.toArgb())
        }
        popupLayout.addView(titleTextView)

        //Mensaje
        val messageTextView = TextView(context).apply {
            text = message
            setTextColor(Color.Gray.toArgb())
            textSize = 16f
        }
        popupLayout.addView(messageTextView)

        /* Lista de opciones */
        options.forEach { option ->
            val optionTextView = TextView(context).apply {
                text = option
                setTextColor(Color.Blue.toArgb())
                textSize = 14f
                setPadding(10,10,10,10)

                //Accion a hacer a la hora de dar click
                setOnClickListener {
                    onOptionClick(option)
                }
            }
            popupLayout.addView(optionTextView)
        }

        return popupLayout
    }

    private fun onOptionClick(option: String) {
        popupWindow.dismiss()
        println("Opción seleccionada: $option")
    }

    fun show(view: View) {
        popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.showAtLocation(view.rootView, Gravity.CENTER, 0, 0)
    }

    fun dismiss() {
        if (::popupWindow.isInitialized && popupWindow.isShowing) {
            popupWindow.dismiss()
        }
    }
}