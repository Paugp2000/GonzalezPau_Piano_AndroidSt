package com.example.gonzalezpau_piano_androidst

import android.annotation.SuppressLint
import android.media.SoundPool
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private val noteMap = mutableMapOf<Int, Int>()   // viewId → soundId

    // Llista de totes les tecles (blanques i negres)
    private val pianoKeys = listOf(
        PianoKey(R.id.key_c2, R.raw.piano_c2),
        PianoKey(R.id.key_d2, R.raw.piano_d2),
        PianoKey(R.id.key_e2, R.raw.piano_e2),
        PianoKey(R.id.key_f2, R.raw.piano_f2),
        PianoKey(R.id.key_g2, R.raw.piano_g2),
        PianoKey(R.id.key_a2, R.raw.piano_a2),
        PianoKey(R.id.key_b2, R.raw.piano_b2),

        PianoKey(R.id.key_c3, R.raw.piano_c3),
        PianoKey(R.id.key_d3, R.raw.piano_d3),
        PianoKey(R.id.key_e3, R.raw.piano_e3),
        PianoKey(R.id.key_f3, R.raw.piano_f3),
        PianoKey(R.id.key_g3, R.raw.piano_g3),
        PianoKey(R.id.key_a3, R.raw.piano_a3),
        PianoKey(R.id.key_b3, R.raw.piano_b3),

        PianoKey(R.id.key_c4, R.raw.piano_c4),

        // Negres
        PianoKey(R.id.key_c2_sharp, R.raw.piano_cs2),
        PianoKey(R.id.key_d2_sharp, R.raw.piano_ds2),
        PianoKey(R.id.key_f2_sharp, R.raw.piano_fs2),
        PianoKey(R.id.key_g2_sharp, R.raw.piano_gs2),
        PianoKey(R.id.key_a2_sharp, R.raw.piano_as2),

        PianoKey(R.id.key_c3_sharp, R.raw.piano_cs3),
        PianoKey(R.id.key_d3_sharp, R.raw.piano_ds3),
        PianoKey(R.id.key_f3_sharp, R.raw.piano_fs3),
        PianoKey(R.id.key_g3_sharp, R.raw.piano_gs3),
        PianoKey(R.id.key_a3_sharp, R.raw.piano_as3)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSoundPool()
        loadSounds()
        setupTouchListeners()
    }

    private fun initSoundPool() {
        soundPool = SoundPool.Builder()
            .setMaxStreams(10)   // permet multitouch real
            .build()
    }

    private fun loadSounds() {
        pianoKeys.forEach { key ->
            val soundId = soundPool.load(this, key.soundRes, 1)
            noteMap[key.viewId] = soundId
        }
    }

    private fun playNote(viewId: Int) {
        val soundId = noteMap[viewId] ?: return
        soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListeners() {
        pianoKeys.forEach { key ->
            val view = findViewById<View>(key.viewId)

            view.setOnTouchListener { v, event ->
                when (event.actionMasked) {

                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_POINTER_DOWN -> {
                        v.isPressed = true
                        playNote(v.id)
                    }

                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_POINTER_UP,
                    MotionEvent.ACTION_CANCEL -> {
                        v.isPressed = false
                    }
                }
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}

data class PianoKey(
    val viewId: Int,
    val soundRes: Int
)