package com.cd.assignment_1_chrisdaniels



import androidx.core.graphics.drawable.toDrawable

/**
 * Shared model class to enable data to be shared between fragments.
 * A list containing ScoresItem objects, which contain information for the high scores list
 */
class MyModel() {

    val scoreList = ArrayList<ScoresItem>()
    var currentScore: Int = 0  //tracks score of users current game


    /**
     * Constructor adds test items to scoreslist for demonstration purposes, also sets players current
     * score to 0
     */
    init {
        scoreList.add(ScoresItem("Test Player A", 20, R.drawable.john_test.toDrawable()))
        scoreList.add(ScoresItem("Test Player B", 2000, R.drawable.sue_test.toDrawable()))
        scoreList.add(ScoresItem("Test Player C", 4000, R.drawable.john_test.toDrawable()))
        scoreList.add(ScoresItem("Test Player D", 20495, R.drawable.sue_test.toDrawable()))

        currentScore = 0

    }

 
}