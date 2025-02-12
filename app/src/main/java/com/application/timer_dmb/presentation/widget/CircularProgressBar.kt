package com.application.timer_dmb.presentation.widget

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.application.timer_dmb.R
import kotlin.math.min

class CircularProgressBar(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    companion object {
        private const val DEFAULT_MAX_VALUE = 100f
        private const val DEFAULT_START_ANGLE = 90f
        private const val DEFAULT_ANIMATION_DURATION = 1500L
    }

    override fun isInEditMode(): Boolean {
        return true
    }

    // Properties
    private var progressAnimator: ValueAnimator? = null
    private var indeterminateModeHandler: Handler? = null

    // View
    private var rectF = RectF()
    private var backgroundPaint: Paint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private var foregroundPaint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val glowPaint = Paint().apply {
        style = Paint.Style.STROKE
        maskFilter = BlurMaskFilter(100f, BlurMaskFilter.Blur.NORMAL)
    }

    private val defaultPaint = Paint().apply {
        color = Color.WHITE // Цвет текста
        textSize = 90f // Размер текста
        textAlign = Paint.Align.LEFT // Выравнивание текста по центру
        isAntiAlias = true // Сглаживание текста
        typeface = ResourcesCompat.getFont(context, R.font.manrope_bold)
    }

    // Кисть для текста "300"
    private val largePaint = Paint().apply {
        color = Color.WHITE // Цвет текста
        textSize = 200f // Размер текста (в 2 раза больше)
        textAlign = Paint.Align.LEFT // Выравнивание текста по центру
        isAntiAlias = true // Сглаживание текста
        typeface = ResourcesCompat.getFont(context, R.font.manrope_bold)
    }

    //private val rs: RenderScript = RenderScript.create(context)

    //region Attributes
    var progress: Float = 0f
        set(value) {
            field = if (progress <= progressMax) value else progressMax
            onProgressChangeListener?.invoke(progress)
            invalidate()
        }
    var daysLeft: String = ""
        set(value) {
            field = value
            invalidate()
        }
    var progressMax: Float = DEFAULT_MAX_VALUE
        set(value) {
            field = if (field >= 0) value else DEFAULT_MAX_VALUE
            invalidate()
        }
    var progressBarWidth: Float = resources.getDimension(R.dimen.default_stroke_width)
        set(value) {
            field = value.dpToPx()
            foregroundPaint.strokeWidth = field
            glowPaint.strokeWidth = field
            requestLayout()
            invalidate()
        }
    var backgroundProgressBarWidth: Float = resources.getDimension(R.dimen.default_background_stroke_width)
        set(value) {
            field = value.dpToPx()
            backgroundPaint.strokeWidth = field
            requestLayout()
            invalidate()
        }
    var progressBarColor: Int = Color.BLACK
        set(value) {
            field = value
            manageColor()
            invalidate()
        }
    var progressBarColorStart: Int? = null
        set(value) {
            field = value
            manageColor()
            invalidate()
        }
    var progressBarColorEnd: Int? = null
        set(value) {
            field = value
            manageColor()
            invalidate()
        }
    var progressBarColorDirection: GradientDirection = GradientDirection.LEFT_TO_RIGHT
        set(value) {
            field = value
            manageColor()
            invalidate()
        }
    var backgroundProgressBarColor: Int = Color.GRAY
        set(value) {
            field = value
            manageBackgroundProgressBarColor()
            invalidate()
        }
    var backgroundProgressBarColorStart: Int? = null
        set(value) {
            field = value
            manageBackgroundProgressBarColor()
            invalidate()
        }
    var backgroundProgressBarColorEnd: Int? = null
        set(value) {
            field = value
            manageBackgroundProgressBarColor()
            invalidate()
        }
    var backgroundProgressBarColorDirection: GradientDirection = GradientDirection.LEFT_TO_RIGHT
        set(value) {
            field = value
            manageBackgroundProgressBarColor()
            invalidate()
        }
    var roundBorder = false
        set(value) {
            field = value
            foregroundPaint.strokeCap = if (field) Paint.Cap.ROUND else Paint.Cap.BUTT
            invalidate()
        }
    var startAngle: Float = DEFAULT_START_ANGLE
        set(value) {
            var angle = value + DEFAULT_START_ANGLE
            while (angle > 360) {
                angle -= 360
            }
            field = if (angle < 0) 0f else if (angle > 360) 360f else angle
            invalidate()
        }
    var progressDirection: ProgressDirection = ProgressDirection.TO_RIGHT
        set(value) {
            field = value
            invalidate()
        }
    var indeterminateMode = false
        set(value) {
            field = value
            onIndeterminateModeChangeListener?.invoke(field)
            progressIndeterminateMode = 0f
            progressDirectionIndeterminateMode = ProgressDirection.TO_RIGHT
            startAngleIndeterminateMode = DEFAULT_START_ANGLE

            indeterminateModeHandler?.removeCallbacks(indeterminateModeRunnable)
            progressAnimator?.cancel()
            indeterminateModeHandler = Handler()

            if (field) {
                indeterminateModeHandler?.post(indeterminateModeRunnable)
            }
        }
    var onProgressChangeListener: ((Float) -> Unit)? = null
    var onIndeterminateModeChangeListener: ((Boolean) -> Unit)? = null
    //endregion

    //region Indeterminate Mode
    private var progressIndeterminateMode: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var progressDirectionIndeterminateMode: ProgressDirection = ProgressDirection.TO_RIGHT
        set(value) {
            field = value
            invalidate()
        }
    private var startAngleIndeterminateMode: Float = DEFAULT_START_ANGLE
        set(value) {
            field = value
            invalidate()
        }

    private val indeterminateModeRunnable = Runnable {
        if (indeterminateMode) {
            postIndeterminateModeHandler()
            // whatever you want to do below
            this@CircularProgressBar.progressDirectionIndeterminateMode = this@CircularProgressBar.progressDirectionIndeterminateMode.reverse()
            if (this@CircularProgressBar.progressDirectionIndeterminateMode.isToRight()) {
                setProgressWithAnimation(0f, 1500)
            } else {
                setProgressWithAnimation(progressMax, 1500)
            }
        }
    }

    private fun postIndeterminateModeHandler() {
        indeterminateModeHandler?.postDelayed(indeterminateModeRunnable, DEFAULT_ANIMATION_DURATION)
    }
    //endregion

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // Load the styled attributes and set their properties
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, 0, 0)

        // Value
        progress = attributes.getFloat(R.styleable.CircularProgressBar_cpb_progress, progress)
        progressMax = attributes.getFloat(R.styleable.CircularProgressBar_cpb_progress_max, progressMax)

        // StrokeWidth
        progressBarWidth = attributes.getDimension(R.styleable.CircularProgressBar_cpb_progressbar_width, progressBarWidth).pxToDp()
        backgroundProgressBarWidth = attributes.getDimension(R.styleable.CircularProgressBar_cpb_background_progressbar_width, backgroundProgressBarWidth).pxToDp()

        // Color
        progressBarColor = attributes.getInt(R.styleable.CircularProgressBar_cpb_progressbar_color, progressBarColor)
        attributes.getColor(R.styleable.CircularProgressBar_cpb_progressbar_color_start, 0)
            .also { if (it != 0) progressBarColorStart = it }
        attributes.getColor(R.styleable.CircularProgressBar_cpb_progressbar_color_end, 0)
            .also { if (it != 0) progressBarColorEnd = it }
        progressBarColorDirection = attributes.getInteger(R.styleable.CircularProgressBar_cpb_progressbar_color_direction, progressBarColorDirection.value).toGradientDirection()
        backgroundProgressBarColor = attributes.getInt(R.styleable.CircularProgressBar_cpb_background_progressbar_color, backgroundProgressBarColor)
        attributes.getColor(R.styleable.CircularProgressBar_cpb_background_progressbar_color_start, 0)
            .also { if (it != 0) backgroundProgressBarColorStart = it }
        attributes.getColor(R.styleable.CircularProgressBar_cpb_background_progressbar_color_end, 0)
            .also { if (it != 0) backgroundProgressBarColorEnd = it }
        backgroundProgressBarColorDirection = attributes.getInteger(R.styleable.CircularProgressBar_cpb_background_progressbar_color_direction, backgroundProgressBarColorDirection.value).toGradientDirection()

        // Progress Direction
        progressDirection = attributes.getInteger(R.styleable.CircularProgressBar_cpb_progress_direction, progressDirection.value).toProgressDirection()

        // Round Border
        roundBorder = attributes.getBoolean(R.styleable.CircularProgressBar_cpb_round_border, roundBorder)

        // Angle
        startAngle = attributes.getFloat(R.styleable.CircularProgressBar_cpb_start_angle, 0f)

        // Indeterminate Mode
        indeterminateMode = attributes.getBoolean(R.styleable.CircularProgressBar_cpb_indeterminate_mode, indeterminateMode)

        attributes.recycle()
    }



    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressAnimator?.cancel()
        indeterminateModeHandler?.removeCallbacks(indeterminateModeRunnable)
    }

    //region Draw Method
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        manageColor()
        manageBackgroundProgressBarColor()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        canvas.drawArc(rectF, -80f, 70f, false, backgroundPaint)
        canvas.drawArc(rectF, 10f, 70f, false, backgroundPaint)
        canvas.drawArc(rectF, 100f, 70f, false, backgroundPaint)
        canvas.drawArc(rectF, 190f, 70f, false, backgroundPaint)

        val realProgress = (if (indeterminateMode) progressIndeterminateMode else progress) * DEFAULT_MAX_VALUE / progressMax

        val isToRightFromIndeterminateMode = indeterminateMode && progressDirectionIndeterminateMode.isToRight()
        val isToRightFromNormalMode = !indeterminateMode && progressDirection.isToRight()
        val angle = (if (isToRightFromIndeterminateMode || isToRightFromNormalMode) 360 else -360) * realProgress / 100



        if (angle <= 90 && angle > 10){
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle + 10f, if (angle - 20f < 0) 0f else angle - 20f, false, foregroundPaint)

            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle + 10f, if (angle - 20f < 0) 0f else angle - 20f, false, glowPaint)
        } else if (angle <= 180 && angle > 10){
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle + 10f, 70f, false, foregroundPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 190f,  if (angle - 110f < 0) 0f else angle - 110f, false, foregroundPaint)

            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle + 10f, 70f, false, glowPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 190f,  if (angle - 110f < 0) 0f else angle - 110f, false, glowPaint)
        } else if (angle <= 270 && angle > 10) {
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle + 10f, 70f, false, foregroundPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 190f, 70f, false, foregroundPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 280f, if (angle - 200f < 0) 0f else angle - 200f, false, foregroundPaint)

            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle + 10f, 70f, false, glowPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 190f, 70f, false, glowPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 280f, if (angle - 200f < 0) 0f else angle - 200f, false, glowPaint)
        } else if (angle <= 360 && angle > 10) {
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle + 10f, 70f, false, foregroundPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 190f, 70f, false, foregroundPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 280f, 70f, false, foregroundPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 370f, if (angle - 290f < 0) 0f else angle - 290f, false, foregroundPaint)

            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else startAngle + 10f, 70f, false, glowPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 190f, 70f, false, glowPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 280f, 70f, false, glowPaint)
            canvas.drawArc(rectF, if (indeterminateMode) startAngleIndeterminateMode else 370f, if (angle - 290f < 0) 0f else angle - 290f, false, glowPaint)
        }

        val centerX = width / 2f // Центр по горизонтали
        val centerY = height / 2f // Центр по вертикали

        // Вычисляем высоту текста "осталось"
        val text1Height = defaultPaint.descent() - defaultPaint.ascent()

        val text2height = largePaint.descent() - largePaint.ascent()

        val text1width = defaultPaint.measureText("осталось")

        // Рисуем "осталось"
        canvas.drawText("осталось", centerX - text1width/2, centerY - text1Height + 20, defaultPaint)

        // Вычисляем ширину текста "300"
        val text2Width = largePaint.measureText(daysLeft)

        val fullWidth = text2Width + defaultPaint.measureText(" дней")

        canvas.drawText(daysLeft, centerX - fullWidth / 2, centerY + text2height/2 - 40, largePaint) // Рисуем "300"
        canvas.drawText(
            "дней",
            centerX - fullWidth / 2 + text2Width, // Смещение для текста "дней"
            centerY + text2height/2 - 40,
            defaultPaint
        ) // Рисуем "дней"


    }

    override fun setBackgroundColor(backgroundColor: Int) {
        backgroundProgressBarColor = backgroundColor
    }

    private fun manageColor() {
        foregroundPaint.shader = createLinearGradient(progressBarColorStart ?: progressBarColor,
            progressBarColorEnd ?: progressBarColor, progressBarColorDirection)
        glowPaint.shader = createLinearGradient(progressBarColorStart ?: progressBarColor,
            progressBarColorEnd ?: progressBarColor, progressBarColorDirection)
    }

    private fun manageBackgroundProgressBarColor() {
        backgroundPaint.shader = createLinearGradient(
            backgroundProgressBarColorStart ?: backgroundProgressBarColor,
            backgroundProgressBarColorEnd ?: backgroundProgressBarColor,
            backgroundProgressBarColorDirection)
    }

    private fun createLinearGradient(startColor: Int, endColor: Int, gradientDirection: GradientDirection): LinearGradient {
        var x0 = 0f
        var y0 = 0f
        var x1 = 0f
        var y1 = 0f
        when (gradientDirection) {
            GradientDirection.LEFT_TO_RIGHT -> x1 = width.toFloat()
            GradientDirection.RIGHT_TO_LEFT -> x0 = width.toFloat()
            GradientDirection.TOP_TO_BOTTOM -> y1 = height.toFloat()
            GradientDirection.BOTTOM_TO_END -> y0 = height.toFloat()
        }
        return LinearGradient(x0, y0, x1, y1, startColor, endColor, Shader.TileMode.CLAMP)
    }
    //endregion

    //region Measure Method
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = min(width, height)
        setMeasuredDimension(min, min)
        val highStroke = (if (progressBarWidth > backgroundProgressBarWidth) progressBarWidth else backgroundProgressBarWidth) + 100
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2, min - highStroke / 2)
    }
    //endregion

    /**
     * Set the progress with animation.
     *
     * @param progress [Float] The progress it should animate to it.
     * @param duration [Long] optional, null by default.
     * @param interpolator [TimeInterpolator] optional, null by default.
     * @param startDelay [Long] optional, null by default.
     */
    @JvmOverloads
    fun setProgressWithAnimation(progress: Float,
                                 duration: Long? = null,
                                 interpolator: TimeInterpolator? = null,
                                 startDelay: Long? = null) {
        progressAnimator?.cancel()
        progressAnimator = ValueAnimator.ofFloat(if (indeterminateMode) progressIndeterminateMode else this.progress, progress)
        duration?.also { progressAnimator?.duration = it }
        interpolator?.also { progressAnimator?.interpolator = it }
        startDelay?.also { progressAnimator?.startDelay = it }
        progressAnimator?.addUpdateListener { animation ->
            (animation.animatedValue as? Float)?.also { value ->
                if (indeterminateMode) progressIndeterminateMode = value else this.progress = value
                if (indeterminateMode) {
                    val updateAngle = value * 360 / 100
                    startAngleIndeterminateMode = DEFAULT_START_ANGLE +
                            if (progressDirectionIndeterminateMode.isToRight()) updateAngle else -updateAngle
                }
            }
        }
        progressAnimator?.start()
    }

    //region Extensions Utils
    private fun Float.dpToPx(): Float =
        this * Resources.getSystem().displayMetrics.density

    private fun Float.pxToDp(): Float =
        this / Resources.getSystem().displayMetrics.density

    private fun Int.toProgressDirection(): ProgressDirection =
        when (this) {
            1 -> ProgressDirection.TO_RIGHT
            2 -> ProgressDirection.TO_LEFT
            else -> throw IllegalArgumentException("This value is not supported for ProgressDirection: $this")
        }

    private fun ProgressDirection.reverse(): ProgressDirection =
        if (this.isToRight()) ProgressDirection.TO_LEFT else ProgressDirection.TO_RIGHT

    private fun ProgressDirection.isToRight(): Boolean = this == ProgressDirection.TO_RIGHT

    private fun Int.toGradientDirection(): GradientDirection =
        when (this) {
            1 -> GradientDirection.LEFT_TO_RIGHT
            2 -> GradientDirection.RIGHT_TO_LEFT
            3 -> GradientDirection.TOP_TO_BOTTOM
            4 -> GradientDirection.BOTTOM_TO_END
            else -> throw IllegalArgumentException("This value is not supported for GradientDirection: $this")
        }
    //endregion

    /**
     * ProgressDirection enum class to set the direction of the progress in progressBar
     */
    enum class ProgressDirection(val value: Int) {
        TO_RIGHT(1),
        TO_LEFT(2)
    }

    /**
     * GradientDirection enum class to set the direction of the gradient progressBarColor
     */
    enum class GradientDirection(val value: Int) {
        LEFT_TO_RIGHT(1),
        RIGHT_TO_LEFT(2),
        TOP_TO_BOTTOM(3),
        BOTTOM_TO_END(4)
    }

}