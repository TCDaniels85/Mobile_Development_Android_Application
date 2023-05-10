package com.cd.assignment_1_chrisdaniels

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

/**
 * Audio class to handle the creation of audio attributes and soundpool objects in
 * order to load sound clips ready to be played
 */
class Audio (context: Context) {
    private var audioAttributes: AudioAttributes
    private var soundPool : SoundPool
    private val crunchEffect: Int
    private val damageEffect:Int

    init{
        audioAttributes = AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(
            AudioAttributes.CONTENT_TYPE_SONIFICATION).build()

        soundPool = SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(1).build()
        crunchEffect = soundPool.load(context,R.raw.crunch,1)
        damageEffect = soundPool.load(context,R.raw.damage,1)
    }


    /**
     * Plays a crunch sound effect
     */
    fun playCrunchEffect(){
        soundPool.play(crunchEffect,1.0f,1.0f,0,0,1.0f)
    }

    /**
     * Plays a sound effect to indicate damage from an enemy
     */
    fun playDamageEffect(){
        soundPool.play(damageEffect,1.0f,1.0f,0,0, 1.0f)
    }

}