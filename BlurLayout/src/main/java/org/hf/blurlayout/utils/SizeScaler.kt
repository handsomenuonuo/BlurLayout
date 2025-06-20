package org.hf.blurlayout.utils

import kotlin.math.ceil

/**
 * Copyright (C) 2024 Hansong Technology. All rights reserved.
 * Author: Felix.Huang
 * Created at: 2025/6/20
 * Email: felix.huang@hansonggroup.com
 * Summary:
 **/
class SizeScaler(val scaleFactor: Float = 0f) {
    // Bitmap size should be divisible by ROUNDING_VALUE to meet stride requirement.
    // This will help avoiding an extra bitmap allocation when passing the bitmap to RenderScript for blur.
    // Usually it's 16, but on Samsung devices it's 64 for some reason.
    companion object {
        const val ROUNDING_VALUE: Int = 64
    }

    fun scale(width: Int, height: Int): Size {
        val nonRoundedScaledWidth = downscaleSize(width.toFloat())
        val scaledWidth = roundSize(nonRoundedScaledWidth)
        //Only width has to be aligned to ROUNDING_VALUE
        val roundingScaleFactor = width.toFloat() / scaledWidth
        //Ceiling because rounding or flooring might leave empty space on the View's bottom
        val scaledHeight = ceil((height / roundingScaleFactor).toDouble()).toInt()

        return Size(scaledWidth, scaledHeight, roundingScaleFactor)
    }

    fun isZeroSized(measuredWidth: Int, measuredHeight: Int): Boolean {
        return downscaleSize(measuredHeight.toFloat()) == 0 || downscaleSize(measuredWidth.toFloat()) == 0
    }

    /**
     * Rounds a value to the nearest divisible by [.ROUNDING_VALUE] to meet stride requirement
     */
    private fun roundSize(value: Int): Int {
        if (value % ROUNDING_VALUE == 0) {
            return value
        }
        return value - (value % ROUNDING_VALUE) + ROUNDING_VALUE
    }

    private fun downscaleSize(value: Float): Int {
        return ceil((value / scaleFactor).toDouble()).toInt()
    }


    inner class Size internal constructor(
        val width: Int, val height: Int,
        val scaleFactor: Float,
    ) {
        override fun equals(o: Any?): Boolean {
            if (this === o) return true
            if (o == null || javaClass != o.javaClass) return false

            val size = o as Size

            if (width != size.width) return false
            if (height != size.height) return false
            return java.lang.Float.compare(size.scaleFactor, scaleFactor) == 0
        }

        override fun hashCode(): Int {
            var result = width
            result = 31 * result + height
            result = 31 * result + (if (scaleFactor != +0.0f) java.lang.Float.floatToIntBits(
                scaleFactor
            ) else 0)
            return result
        }

        override fun toString(): String {
            return "Size{" +
                    "width=" + width +
                    ", height=" + height +
                    ", scaleFactor=" + scaleFactor +
                    '}'
        }
    }
}