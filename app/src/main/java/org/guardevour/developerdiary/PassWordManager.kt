package org.guardevour.developerdiary

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.lang.Math.pow
import kotlin.math.pow

class PassWordManager(context: Context) {
    private val preferences = context.getSharedPreferences("AppPref", MODE_PRIVATE)

    fun getPassword() = decode(preferences.getString("PASSWORD", "").toString())

    fun setPassword(newPassword: String){
        preferences.edit().putString("PASSWORD",code(newPassword)).apply()
    }

    private fun decode(string: String): String{
        var resultString = ""
        string.forEach {char->
            resultString += (char.toString().toInt() + 5) % 10
        }
        return resultString
    }

    private fun code(string: String): String{
        var resultString = ""
        string.forEach {char->
            resultString += (char.toString().toInt() + 5) % 10
        }
        return resultString
    }

}