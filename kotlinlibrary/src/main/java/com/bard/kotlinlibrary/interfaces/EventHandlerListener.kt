package com.bard.kotlinlibrary.interfaces

import android.content.Context
import android.view.View
import android.widget.Toast

class EventHandlerListener constructor(var context: Context) {

    fun btnClick(view: View){
        Toast.makeText(context, "btn clicked", Toast.LENGTH_SHORT).show()
    }
}