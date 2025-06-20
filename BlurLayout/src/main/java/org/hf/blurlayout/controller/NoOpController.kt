package org.hf.blurlayout.controller

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import org.hf.blurlayout.interfaces.IBlurController

/**
 * Copyright (C) 2024 Hansong Technology. All rights reserved.
 * Author: Felix.Huang
 * Created at: 2025/6/20
 * Email: felix.huang@hansonggroup.com
 * Summary:
 **/
class NoOpController : IBlurController {
    override fun draw(canvas: Canvas) = true

    override fun updateBlurViewSize() {}

    override fun destroy() {}

    override fun setBlurRadius(radius: Float) = this

    override fun setOverlayColor(overlayColor: Int) = this

    override fun setFrameClearDrawable(frameClearDrawable: Drawable?) = this

    override fun setBlurEnabled(enabled: Boolean) = this

    override fun setBlurAutoUpdate(enabled: Boolean) = this
}