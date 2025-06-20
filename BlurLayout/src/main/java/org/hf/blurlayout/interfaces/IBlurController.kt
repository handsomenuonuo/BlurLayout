package org.hf.blurlayout.interfaces

import android.graphics.Canvas

/**
 * Copyright (C) 2024 Hansong Technology. All rights reserved.
 * Author: Felix.Huang
 * Created at: 2025/6/20
 * Email: felix.huang@hansonggroup.com
 * Summary:
 **/
interface IBlurController : IBlurViewFacade {

    companion object {
        const val DEFAULT_SCALE_FACTOR: Float = 10f
        const val DEFAULT_BLUR_RADIUS: Float = 2f
    }

    /**
     * Draws blurred content on given canvas
     *
     * @return true if BlurView should proceed with drawing itself and its children
     */
    fun draw(canvas: Canvas): Boolean

    /**
     * Must be used to notify Controller when BlurView's size has changed
     */
    fun updateBlurViewSize()

    /**
     * Frees allocated resources
     */
    fun destroy()
}