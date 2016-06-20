package nthu.nmsl.crowdsourcinggame.fragment;

import android.app.Fragment;
import android.hardware.Camera;
import android.location.Location;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.BeyondarGLSurfaceView;
import com.beyondar.android.view.OnTouchBeyondarViewListener;
import com.beyondar.android.world.BeyondarObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import nthu.nmsl.crowdsourcinggame.R;
import nthu.nmsl.crowdsourcinggame.activities.BackgroundManager;
import nthu.nmsl.crowdsourcinggame.instances.CreatureList;
import nthu.nmsl.crowdsourcinggame.objects.TaskData;
import nthu.nmsl.crowdsourcinggame.objects.TaskDataManager;
import nthu.nmsl.crowdsourcinggame.settings.StorageSetting;
import nthu.nmsl.crowdsourcinggame.tools.BeyondARWorld;
import nthu.nmsl.crowdsourcinggame.tools.LocationSensor;

/**
 * Created by inin6 on 2015/11/11.
 */
public class ARFragment extends Fragment implements SurfaceHolder.Callback {
    private static final String TAG = "Tab.AR";
    private BeyondarFragmentSupport beyondarFragment;
    private BeyondARWorld arWorld;
    private MediaRecorder mediarecorder;// 錄製視頻的類
    private SurfaceView surfaceview;// 顯示視頻的控制項
    // 用來顯示視頻的一個介面，我靠不用還不行，也就是說用mediarecorder錄製視頻還得給個介面看
// 想偷偷錄視頻的同學可以考慮別的辦法。。嗯需要實現這個介面的Callback介面
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private boolean isRecording = false;
    private long recordTaskID = -1;
    private View view;

    public ARFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.ar_page, null);
        }
        arInitial();

        camera = beyondarFragment.getCameraView().getCamera();
        surfaceview = beyondarFragment.getCameraView();
        surfaceHolder = surfaceview.getHolder();// 取得holder
        surfaceHolder.addCallback(this); // holder加入回檔介面
        // setType必須設置，要不出錯.
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        Timer timer = new Timer(true);
        timer.schedule(new MyTimerTask(), 1000, 1000);
        return view;
    }

    public void arInitial(){
        Log.e(TAG, "AR initialization");
        beyondarFragment = (BeyondarFragmentSupport) ((FragmentActivity)getActivity()).getSupportFragmentManager().findFragmentById(
                R.id.ARFragment);
        Location currentLocation = LocationSensor.getInstance(getActivity()).getCurrentLocation();
        arWorld = new BeyondARWorld(getActivity(), currentLocation.getLatitude(), currentLocation.getLongitude(), false);
        beyondarFragment.setWorld(arWorld.getWorld());
        beyondarFragment.showFPS(true);
        beyondarFragment.setMaxDistanceToRender(50);
        beyondarFragment.getGLSurfaceView().setOnTouchListener(touchEvent);
        ArrayList<TaskData> datas = TaskDataManager.getInstance().getAll();
        if ( datas != null ) {
            for (TaskData data : datas) {
                Log.e(TAG, String.valueOf(data.getTaskID()) + " " + data.getTaskName());
                if (data.getTaskID() == 11 ) {
                    data.setLatitude(currentLocation.getLongitude() - 0.0001);
                    data.setLongitude(currentLocation.getLatitude() - 0.0001);
                }
                arWorld.addObject(data.getTaskID(), data.getLongitude(), data.getLatitude(), CreatureList.pictures[data.getCreatureID()], data.getTaskName());
            }
        }
    }

    private class MyTimerTask extends TimerTask {
        public void run() {
            Location currentLocation = LocationSensor.getInstance().getCurrentLocation();
            arWorld.setGeoPosition(currentLocation.getLatitude(), currentLocation.getLongitude());
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
            if (arWorld == null || beyondarTouch == null || event == null) {
                return false;
            }
            beyondarTouch.onTouchBeyondarView(event, beyondarFragment.getGLSurfaceView());
            return true;
        }
    };

    public OnTouchBeyondarViewListener beyondarTouch = new OnTouchBeyondarViewListener() {
        public void onTouchBeyondarView(MotionEvent event, BeyondarGLSurfaceView beyondarView) {
            float x = event.getX();
            float y = event.getY();
            ArrayList<BeyondarObject> geoObjects = new ArrayList<BeyondarObject>();
            // This method call is better to don't do it in the UI thread!
            beyondarView.getBeyondarObjectsOnScreenCoordinates(x, y, geoObjects);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    /*Iterator<BeyondarObject> iterator = geoObjects.iterator();
                    if (iterator.hasNext()) {
                        BeyondarObject geoObject = iterator.next();
                        startRecord("temp.3gp");
                    }*/
                    break;
                case MotionEvent.ACTION_UP:
                    stopRecord();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                default:
                    break;
            }
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
        mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
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

    public void onResume() {
        super.onResume();
    }

    public void onDestroy(){
        super.onDestroy();
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

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
// 將holder，這個holder為開始在oncreat裡面取得的holder，將它賦給surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
// 將holder，這個holder為開始在oncreat裡面取得的holder，將它賦給surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
// surfaceDestroyed的時候同時物件設置為null
        surfaceview = null;
        surfaceHolder = null;
        mediarecorder = null;
    }
}
