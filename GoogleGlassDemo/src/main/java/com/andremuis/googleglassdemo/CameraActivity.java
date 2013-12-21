package com.andremuis.googleglassdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.glass.media.CameraManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.File;

/**
 * Created by amuis on 12/19/13.
 */
public class CameraActivity extends Activity {

    private static final int TAKE_PICTURE_REQUEST = 11;

    private ImageView mPictureImageView;
    private TextView mProcessingTextView;

    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_camera);

        mPictureImageView = (ImageView)findViewById(R.id.picture_imageView);

        mProcessingTextView = (TextView)findViewById(R.id.processing_textView);
        mProcessingTextView.setVisibility(View.INVISIBLE);

        mGestureDetector = createGestureDetector(this);
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);

        gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture)
            {
            switch (gesture)
            {
                case TAP:
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_PICTURE_REQUEST);
                    break;
                case SWIPE_LEFT:
                    startActivity(new Intent(getApplicationContext(), SensorActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    break;
                case SWIPE_RIGHT:
                    startActivity(new Intent(getApplicationContext(), VoiceActivity.class));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK)
        {
            String picturePath = data.getStringExtra(CameraManager.EXTRA_PICTURE_FILE_PATH);
            processPictureWhenReady(picturePath);
            mProcessingTextView.setVisibility(View.VISIBLE);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processPictureWhenReady(final String picturePath)
    {
        final File pictureFile = new File(picturePath);

        if (pictureFile.exists())
        {
            Bitmap pictureBitmap = BitmapFactory.decodeFile(pictureFile.getAbsolutePath());
            mPictureImageView.setImageBitmap(pictureBitmap.createScaledBitmap(pictureBitmap, mPictureImageView.getWidth(), mPictureImageView.getHeight(), true));

            mProcessingTextView.setVisibility(View.INVISIBLE);
        }
        else
        {
            final File parentDirectory = pictureFile.getParentFile();

            FileObserver observer = new FileObserver(parentDirectory.getPath())
            {
                private boolean isFileWritten;

                @Override
                public void onEvent(int event, String path)
                {
                    if (!isFileWritten)
                    {
                        File affectedFile = new File(parentDirectory, path);
                        isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile.equals(pictureFile));

                        if (isFileWritten)
                        {
                            stopWatching();

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    processPictureWhenReady(picturePath);
                                }
                            });
                        }
                    }
                }
            };

            observer.startWatching();
        }
    }
}
