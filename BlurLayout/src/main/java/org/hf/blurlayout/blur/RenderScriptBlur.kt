package org.hf.blurlayout.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import org.hf.blurlayout.interfaces.IBlurAlgorithm
import org.hf.blurlayout.interfaces.IBlurController.Companion.DEFAULT_SCALE_FACTOR

/**
 * Copyright (C) 2024 Hansong Technology. All rights reserved.
 * Author: Felix.Huang
 * Created at: 2025/6/20
 * Email: felix.huang@hansonggroup.com
 * Blur using RenderScript, processed on GPU when device drivers support it.
 * Requires API 17+
 *
 * @deprecated because RenderScript is deprecated and its hardware acceleration is not guaranteed.
 * RenderEffectBlur is the best alternative at the moment.
 **/
class RenderScriptBlur(val context: Context) : IBlurAlgorithm {
    private val paint = Paint(Paint.FILTER_BITMAP_FLAG)
    private var renderScript: RenderScript = RenderScript.create(context)
    private var blurScript: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    private var outAllocation: Allocation? = null

    private var lastBitmapWidth = -1
    private var lastBitmapHeight = -1

    private fun canReuseAllocation(bitmap: Bitmap): Boolean {
        return bitmap.height == lastBitmapHeight && bitmap.width == lastBitmapWidth
    }

    /**
     * @param bitmap     bitmap to blur
     * @param blurRadius blur radius (1..25)
     * @return blurred bitmap
     */
    override fun blur(bitmap: Bitmap, blurRadius: Float): Bitmap {
        //Allocation will use the same backing array of pixels as bitmap if created with USAGE_SHARED flag
        val inAllocation = Allocation.createFromBitmap(renderScript, bitmap)

        if (!canReuseAllocation(bitmap)) {
            if (outAllocation != null) {
                outAllocation!!.destroy()
            }
            outAllocation = Allocation.createTyped(renderScript, inAllocation.type)
            lastBitmapWidth = bitmap.width
            lastBitmapHeight = bitmap.height
        }

        blurScript.setRadius(blurRadius)
        blurScript.setInput(inAllocation)
        //do not use inAllocation in forEach. it will cause visual artifacts on blurred Bitmap
        blurScript.forEach(outAllocation)
        outAllocation!!.copyTo(bitmap)

        inAllocation.destroy()
        return bitmap
    }

    override fun destroy() {
        blurScript.destroy()
        renderScript.destroy()
        if (outAllocation != null) {
            outAllocation!!.destroy()
        }
    }

    override fun canModifyBitmap(): Boolean {
        return true
    }

    override fun getSupportedBitmapConfig(): Bitmap.Config {
        return Bitmap.Config.ARGB_8888
    }

    override fun scaleFactor(): Float {
        return DEFAULT_SCALE_FACTOR
    }

    override fun render(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
}