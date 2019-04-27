package joshvdh.com.codechallengeapril.audiofft

import android.media.AudioFormat
import android.media.AudioRecord
import kotlin.concurrent.thread

typealias AudioRecordThreadCallback = (values: List<Double>) -> Unit
class AudioRecordThread(
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

//    val fftVM = FFTViewModel(context, fftSize, hzPerDataPoint)

    private val fftData = mutableListOf<Double>()
    private var running = false

    init {
        if (audioRecord.state != AudioRecord.STATE_INITIALIZED)
            throw UnsupportedOperationException("This device doesn't support AudioRecord")
    }

    fun onStart() {
        audioRecord.startRecording()
    }

    fun runThreadLoop() {
        running = true
        thread(start = true) {
            while (running) {
                part1()
            }
        }
    }

    fun stopThreadLoop() {
        running = false
    }

    fun part1() {
        audioRecord.read(audioData.yData, 0, bufferSize)

        val itemsArray = audioData.xData
        for (index in 0 until bufferSize)
            itemsArray[index] = time++

        part2(audioData)
    }

    fun part2(audioData: AudioData) {

        fft.run(audioData.yData, fftData)
//        fftData.setSize(fftSize)
        callback(fftData)
    }

    fun onStop() {
        audioRecord.stop()
    }

    fun getBufferSize(): Int = bufferSize
    fun getSampleRate(): Int = sampleRate
}