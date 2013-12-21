package com.andremuis.googleglassdemo;

import android.app.Activity;
import android.graphics.Color;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.lang.Math;

/**
 * Created by amuis on 12/15/13.
 */
public class SensorActivity extends Activity implements SensorEventListener
{
    private static final double STANDARD_GRAVITY = 9.8;

    private static final double STRAIGHT_AHEAD_HEAD_TILT_ANGLE = 6;
    private static final double HEAD_TILT_ANGLE_THRESHOLD = 12;

    private static final double HEAD_COCKED_ANGLE_THRESHOLD = 20;

    private static final double UP_DOWN_ANGULAR_VELOCITY_THRESHOLD = 0.4;
    private static final double LEFT_RIGHT_ANGULAR_VELOCITY_THRESHOLD = 0.5;

    private static final int LIGHT_BLUE_COLOR = Color.parseColor("#44757D");
    private static final int BLUE_COLOR = Color.parseColor("#88eafa");

    private TextView mLookingUpTextView;
    private TextView mLookingStraightAheadTextView;
    private TextView mLookingDownTextView;

    private TextView mHeadCockedLeftTextView;
    private TextView mHeadUprightTextView;
    private TextView mHeadCockedRightTextView;

    private TextView mRotatingHeadUpTextView;
    private TextView mRotatingHeadDownTextView;
    private TextView mRotatingHeadLeftTextView;
    private TextView mRotatingHeadRightTextView;

    private TextView mLightLevelTextView;

    private GestureDetector mGestureDetector;

    private SensorManager mSensorManager;
    private Sensor mGravitySensor;
    private Sensor mGyroscope;
    private Sensor mLightSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sensor);

        mLookingUpTextView = (TextView)findViewById(R.id.looking_up_textView);
        mLookingStraightAheadTextView = (TextView)findViewById(R.id.looking_straight_ahead_textView);
        mLookingDownTextView = (TextView)findViewById(R.id.looking_down_textView);

        mHeadCockedLeftTextView = (TextView)findViewById(R.id.head_cocked_left_textView);
        mHeadUprightTextView = (TextView)findViewById(R.id.head_upright_textView);
        mHeadCockedRightTextView = (TextView)findViewById(R.id.head_cocked_right_textView);

        mRotatingHeadUpTextView = (TextView)findViewById(R.id.rotating_head_up_textView);
        mRotatingHeadDownTextView = (TextView)findViewById(R.id.rotating_head_down_textView);
        mRotatingHeadLeftTextView = (TextView)findViewById(R.id.rotating_head_left_textView);
        mRotatingHeadRightTextView = (TextView)findViewById(R.id.rotating_head_right_textView);

        mLightLevelTextView = (TextView)findViewById(R.id.light_level_textView);

        mGestureDetector = createGestureDetector(this);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mGravitySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mGravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);

        gestureDetector.setBaseListener(new GestureDetector.BaseListener()
        {
            @Override
            public boolean onGesture(Gesture gesture)
            {
            switch (gesture)
            {
                case SWIPE_LEFT:
                    startActivity(new Intent(getApplicationContext(), GestureActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    break;
                case SWIPE_RIGHT:
                    startActivity(new Intent(getApplicationContext(), CameraActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
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

    @Override
    public final void onSensorChanged(SensorEvent event)
    {
        if (event.sensor == mGravitySensor)
        {
            double headTiltAngle = -Math.toDegrees(Math.asin(event.values[2] / STANDARD_GRAVITY));

            if (headTiltAngle > STRAIGHT_AHEAD_HEAD_TILT_ANGLE + HEAD_TILT_ANGLE_THRESHOLD)
            {
                mLookingUpTextView.setTextColor(BLUE_COLOR);
                mLookingStraightAheadTextView.setTextColor(LIGHT_BLUE_COLOR);
                mLookingDownTextView.setTextColor(LIGHT_BLUE_COLOR);
            }
            else if (headTiltAngle <= STRAIGHT_AHEAD_HEAD_TILT_ANGLE + HEAD_TILT_ANGLE_THRESHOLD && headTiltAngle >= STRAIGHT_AHEAD_HEAD_TILT_ANGLE - HEAD_TILT_ANGLE_THRESHOLD)
            {
                mLookingUpTextView.setTextColor(LIGHT_BLUE_COLOR);
                mLookingStraightAheadTextView.setTextColor(BLUE_COLOR);
                mLookingDownTextView.setTextColor(LIGHT_BLUE_COLOR);
            }
            else if (headTiltAngle < STRAIGHT_AHEAD_HEAD_TILT_ANGLE - HEAD_TILT_ANGLE_THRESHOLD)
            {
                mLookingUpTextView.setTextColor(LIGHT_BLUE_COLOR);
                mLookingStraightAheadTextView.setTextColor(LIGHT_BLUE_COLOR);
                mLookingDownTextView.setTextColor(BLUE_COLOR);
            }

            double headCockedAngle = Math.toDegrees(Math.asin(event.values[0] / STANDARD_GRAVITY));
            if (headCockedAngle > HEAD_COCKED_ANGLE_THRESHOLD)
            {
                mHeadCockedLeftTextView.setTextColor(BLUE_COLOR);
                mHeadUprightTextView.setTextColor(LIGHT_BLUE_COLOR);
                mHeadCockedRightTextView.setTextColor(LIGHT_BLUE_COLOR);
            }
            else if (headCockedAngle <= HEAD_COCKED_ANGLE_THRESHOLD && headCockedAngle >= -HEAD_COCKED_ANGLE_THRESHOLD)
            {
                mHeadCockedLeftTextView.setTextColor(LIGHT_BLUE_COLOR);
                mHeadUprightTextView.setTextColor(BLUE_COLOR);
                mHeadCockedRightTextView.setTextColor(LIGHT_BLUE_COLOR);
            }
            else if (headCockedAngle < -HEAD_COCKED_ANGLE_THRESHOLD)
            {
                mHeadCockedLeftTextView.setTextColor(LIGHT_BLUE_COLOR);
                mHeadUprightTextView.setTextColor(LIGHT_BLUE_COLOR);
                mHeadCockedRightTextView.setTextColor(BLUE_COLOR);
            }
        }
        else if (event.sensor == mGyroscope)
        {
            if (event.values[0] < -UP_DOWN_ANGULAR_VELOCITY_THRESHOLD)
            {
                mRotatingHeadUpTextView.setTextColor(LIGHT_BLUE_COLOR);
                mRotatingHeadDownTextView.setTextColor(BLUE_COLOR);
            }
            else if (event.values[0] >= -UP_DOWN_ANGULAR_VELOCITY_THRESHOLD && event.values[0] <= UP_DOWN_ANGULAR_VELOCITY_THRESHOLD)
            {
                mRotatingHeadUpTextView.setTextColor(LIGHT_BLUE_COLOR);
                mRotatingHeadDownTextView.setTextColor(LIGHT_BLUE_COLOR);
            }
            else if (event.values[0] > UP_DOWN_ANGULAR_VELOCITY_THRESHOLD)
            {
                mRotatingHeadUpTextView.setTextColor(BLUE_COLOR);
                mRotatingHeadDownTextView.setTextColor(LIGHT_BLUE_COLOR);
            }

            if (event.values[1] < -LEFT_RIGHT_ANGULAR_VELOCITY_THRESHOLD)
            {
                mRotatingHeadLeftTextView.setTextColor(LIGHT_BLUE_COLOR);
                mRotatingHeadRightTextView.setTextColor(BLUE_COLOR);
            }
            else if (event.values[1] >= -LEFT_RIGHT_ANGULAR_VELOCITY_THRESHOLD && event.values[1] <= LEFT_RIGHT_ANGULAR_VELOCITY_THRESHOLD)
            {
                mRotatingHeadLeftTextView.setTextColor(LIGHT_BLUE_COLOR);
                mRotatingHeadRightTextView.setTextColor(LIGHT_BLUE_COLOR);
            }
            else if (event.values[1] > LEFT_RIGHT_ANGULAR_VELOCITY_THRESHOLD)
            {
                mRotatingHeadLeftTextView.setTextColor(BLUE_COLOR);
                mRotatingHeadRightTextView.setTextColor(LIGHT_BLUE_COLOR);
            }
        }
        else if (event.sensor == mLightSensor)
        {
            mLightLevelTextView.setText(String.format("%.0f lux", event.values[0]));
        }
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        Log.d("SensorActivity", "Accuracy for sensor " + sensor + " changed to " + accuracy);
    }
}

