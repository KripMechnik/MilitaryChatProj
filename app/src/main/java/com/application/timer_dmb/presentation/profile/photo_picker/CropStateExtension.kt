package com.application.timer_dmb.presentation.profile.photo_picker

import com.mr0xf00.easycrop.CropState

fun CropState.rotLeft() {
    transform = transform.copy(angleDeg = transform.angleDeg.prev90())
}

fun CropState.rotRight() {
    transform = transform.copy(angleDeg = transform.angleDeg.next90())
}

fun CropState.flipHorizontal() {
    if ((transform.angleDeg / 90) % 2 == 0) flipX() else flipY()
}

fun CropState.flipVertical() {
    if ((transform.angleDeg / 90) % 2 == 0) flipY() else flipX()
}

fun CropState.flipX() {
    transform = transform.copy(scale = transform.scale.copy(x = -1 * transform.scale.x))
}

fun CropState.flipY() {
    transform = transform.copy(scale = transform.scale.copy(y = -1 * transform.scale.y))
}

fun Int.next90() = (this + 90).angleRange()
fun Int.prev90() = (this - 90).angleRange()

fun Int.angleRange(): Int {
    val angle = (this % 360 + 360) % 360
    return if (angle <= 180) angle else angle - 360
}