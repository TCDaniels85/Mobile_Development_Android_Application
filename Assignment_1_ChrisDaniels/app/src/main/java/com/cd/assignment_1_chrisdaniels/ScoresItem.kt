package com.cd.assignment_1_chrisdaniels

import android.graphics.drawable.Drawable

/**
 * Data item for use on high scores fragment, contains variables to for user name, score, and an image/photo
 */
data class ScoresItem(var name:String, var score:Int, var image: Drawable) {
}