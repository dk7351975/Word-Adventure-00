package com.example.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class SpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context, this)
    private var isInitialized = false
    var isEnabled: Boolean = true

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("SpeechManager", "English language not supported on TTS")
            } else {
                isInitialized = true
                tts?.setSpeechRate(0.85f) // Child-friendly clear rate
                tts?.setPitch(1.1f)      // Friendly clear tone
            }
        } else {
            Log.e("SpeechManager", "TTS initialization failed")
        }
    }

    fun speakLetter(letter: Char) {
        if (!isEnabled || !isInitialized) return
        val textToSpeak = letter.uppercaseChar().toString()
        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "letter_$letter")
    }

    fun speakWord(word: String) {
        if (!isEnabled || !isInitialized) return
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, "word_$word")
    }

    fun speakPrompt(text: String) {
        if (!isEnabled || !isInitialized) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "prompt_${text.hashCode()}")
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
