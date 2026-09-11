package com.example.game

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class SoundManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Default)

    var soundEnabled: Boolean = true
    var musicEnabled: Boolean = true
    var vibrationEnabled: Boolean = true

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun playTap() {
        if (!soundEnabled) return
        scope.launch {
            playTone(frequency = 580f, durationMs = 35, volume = 0.35f)
        }
    }

    fun playLetterSelect(step: Int) {
        if (vibrationEnabled) {
            vibrate(15)
        }
        if (!soundEnabled) return
        scope.launch {
            // Ascending pitch scale based on word length
            val baseFreq = 440f
            val freq = baseFreq * (1.0f + (step.coerceIn(0, 10) * 0.08f))
            playTone(frequency = freq, durationMs = 45, volume = 0.4f)
        }
    }

    fun playWordFound() {
        if (vibrationEnabled) {
            vibrate(60)
        }
        if (!soundEnabled) return
        scope.launch {
            // Bright cheerful chime: C5, E5, G5, C6
            playSequence(
                listOf(
                    Pair(523.25f, 70),
                    Pair(659.25f, 70),
                    Pair(783.99f, 90),
                    Pair(1046.50f, 180)
                ),
                volume = 0.5f
            )
        }
    }

    fun playError() {
        if (vibrationEnabled) {
            vibrate(40)
        }
        if (!soundEnabled) return
        scope.launch {
            playTone(frequency = 180f, durationMs = 90, volume = 0.3f)
        }
    }

    fun playHint() {
        if (!soundEnabled) return
        scope.launch {
            playSequence(
                listOf(
                    Pair(783.99f, 60),
                    Pair(987.77f, 60),
                    Pair(1318.51f, 120)
                ),
                volume = 0.45f
            )
        }
    }

    fun playCoin() {
        if (!soundEnabled) return
        scope.launch {
            playSequence(
                listOf(
                    Pair(987.77f, 50),
                    Pair(1318.51f, 100)
                ),
                volume = 0.45f
            )
        }
    }

    fun playLevelComplete() {
        if (vibrationEnabled) {
            vibrate(100)
        }
        if (!soundEnabled) return
        scope.launch {
            playSequence(
                listOf(
                    Pair(523.25f, 100),
                    Pair(659.25f, 100),
                    Pair(783.99f, 100),
                    Pair(1046.50f, 220),
                    Pair(1318.51f, 350)
                ),
                volume = 0.6f
            )
        }
    }

    fun vibrate(durationMs: Long) {
        if (!vibrationEnabled) return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {
            // Gracefully ignore vibration errors on unsupported devices
        }
    }

    private fun playTone(frequency: Float, durationMs: Int, volume: Float) {
        val sampleRate = 22050
        val numSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(numSamples)

        for (i in 0 until numSamples) {
            val angle = 2.0 * Math.PI * i / (sampleRate / frequency)
            val envelope = when {
                i < numSamples * 0.1 -> i / (numSamples * 0.1)
                i > numSamples * 0.7 -> (numSamples - i) / (numSamples * 0.3)
                else -> 1.0
            }
            buffer[i] = (sin(angle) * envelope * Short.MAX_VALUE * volume).toInt().toShort()
        }

        try {
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(buffer, 0, buffer.size)
            track.play()
            Thread.sleep(durationMs.toLong() + 20)
            track.release()
        } catch (_: Exception) {}
    }

    private fun playSequence(notes: List<Pair<Float, Int>>, volume: Float) {
        for (note in notes) {
            playTone(note.first, note.second, volume)
        }
    }
}
