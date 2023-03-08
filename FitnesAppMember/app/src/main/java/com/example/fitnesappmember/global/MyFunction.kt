package com.example.fitnesappmember.global

import android.database.Cursor
import android.util.Log

class MyFunction {

    companion object{
        fun getValue(cursor: Cursor,columName:String):String{
            var value:String =""

            try {
                val col = cursor.getColumnIndex(columName)
                value = cursor.getString(col)

            }catch (e:Exception){
                e.printStackTrace()
                Log.d("MyFunction", "getValue${e.printStackTrace()}")
                value = ""
            }
            return value
        }
    }
}