package com.cd.assignment_1_chrisdaniels

import android.graphics.drawable.Drawable

/**
 * Class to represent the plant cell game object, constructor sets points value and name of object
 */
class PlantCell(x: Int, y: Int, dx: Int, dy: Int, image: Drawable) :
    Collectable(x, y, dx, dy, image) {

        init{
            name = "plant cell"
            pointsValue = 5

        }
}