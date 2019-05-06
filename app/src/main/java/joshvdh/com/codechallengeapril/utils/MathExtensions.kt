package joshvdh.com.codechallengeapril.utils

fun Double.lerpTo(target: Double, progress: Double): Double = (((target - this) * progress) + this)