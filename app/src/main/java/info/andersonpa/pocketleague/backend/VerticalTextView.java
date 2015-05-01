package info.andersonpa.pocketleague.backend;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

// http://stackoverflow.com/questions/1258275/vertical-rotated-label-in-android/7855852#7855852
public class VerticalTextView extends TextView {
     final boolean topDown;

    public VerticalTextView(Context context, AttributeSet attrs, boolean topDown){
        super(context, attrs);
        this.topDown = topDown;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas){
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if(topDown){
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        }else {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
            canvas.translate(2, getWidth()/2 - getTextSize() + 6);
        }

        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());

        getLayout().draw(canvas);
        canvas.restore();
    }
}

