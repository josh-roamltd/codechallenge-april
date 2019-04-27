package joshvdh.com.codechallengeapril.audiofft

data class AudioData(val pointsCount: Int) {
    val xData = LongArray(pointsCount)
    val yData = ShortArray(pointsCount)
}