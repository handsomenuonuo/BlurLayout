package org.hf.blurlayout.interfaces

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

/**
 * Copyright (C) 2024 Hansong Technology. All rights reserved.
 * Author: Felix.Huang
 * Created at: 2025/6/20
 * Email: felix.huang@hansonggroup.com
 * Summary:
 **/
interface IBlurViewFacade {
    /**
     * Enables/disables the blur. Enabled by default
     *
     * @param enabled true to enable, false otherwise
     * @return [IBlurViewFacade]
     */
    fun setBlurEnabled(enabled: Boolean): IBlurViewFacade

    /**
     * Can be used to stop blur auto update or resume if it was stopped before.
     * Enabled by default.
     *
     * @return [IBlurViewFacade]
     */
    fun setBlurAutoUpdate(enabled: Boolean): IBlurViewFacade

    /**
     * @param frameClearDrawable sets the drawable to draw before view hierarchy.
     * Can be used to draw Activity's window background if your root layout doesn't provide any background
     * Optional, by default frame is cleared with a transparent color.
     * @return [IBlurViewFacade]
     */
    fun setFrameClearDrawable(frameClearDrawable: Drawable?): IBlurViewFacade

    /**
     * @param radius sets the blur radius
     * Default value is [IBlurController.DEFAULT_BLUR_RADIUS]
     * @return [IBlurViewFacade]
     */
    fun setBlurRadius(radius: Float): IBlurViewFacade

    /**
     * Sets the color overlay to be drawn on top of blurred content
     *
     * @param overlayColor int color
     * @return [IBlurViewFacade]
     */
    fun setOverlayColor(@ColorInt overlayColor: Int): IBlurViewFacade
}