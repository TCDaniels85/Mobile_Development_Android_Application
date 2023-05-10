package com.cd.assignment_1_chrisdaniels

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cd.assignment_1_chrisdaniels.databinding.ScoresItemBinding

/**
 * Adapter class to bridge scoresItem data and the recycler view
 */
class ScoresAdapter (val scoreList:ArrayList<ScoresItem>) : RecyclerView.Adapter<ScoresAdapter.ViewHolder>() {

    //set itemBinding variable to a type which is a proxy class representing the user interface.
    inner class ViewHolder(var itemBinding: ScoresItemBinding) : RecyclerView.ViewHolder(itemBinding.root){

    }

    /**
     *Inflates layout and sorts the list of scores into descending order
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate xml to create an object which represents the card view and views within
        val itemBinding = ScoresItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        scoreList.sortByDescending { scoresItem -> scoresItem.score }
        return ViewHolder(itemBinding)
    }

    /**
     * For every position in the scoresList, set the UI items in the card view to the corresponding to
     * the item in the scoresList
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scoreItem = scoreList[position]
        holder.itemBinding.nameText.text = scoreItem.name
        holder.itemBinding.scoreText.text = scoreItem.score.toString()
        holder.itemBinding.playerImageView.setImageDrawable(scoreItem.image)
    }

    /**
     * Return size of scoresList
     */
    override fun getItemCount(): Int {
        return scoreList.size
    }
}