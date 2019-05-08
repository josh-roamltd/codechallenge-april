package joshvdh.com.codechallengeapril.audiofft

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.concurrent.thread


typealias AudioRecordThreadCallback = (values: List<Double>) -> Unit

class AudioRecordThread(
    context: Context,
    fileName: String,
    private val sampleRate: Int = 44100,
    private val bufferSize: Int = 2048, // should be with power of 2 for correct work of FFT
    private val maxFrequency: Int = 10000,
    private val callback: AudioRecordThreadCallback
) {

    private val audioRecord = AudioRecord(
        1,
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize * 2
    )

    private val audioData = AudioData(bufferSize)
    private var time = 0L
    private val audioStreamBufferSize = 500000

    private val fft = Radix2FFT(bufferSize)
    private val hzPerDataPoint = sampleRate.toDouble() / bufferSize
    private val fftSize = (maxFrequency / hzPerDataPoint).toInt()

    private var fileOutputStream: FileOutputStream? = null
    private var dataOutputStream: DataOutputStream? = null
    private var filePath: File? = null

    private val fftData = mutableListOf<Double>()
    private var running = false

    init {
        if (audioRecord.state != AudioRecord.STATE_INITIALIZED)
            throw UnsupportedOperationException("This device doesn't support AudioRecord")
        filePath = context.getFileStreamPath(fileName)
    }

    fun onStart() {
        audioRecord.startRecording()
    }

    fun runThreadLoop() {
        running = true
        thread(start = true) {
            fileOutputStream = FileOutputStream(filePath)
            dataOutputStream = DataOutputStream(fileOutputStream)

            while (running) {
                processAudioStream()
            }

            dataOutputStream?.close()
            fileOutputStream?.close()
        }
    }

    fun stopThreadLoop() {
        running = false
    }

    private fun processAudioStream() {
        audioRecord.read(audioData.yData, 0, bufferSize)
        audioData.yData.forEach {
            dataOutputStream?.writeShort(it.toInt())
        }

        val itemsArray = audioData.xData
        for (index in 0 until bufferSize)
            itemsArray[index] = time++

        fft.run(audioData.yData, fftData)
        callback(fftData)
    }

    fun onStop() {
        if (audioRecord.state == AudioRecord.STATE_INITIALIZED) {
            audioRecord.stop()
            audioRecord.release()
        }
    }

    fun getBufferSize(): Int = bufferSize
    fun getSampleRate(): Int = sampleRate
}