package com.cd.assignment_1_chrisdaniels

import android.graphics.drawable.Drawable

/**
 * Class to represent the algae game object, constructor sets points value and name of object
 */
class Algae(x: Int, y: Int, dx: Int, dy: Int, image: Drawable) : Collectable(x, y, dx, dy, image) {

    init{
        pointsValue = 25
        name = "algae"
    }
}