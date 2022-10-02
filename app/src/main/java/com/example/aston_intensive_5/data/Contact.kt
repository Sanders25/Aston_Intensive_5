package com.example.aston_intensive_5.data

import android.graphics.Color
import kotlin.random.Random

data class Contact(
    var name: String,
    var number: String,
) {
    var initials: String = ""
    var backgroundColor: Int = 0

    init {
        if (name.last().isWhitespace())
            name = name.dropLast(1)
        for (c in name.split(" ")) {
            initials += c[0].uppercase()
        }
        val rnd = Random
        backgroundColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}
