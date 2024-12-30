package com.example.musicplayer.ui.theme

import java.util.Locale
import java.util.concurrent.TimeUnit


object Util{
    fun calculateDuration(milli:Long):String{
        val hours = TimeUnit.MILLISECONDS.toHours(milli)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milli) - TimeUnit.HOURS.toMinutes(hours)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milli) - TimeUnit.MINUTES.toSeconds(minutes)


        val formattedDuration=String.format(Locale.ENGLISH,"%02d:%02d:%02d", hours,minutes,seconds)
        return formattedDuration
    }

    fun truncateName(name:String):String{
        var truncName=""
        if (name.endsWith("mp3")){
            truncName=name.substring(startIndex = 0, endIndex =name.length-4)
        }
        return truncName
    }
}