package com.kiwip.kiwipiv2

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity(), RecognitionListener {

    private lateinit var ibMic: ImageButton
    private lateinit var tvSpeech: TextView
    private lateinit var spRecognizer: SpeechRecognizer
    private var micStatus = false
    private var TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ibMic = findViewById(R.id.ib_microphone)
        tvSpeech = findViewById(R.id.tv_speech)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
        }


        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            spRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            spRecognizer = SpeechRecognizer.createSpeechRecognizer(
                this,
                ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService")
            )
            spRecognizer.setRecognitionListener(this)
            val spRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            ibMic.setOnClickListener {
                micStatus = !micStatus
                if (micStatus) {
                    ibMic.setImageResource(R.drawable.ic_baseline_stop_24)
                    Log.i("MainActivity", "*** start listening")
                    spRecognizer.startListening(spRecognizerIntent)
                } else {
                    ibMic.setImageResource(R.drawable.ic_baseline_mic_24)
                    Log.i("MainActivity", "*** stop listening")
                    spRecognizer.stopListening()
                }
            }
        } else {
            Log.e("MainActivity", "*** Error Speech Recognizer Not Available")
            Toast.makeText(this, "Speech Recognizer Not Available", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {
    }

    override fun onBeginningOfSpeech() {
    }

    override fun onRmsChanged(rmsdB: Float) {
    }

    override fun onBufferReceived(buffer: ByteArray?) {
    }

    override fun onEndOfSpeech() {
    }

    override fun onError(error: Int) {
    }

    override fun onResults(results: Bundle?) {
        val data: ArrayList<String> =
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>
        tvSpeech.text = data[0]
        Log.i("MainActivity", "*** " + data[0])
    }

    override fun onPartialResults(partialResults: Bundle?) {
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
    }


}