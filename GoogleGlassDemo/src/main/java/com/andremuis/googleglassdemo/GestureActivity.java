package com.andremuis.googleglassdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class GestureActivity extends Activity
{
    private static final int LIGHT_BLUE_COLOR = Color.parseColor("#44757D");
    private static final int BLUE_COLOR = Color.parseColor("#88eafa");

    private TextView mTapTextView;
    private TextView mTwoTapTextView;
    private TextView mThreeTapTextView;
    private TextView mLongPressTextView;
    private TextView mTwoLongPressTextView;
    private TextView mThreeLongPressTextView;
    private TextView mSwipeLeftTextView;
    private TextView mTwoSwipeLeftTextView;
    private TextView mSwipeRightTextView;
    private TextView mTwoSwipeRightTextView;
    private TextView mSwipeUpTextView;
    private TextView mTwoSwipeUpTextView;

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gesture);

        mTapTextView = (TextView)findViewById(R.id.tap_textView);
        mTwoTapTextView = (TextView)findViewById(R.id.two_tap_textView);
        mThreeTapTextView = (TextView)findViewById(R.id.three_tap_textView);
        mLongPressTextView = (TextView)findViewById(R.id.long_press_textView);
        mTwoLongPressTextView = (TextView)findViewById(R.id.two_long_press_textView);
        mThreeLongPressTextView = (TextView)findViewById(R.id.three_long_press_textView);
        mSwipeLeftTextView = (TextView)findViewById(R.id.swipe_left_textView);
        mTwoSwipeLeftTextView = (TextView)findViewById(R.id.two_swipe_left_textView);
        mSwipeRightTextView = (TextView)findViewById(R.id.swipe_right_textView);
        mTwoSwipeRightTextView = (TextView)findViewById(R.id.two_swipe_right_textView);
        mSwipeUpTextView = (TextView)findViewById(R.id.swipe_up_textView);
        mTwoSwipeUpTextView = (TextView)findViewById(R.id.two_swipe_up_textView);

        mGestureDetector = createGestureDetector(this);
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);

        gestureDetector.setBaseListener( new GestureDetector.BaseListener()
        {
            @Override
            public boolean onGesture(Gesture gesture)
            {
            resetDisplay();

            switch (gesture)
            {
                case TAP:
                    mTapTextView.setTextColor(BLUE_COLOR);
                    break;
                case TWO_TAP:
                    mTwoTapTextView.setTextColor(BLUE_COLOR);
                    break;
                case THREE_TAP:
                    mThreeTapTextView.setTextColor(BLUE_COLOR);
                    break;
                case LONG_PRESS:
                    mLongPressTextView.setTextColor(BLUE_COLOR);
                    break;
                case TWO_LONG_PRESS:
                    mTwoLongPressTextView.setTextColor(BLUE_COLOR);
                    break;
                case THREE_LONG_PRESS:
                    mThreeLongPressTextView.setTextColor(BLUE_COLOR);
                    break;
                case SWIPE_LEFT:
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();

                    mSwipeLeftTextView.setTextColor(BLUE_COLOR);
                    break;
                case TWO_SWIPE_LEFT:
                    mTwoSwipeLeftTextView.setTextColor(BLUE_COLOR);
                    break;
                case SWIPE_RIGHT:
                    startActivity(new Intent(getApplicationContext(), SensorActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();

                    mSwipeRightTextView.setTextColor(BLUE_COLOR);
                    break;
                case TWO_SWIPE_RIGHT:
                    mTwoSwipeRightTextView.setTextColor(BLUE_COLOR);
                    break;
                case SWIPE_UP:
                    mSwipeUpTextView.setTextColor(BLUE_COLOR);
                    break;
                case TWO_SWIPE_UP:
                    mTwoSwipeUpTextView.setTextColor(BLUE_COLOR);
                    break;
                default:
                    break;
            }

            return true;
            }
        });

        return gestureDetector;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        if (mGestureDetector != null)
        {
            return mGestureDetector.onMotionEvent(event);
        }

        return super.onGenericMotionEvent(event);
    }

    protected void resetDisplay()
    {
        mTapTextView.setTextColor(LIGHT_BLUE_COLOR);
        mTwoTapTextView.setTextColor(LIGHT_BLUE_COLOR);
        mThreeTapTextView.setTextColor(LIGHT_BLUE_COLOR);
        mLongPressTextView.setTextColor(LIGHT_BLUE_COLOR);
        mTwoLongPressTextView.setTextColor(LIGHT_BLUE_COLOR);
        mThreeLongPressTextView.setTextColor(LIGHT_BLUE_COLOR);
        mSwipeLeftTextView.setTextColor(LIGHT_BLUE_COLOR);
        mTwoSwipeLeftTextView.setTextColor(LIGHT_BLUE_COLOR);
        mSwipeRightTextView.setTextColor(LIGHT_BLUE_COLOR);
        mTwoSwipeRightTextView.setTextColor(LIGHT_BLUE_COLOR);
        mSwipeUpTextView.setTextColor(LIGHT_BLUE_COLOR);
        mTwoSwipeUpTextView.setTextColor(LIGHT_BLUE_COLOR);
    }
}
