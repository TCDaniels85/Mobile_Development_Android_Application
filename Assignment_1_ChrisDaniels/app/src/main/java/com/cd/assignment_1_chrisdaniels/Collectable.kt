package com.cd.assignment_1_chrisdaniels

import android.graphics.Canvas
import android.graphics.drawable.Drawable

/**
 * Collectable abstract class which overrides the movement class to provide motion
 * for the collectable game objects
 */
abstract class Collectable(x: Int, y: Int, dx: Int, dy: Int, image: Drawable) :
    GameObject(x, y, dx, dy, image) {
        var pointsValue: Int = 0 //creates variable for points value



    override fun move(canvas: Canvas) {

        y+=dy
        x+=dx
        if(x>(canvas.width - width) || x < 0) //prevents object moving past the horizontal screen boundary
            dx = -dx
        super.move(canvas)
    }
}