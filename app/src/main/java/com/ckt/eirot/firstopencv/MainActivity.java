package com.ckt.eirot.firstopencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FirstOpenCV";
    private static final int OPENCVMANAGER_LOAD_SECCESS = 0;
    private ImageView mImageView;
    private Bitmap bmp;
    private boolean switchStatus;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.d("OpenCV", "OpenCV Manager loaded successfully");
                    Message mMessage = new Message();
                    mMessage.arg1 = OPENCVMANAGER_LOAD_SECCESS;
                    mHandler.sendMessage(mMessage);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OPENCVMANAGER_LOAD_SECCESS:
                    /**
                     *  No implementation found for long org.opencv.core.Mat.n_Mat
                     *  doOpenCV();
                     */
                    break;
                default:
                    Log.d(TAG, "--->> handleMessage: default");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.img_opencv);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenCV();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    private void doOpenCV() {
        Log.d(TAG, "----->>doOpenCV: switchStatus = " + switchStatus);
        if (switchStatus) {
            mImageView.setImageResource(R.drawable.ibeauty);
            switchStatus = false;
        }else {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ibeauty);
            Mat rgbMat = new Mat();
            Utils.bitmapToMat(bmp, rgbMat);

            Mat grayMat = new Mat();
            Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);

            Bitmap bmpOut = Bitmap.createBitmap(grayMat.cols(), grayMat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(grayMat, bmpOut);
            mImageView.setImageBitmap(bmpOut);

            switchStatus = true;
        }
    }
}
