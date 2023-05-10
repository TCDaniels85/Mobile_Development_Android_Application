package com.cd.assignment_1_chrisdaniels

import android.graphics.Canvas
import android.graphics.drawable.Drawable

/**
 * Enemy abstract class which overrides the movement class to provide motion
 * for the collectable game objects
 */
abstract class Enemy(x: Int, y: Int, dx: Int, dy: Int, image: Drawable) : GameObject(x, y, dx, dy, image) {
    var damage: Int = 0 //sets damage variable for enemies


    override fun move(canvas: Canvas) {

        x += dx
        y += dy
        if(x>(canvas.width - width) || x < 0)
            dx = -dx
        super.move(canvas)
    }
}