package joshvdh.com.codechallengeapril.audiofft

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import java.io.DataInputStream
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import kotlin.concurrent.thread

class AudioPlayThread(
    context: Context,
    fileName: String,
    private val sampleRate: Int = 44100,
    private val bufferSize: Int = 2048
) {
    private val audioTrack = AudioTrack(
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build(),
        AudioFormat.Builder()
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .build(),
        bufferSize * 2,
        AudioTrack.MODE_STREAM,
        AudioManager.AUDIO_SESSION_ID_GENERATE
    )

    private var fileInputStream: FileInputStream? = null
    private var dataInputStream: DataInputStream? = null
    private var filePath: File? = null
    private var bufferOffset = 0

    private var running = false

    init {
        filePath = context.getFileStreamPath(fileName)
    }

    fun start() {
        running = true
        thread(start = true) {

            fileInputStream = FileInputStream(filePath)
            dataInputStream = DataInputStream(fileInputStream)
            bufferOffset = 0

            audioTrack.play()

            while (running) {
                processInputStream()
            }

            dataInputStream?.close()
            fileInputStream?.close()

//            audioTrack.stop()
        }
    }

    fun stop() {
        running = false
    }

    private fun processInputStream() {
        //Read first buffer size
        var complete = false
        val currentBuffer = ShortArray(bufferSize)
        for (i in 0 until bufferSize) {
            try {
                currentBuffer[i] = dataInputStream?.readShort() ?: 0
            } catch (e: EOFException) {
                //end of file!
                complete = true
            }
        }
        audioTrack.write(currentBuffer, bufferOffset, bufferSize)
        bufferOffset += bufferSize

        if (complete) {
            stop()
        }
    }
}