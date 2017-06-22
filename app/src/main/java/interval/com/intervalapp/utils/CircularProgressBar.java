package interval.com.intervalapp.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Mikhael LOPEZ on 16/10/2015.
 */

public class CircularProgressBar extends View {

    private ObjectAnimator objectAnimator;

    private float progress = 0;

    private float strokeWidth = getResources().getDimension(com.mikhaellopez.circularprogressbar.R.dimen.default_stroke_width);

    private float backgroundStrokeWidth = getResources().getDimension(com.mikhaellopez.circularprogressbar.R.dimen.default_background_stroke_width);

    private int color = Color.BLACK;

    private int backgroundColor = Color.GRAY;

    private RectF rectF;

    private Paint backgroundPaint;

    private Paint foregroundPaint;

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        rectF = new RectF();
        TypedArray typedArray = context.getTheme()
                .obtainStyledAttributes(attrs, com.mikhaellopez.circularprogressbar
                        .R.styleable.CircularProgressBar, 0, 0);
        try {
            progress = typedArray.getFloat(com.mikhaellopez.circularprogressbar
                    .R.styleable.CircularProgressBar_cpb_progress, progress);
            strokeWidth = typedArray.getDimension(com.mikhaellopez.circularprogressbar
                    .R.styleable.CircularProgressBar_cpb_progressbar_width, strokeWidth);
            backgroundStrokeWidth = typedArray.getDimension(com.mikhaellopez.circularprogressbar
                    .R.styleable.CircularProgressBar_cpb_background_progressbar_width,
                    backgroundStrokeWidth);
            color = typedArray.getInt(com.mikhaellopez.circularprogressbar
                    .R.styleable.CircularProgressBar_cpb_progressbar_color, color);
            backgroundColor = typedArray.getInt(com.mikhaellopez.circularprogressbar
                    .R.styleable.CircularProgressBar_cpb_background_progressbar_color,
                    backgroundColor);
        } finally {
            typedArray.recycle();
        }

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);

        foregroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        foregroundPaint.setColor(color);
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(rectF, backgroundPaint);
        float angle = 360 * progress / 100;
        int startAngle = -90;
        canvas.drawArc(rectF, startAngle, angle, false, foregroundPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        float highStroke = (strokeWidth > backgroundStrokeWidth) ? strokeWidth
                : backgroundStrokeWidth;
        rectF.set(0 + highStroke / 2, 0 + highStroke / 2, min - highStroke / 2,
                min - highStroke / 2);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = (progress <= 100) ? progress : 100;
        invalidate();
    }

    public float getProgressBarWidth() {
        return strokeWidth;
    }

    public void setProgressBarWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        foregroundPaint.setStrokeWidth(strokeWidth);
        requestLayout();
        invalidate();
    }

    public float getBackgroundProgressBarWidth() {
        return backgroundStrokeWidth;
    }

    public void setBackgroundProgressBarWidth(float backgroundStrokeWidth) {
        this.backgroundStrokeWidth = backgroundStrokeWidth;
        backgroundPaint.setStrokeWidth(backgroundStrokeWidth);
        requestLayout();
        invalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        foregroundPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
        requestLayout();
    }

    /**
     * Set the progress with an animation.
     * Note that the {@link ObjectAnimator} Class automatically set the progress
     * so don't call the
     * {@link com.mikhaellopez.circularprogressbar.CircularProgressBar#setProgress(float)}
     * directly within this method.
     *
     * @param progress The progress it should animate to it.
     */
    public void setProgressWithAnimation(float progress) {
        setProgressWithAnimation(progress, 1500);
    }

    /**
     * Set the progress with an animation.
     * Note that the {@link ObjectAnimator} Class automatically set the progress
     * so don't call the
     * {@link com.mikhaellopez.circularprogressbar.CircularProgressBar#setProgress(float)}
     * directly within this method.
     *
     * @param progress The progress it should animate to it.
     * @param duration The length of the animation, in milliseconds.
     */
    public void setProgressWithAnimation(float progress, int duration) {
        objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }

    public void stopAnimation(){
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }
}
