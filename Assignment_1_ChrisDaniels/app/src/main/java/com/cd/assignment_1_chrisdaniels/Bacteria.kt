package com.cd.assignment_1_chrisdaniels

import android.graphics.drawable.Drawable

/**
 * Class to represent the bacteria game object, constructor sets points value and name of object
 */
class Bacteria(x: Int, y: Int, dx: Int, dy: Int, image: Drawable) :
    Collectable(x, y, dx, dy, image) {

        init{
            pointsValue = 15
            name = "bacteria"
        }
}