package nthu.nmsl.crowdsourcinggame.fragment;

import android.app.Fragment;
import android.hardware.Camera;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nthu.nmsl.crowdsourcinggame.R;
import nthu.nmsl.crowdsourcinggame.activities.BackgroundManager;
import nthu.nmsl.crowdsourcinggame.objects.TaskData;
import nthu.nmsl.crowdsourcinggame.objects.TaskDataManager;
import nthu.nmsl.crowdsourcinggame.settings.StorageSetting;
import nthu.nmsl.crowdsourcinggame.tools.LocationSensor;
/**
 * Created by inin6 on 2015/11/11.
 */

public class ARFragmentV2 extends Fragment {
    private MediaRecorder mediarecorder;// 錄製視頻的類

    private Camera camera;
    private boolean isRecording = false;
    private long recordTaskID = -1;
    private View view;
    private static final String TAG = "OCVSample::Activity";

    private JavaCameraView  mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this.getActivity()) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    mOpenCvCameraView = (JavaCameraView) view.findViewById(R.id.tutorial1_activity_java_surface_view);
                    mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
                    mOpenCvCameraView.setCvCameraViewListener(cameraViewListener);
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public ARFragmentV2() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }
    private OpenGLView glSurfaceView;


    private CvCameraViewListener2 cameraViewListener = new CvCameraViewListener2() {
        private int width, height;
        private Mat                    mRgba;

        public void onCameraViewStarted(int width, int height) {
            Log.i("onCameraViewStarted", "onCameraViewStarted");
            this.width = width;
            this.height = height;
            mRgba = new Mat(height, width, CvType.CV_8UC4);
            mask = new Mat(height + 2, width + 2, CvType.CV_8U);
            edgeRGB = new Mat(height, width, CvType.CV_8UC3);
            matRGB = new Mat(height, width, CvType.CV_8UC3);
            edges = new Mat(height, width, CvType.CV_8UC1);
            maskOrigin = new Mat(height + 2, width + 2, CvType.CV_8U);
        }

        public void onCameraViewStopped() {
            Log.i("onCameraViewStopped", "onCameraViewStopped");
        }
        private Mat mask;
        private Mat maskOrigin;
        private Mat edgeRGB;
        private Mat matRGB;
        private int skipFrameCount = 0;
        private int skipFrame = 10;
        public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
            if (skipFrameCount++ % skipFrame != 0) return matRGB;
            mRgba = inputFrame.rgba();
            //mGray = inputFrame.gray();
            Imgproc.cvtColor(mRgba, matRGB, Imgproc.COLOR_RGBA2RGB);
            /*
                        Canny
                        */
            Imgproc.Canny(matRGB, edges, 50, 150);
            Imgproc.dilate(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.dilate(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.dilate(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.dilate(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.erode(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.erode(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.erode(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.erode(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));

            //Imgproc.dilate(edges, edges, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.cvtColor(edges, edgeRGB, Imgproc.COLOR_GRAY2RGB);
            Core.add(matRGB, edgeRGB, matRGB);
            /*
                        floodfill
                        */
            mask = new Mat(height + 2, width + 2, CvType.CV_8U);
            Imgproc.floodFill(matRGB, mask, new Point(width / 2, height - 20), new Scalar(255), null, new Scalar(60, 60, 60), new Scalar(60, 60, 60), Imgproc.FLOODFILL_FIXED_RANGE);

            return matRGB;
        }

        Mat edges;
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.ar_page_v2, null);
        }
        mOpenCvCameraView = (JavaCameraView) view.findViewById(R.id.tutorial1_activity_java_surface_view);


        glSurfaceView = new OpenGLView(this.getActivity());
        this.getActivity().addContentView(glSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        
        arInitial();



        Timer timer = new Timer(true);
        timer.schedule(new MyTimerTask(), 1000, 1000);
        return view;
    }

    public void arInitial(){
        Log.e(TAG, "AR initialization");
        Log.i(TAG, "called onCreate");



        //SensorHandler sensor = new SensorHandler(this);




        ArrayList<TaskData> datas = TaskDataManager.getInstance().getAll();
        if ( datas != null ) {
            for (TaskData data : datas) {
                Log.e(TAG, String.valueOf(data.getTaskID()) + " " + data.getTaskName());
                if (data.getTaskID() == 11 ) {
                    /*data.setLatitude(currentLocation.getLongitude() - 0.0001);
                    data.setLongitude(currentLocation.getLatitude() - 0.0001);*/
                }
            }
        }


    }

    private class MyTimerTask extends TimerTask {
        public void run() {
            Location currentLocation = LocationSensor.getInstance().getCurrentLocation();

            ArrayList<TaskData> datas = TaskDataManager.getInstance().getAll();
            if ( datas != null && !isRecording ) {
                for (TaskData data : datas) {
                    if (data.getTaskID() == 11 ) {
                        data.setLatitude(currentLocation.getLongitude() - 0.0001);
                        data.setLongitude(currentLocation.getLatitude() - 0.0001);
                    }
                    float[] results = new float[1];
                    Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), data.getLongitude(), data.getLatitude(), results);
                    if ( results[0] < 50) { // distance (meter)
                        Log.d(TAG, data.getTaskName());
                        recordTaskID = data.getTaskID();
                        startRecord(String.valueOf(recordTaskID) + ".mp4");
                    }
                }
            }
        }
    };

    private View.OnTouchListener touchEvent = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, final MotionEvent event) {
            /*if (arWorld == null || beyondarTouch == null || event == null) {
                return false;
            }
            beyondarTouch.onTouchBeyondarView(event, beyondarFragment.getGLSurfaceView());*/
            return true;
        }
    };


    private boolean startRecord(String fileName) {
        if (isRecording) return false;
        Log.d(TAG,"Start recoder!!");
        /*Toast.makeText(getActivity(), "Start Record.",
                Toast.LENGTH_LONG).show();*/
        mediarecorder = new MediaRecorder();
        mediarecorder.reset();
        camera.lock();
        //camera.setDisplayOrientation(90);
        camera.unlock();
        mediarecorder.setCamera(camera);
        mediarecorder.setOrientationHint(90);
        //mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediarecorder.setVideoSize(1280, 720);
        //mediarecorder.setVideoFrameRate(20);
        mediarecorder.setOutputFile(StorageSetting.videoSavingPath + fileName);
        try {
            // 準備錄製
            mediarecorder.prepare();
            // 開始錄製
            mediarecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isRecording = true;
        return true;
    }
    public boolean stopRecord() {
        if (isRecording && mediarecorder != null) {
            Log.d(TAG,"Stop recoder!!");
            // 停止錄製
            mediarecorder.stop();
            // 釋放資源
            mediarecorder.release();
            mediarecorder = null;
            BackgroundManager.getInstance(getActivity()).uploadVideo(String.valueOf(recordTaskID) + ".mp4", recordTaskID);
            recordTaskID = -1;
            return true;
        }
        return false;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }


    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this.getActivity(), mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
        if (isRecording && mediarecorder != null) {
            Log.d(TAG, "Stop recoder!!");
            // 停止錄製
            mediarecorder.stop();
            // 釋放資源
            mediarecorder.release();
            mediarecorder = null;
            recordTaskID = -1;
        }
        Log.d(TAG,"Home onDestroy");
        //SensorInstance.profiler.flushAll();
    }

}
