package com.cd.assignment_1_chrisdaniels

import android.graphics.drawable.Drawable

/**
 * Class to represent the nematode enemy game object, constructor sets damage value and name of object
 */
class Nematode(x: Int, y: Int, dx: Int, dy: Int, image: Drawable) : Enemy(x, y, dx, dy, image) {

    init{
        name = "nematode"
        damage = 4
    }
}