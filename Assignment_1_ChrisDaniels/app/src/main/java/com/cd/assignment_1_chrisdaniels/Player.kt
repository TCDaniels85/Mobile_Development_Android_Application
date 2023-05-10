package com.cd.assignment_1_chrisdaniels

import android.graphics.Canvas
import android.graphics.drawable.Drawable

/**
 * Player class which which represents the user controlled character, dx and dy variables are altered via the
 * GameSurfaceView which receives data from the sensors to provide data for these variables
 */
class Player (x: Int, y: Int, dx: Int, dy: Int, image: Drawable) : GameObject(x, y, dx, dy, image) {


    init{
        name = "player"
    }

    /**
     * Overridden move method, performs checks to ensure the player will not go beyond the screen boundaries
     */
    override fun move(canvas: Canvas) {
        if((x>(canvas.width - width) && dx > 0 )|| (x < 0 && dx < 0))
            dx = 0
        else
            x+=dx
        if((y>(canvas.height - (height +20)) && dy > 0 ) || (y<0 && dy<0))
            dy = 0
        else
            y+=dy

        super.move(canvas)

    }
}