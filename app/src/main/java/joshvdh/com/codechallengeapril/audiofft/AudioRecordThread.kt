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
//    private val audioTrack = AudioTrack(
//        AudioAttributes.Builder()
//            .setUsage(AudioAttributes.USAGE_MEDIA)
//            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//            .build(),
//        AudioFormat.Builder()
//            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
//            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
//            .setSampleRate(sampleRate)
//            .build(),
//        bufferSize * 2,
//        AudioTrack.MODE_STREAM,
//        AudioManager.AUDIO_SESSION_ID_GENERATE
//    )

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
                processAudioStream()
            }
        }
    }

    fun stopThreadLoop() {
        running = false
    }

    private fun processAudioStream() {
        audioRecord.read(audioData.yData, 0, bufferSize)

        writeData(audioData)

        //Write to file
        val itemsArray = audioData.xData
        for (index in 0 until bufferSize)
            itemsArray[index] = time++

        fft.run(audioData.yData, fftData)
        callback(fftData)
    }

    private fun writeData(audioData: AudioData) {
//        audioTrack.write(audioData.yData, audioData.yData.size, AudioTrack.WRITE_BLOCKING)
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