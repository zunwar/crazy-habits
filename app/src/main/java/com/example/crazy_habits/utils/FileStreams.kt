package com.example.crazy_habits.utils

import android.app.Application
import android.content.Context

class FileStreams private constructor(private val application : Application) {

    companion object {
        private var fileStreams : FileStreams? = null

        fun createFileStreams(application: Application) : FileStreams {
            if (fileStreams == null) {
                fileStreams = FileStreams(application)
            }
            return fileStreams!!
        }
        fun getFileStreams() = fileStreams!!
    }

    fun saveListToFile(nameOfFile : String, list: List<String>): Boolean {
        return try {
            application.openFileOutput("$nameOfFile.txt", Context.MODE_PRIVATE).use{
                val a = list.joinToString()
//                val a = ""
                it.write(a.toByteArray())
            }
            true
        }catch (e:Exception) {
            false
        }
    }

    fun getListFromFile(nameOfFile : String) : List<String>{
        application.openFileInput("$nameOfFile.txt").bufferedReader().useLines { lines ->
            val a = lines.fold("") { some, text ->
                "$some$text"
            }
            return a.split(", ").toList()
        }
    }

}