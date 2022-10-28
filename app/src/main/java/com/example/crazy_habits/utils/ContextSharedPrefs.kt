package com.example.crazy_habits.utils

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class ContextSharedPrefs private constructor(private val application : Application) {
    val preferences = application.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE)!!

    companion object {
        var contextSharedPrefs : ContextSharedPrefs? = null

        fun createSharedPrefs(application: Application) : ContextSharedPrefs {
            if (contextSharedPrefs == null) {
                contextSharedPrefs = ContextSharedPrefs(application)
            }
            return contextSharedPrefs!!
        }
        fun getcontextSharedPrefs() = contextSharedPrefs!!
    }
    /**
     * Saves object into the Preferences.
     *
     * @param `object` Object of model class (of type [T]) to save
     * @param key Key with which Shared preferences to
     **/
    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        preferences.edit().putString(key, jsonString).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = preferences.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    inline fun <reified T> getList(key: String) :T? {
        val value = preferences.getString(key, null)
        return GsonBuilder().create().fromJson<T>(value, object : TypeToken<T>() {}.type)
    }




}