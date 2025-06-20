package org.hf.blurlayout.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import org.hf.blurlayout.R
import org.hf.blurlayout.blur.RenderEffectBlur
import org.hf.blurlayout.blur.RenderScriptBlur
import org.hf.blurlayout.controller.NoOpController
import org.hf.blurlayout.controller.PreDrawBlurController
import org.hf.blurlayout.controller.PreDrawBlurController.Companion.TRANSPARENT
import org.hf.blurlayout.interfaces.IBlurAlgorithm
import org.hf.blurlayout.interfaces.IBlurController
import org.hf.blurlayout.interfaces.IBlurViewFacade

/**
 * Copyright (C) 2024 Hansong Technology. All rights reserved.
 * Author: Felix.Huang
 * Created at: 2025/6/20
 * Email: felix.huang@hansonggroup.com
 * Summary:
 **/
class BlurLayout : FrameLayout {
    val TAG: String = BlurLayout::class.java.simpleName

    private var blurController: IBlurController = NoOpController()

    @ColorInt
    private var overlayColor = 0
    private var blurAutoUpdate = true

    constructor(context: Context):this(context,null)
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs,0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr){
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BlurLayout, defStyleAttr, 0)
        overlayColor = a.getColor(R.styleable.BlurLayout_blurOverlayColor, TRANSPARENT)
        a.recycle()
    }

    override fun draw(canvas: Canvas) {
        val shouldDraw = blurController.draw(canvas)
        if (shouldDraw) {
            super.draw(canvas)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        blurController.updateBlurViewSize()
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                getBackground().getOutline(outline)
                outline.alpha = 1f
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        blurController.setBlurAutoUpdate(false)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!isHardwareAccelerated) {
            Log.e(TAG, "BlurView can't be used in not hardware-accelerated window!")
        } else {
            blurController.setBlurAutoUpdate(this.blurAutoUpdate)
        }
    }

    /**
     * @param rootView  root to start blur from.
     * Can be Activity's root content layout (android.R.id.content)
     * or (preferably) some of your layouts. The lower amount of Views are in the root, the better for performance.
     * @param algorithm sets the blur algorithm
     * @return [BlurLayout] to setup needed params.
     */
    fun setupWith(rootView: ViewGroup, algorithm: IBlurAlgorithm?=null): IBlurViewFacade {
        blurController.destroy()
        val blurController: IBlurController =
            PreDrawBlurController(this, rootView, overlayColor, algorithm ?: getBlurAlgorithm())
        this.blurController = blurController
        return blurController
    }

    /**
     * @see IBlurViewFacade.setBlurRadius
     */
    fun setBlurRadius(radius: Float): IBlurViewFacade {
        return blurController.setBlurRadius(radius)
    }

    /**
     * @see IBlurViewFacade.setOverlayColor
     */
    fun setOverlayColor(@ColorInt overlayColor: Int): IBlurViewFacade {
        this.overlayColor = overlayColor
        return blurController.setOverlayColor(overlayColor)
    }

    /**
     * @see IBlurViewFacade.setBlurAutoUpdate
     */
    fun setBlurAutoUpdate(enabled: Boolean): IBlurViewFacade {
        this.blurAutoUpdate = enabled
        return blurController.setBlurAutoUpdate(enabled)
    }

    /**
     * @see IBlurViewFacade.setBlurEnabled
     */
    fun setBlurEnabled(enabled: Boolean): IBlurViewFacade {
        return blurController.setBlurEnabled(enabled)
    }

    private fun getBlurAlgorithm(): IBlurAlgorithm {
        val algorithm: IBlurAlgorithm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            RenderEffectBlur()
        } else {
            RenderScriptBlur(context)
        }
        return algorithm
    }
}