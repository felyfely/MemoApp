package leonproject.com.MemoColor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by fudan on 3/23/15.
 */
public class SquareImageView extends ImageView {
//    public SquareImageView(Context context) {
//        super(context);
//    }
//
//    public SquareImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        int width = getMeasuredWidth();
//        setMeasuredDimension(width, width);
//    }



    private double mHeightRatio;

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context) {
        super(context);
    }

    public void setHeightRatio(double ratio) {
        if (ratio != mHeightRatio) {
            mHeightRatio = ratio;
            requestLayout();
        }
    }

    public double getHeightRatio() {
        return mHeightRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * mHeightRatio);
            setMeasuredDimension(width, height);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
