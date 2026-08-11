package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.sin

class AudioManager {

    var soundEnabled: Boolean = true
    var musicEnabled: Boolean = true

    private val audioScope = CoroutineScope(Dispatchers.Default)
    private val sampleRate = 22050

    fun playClick() {
        if (!soundEnabled) return
        playToneSequence(floatArrayOf(523.25f), floatArrayOf(0.05f))
    }

    fun playCorrect() {
        if (!soundEnabled) return
        // Bright cheerful ascending arpeggio (C5 -> E5 -> G5 -> C6)
        playToneSequence(
            floatArrayOf(523.25f, 659.25f, 783.99f, 1046.50f),
            floatArrayOf(0.07f, 0.07f, 0.07f, 0.18f)
        )
    }

    fun playWrong() {
        if (!soundEnabled) return
        // Gentle low descending boing (G3 -> Eb3 -> C3)
        playToneSequence(
            floatArrayOf(196.00f, 155.56f, 130.81f),
            floatArrayOf(0.10f, 0.10f, 0.20f)
        )
    }

    fun playHeartLost() {
        if (!soundEnabled) return
        playToneSequence(
            floatArrayOf(220.00f, 185.00f, 164.81f),
            floatArrayOf(0.10f, 0.10f, 0.22f)
        )
    }

    fun playHint() {
        if (!soundEnabled) return
        // Sparkle arpeggio
        playToneSequence(
            floatArrayOf(587.33f, 880.00f, 1174.66f, 1760.00f),
            floatArrayOf(0.06f, 0.06f, 0.08f, 0.15f)
        )
    }

    fun playLevelComplete() {
        if (!soundEnabled) return
        // Triumphant victory fanfare (C5 -> E5 -> G5 -> A5 -> C6 -> E6)
        playToneSequence(
            floatArrayOf(523.25f, 659.25f, 783.99f, 880.00f, 1046.50f, 1318.51f),
            floatArrayOf(0.08f, 0.08f, 0.08f, 0.08f, 0.12f, 0.35f)
        )
    }

    fun playStar() {
        if (!soundEnabled) return
        playToneSequence(floatArrayOf(987.77f, 1318.51f), floatArrayOf(0.08f, 0.15f))
    }

    fun playCoin() {
        if (!soundEnabled) return
        playToneSequence(floatArrayOf(987.77f, 1318.51f), floatArrayOf(0.06f, 0.12f))
    }

    fun playVictory() {
        if (!soundEnabled) return
        playToneSequence(
            floatArrayOf(523.25f, 659.25f, 783.99f, 880f, 1046.50f),
            floatArrayOf(0.08f, 0.08f, 0.08f, 0.12f, 0.35f)
        )
    }

    private fun playToneSequence(frequencies: FloatArray, durations: FloatArray) {
        audioScope.launch {
            try {
                var totalSamples = 0
                for (d in durations) {
                    totalSamples += (sampleRate * d).toInt()
                }

                val pcmData = ShortArray(totalSamples)
                var currentSample = 0

                for (i in frequencies.indices) {
                    val freq = frequencies[i]
                    val duration = durations[i]
                    val numSamples = (sampleRate * duration).toInt()

                    for (j in 0 until numSamples) {
                        val time = j.toDouble() / sampleRate
                        val angle = 2.0 * Math.PI * freq * time
                        // Envelope fade in/out to avoid audio pop
                        val envelope = when {
                            j < 100 -> j / 100.0
                            j > numSamples - 100 -> (numSamples - j) / 100.0
                            else -> 1.0
                        }
                        val sampleValue = (sin(angle) * 30000 * envelope).toInt().coerceIn(-32767, 32767).toShort()
                        if (currentSample < totalSamples) {
                            pcmData[currentSample++] = sampleValue
                        }
                    }
                }

                val bufferSize = Math.max(
                    totalSamples * 2,
                    AudioTrack.getMinBufferSize(
                        sampleRate,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT
                    )
                )

                val audioTrack = AudioTrack.Builder()
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
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(pcmData, 0, totalSamples)
                audioTrack.play()
                // Wait for playback then release
                val totalDurationMs = (durations.sum() * 1000).toLong() + 50
                kotlinx.coroutines.delay(totalDurationMs)
                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                // Ignore audio errors gracefully
            }
        }
    }
}
