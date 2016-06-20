package nthu.nmsl.crowdsourcinggame.tools;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import nthu.nmsl.crowdsourcinggame.objects.TaskData;
import nthu.nmsl.crowdsourcinggame.objects.VideoObject;
import nthu.nmsl.crowdsourcinggame.settings.SocketSetting;

/**
 * Created by inin6 on 2015/10/31.
 */
public class CommunicateSocket {
    private static final String TAG = "tools.socket";
    private static CommunicateSocket objectSelf = null;
    java.net.Socket brokerSocket = null;
    ObjectOutputStream os;
    ObjectInputStream is;
    private boolean isConnected = false;
    public CommunicateSocket(){
    }

    public static CommunicateSocket getInstance() {
        if (objectSelf == null) {
            objectSelf = new CommunicateSocket();
        }
        return objectSelf;
    }

    public boolean connectToBroker() {
        if (isConnected) return true;
        try {
            brokerSocket = new java.net.Socket(SocketSetting.brokerIP, SocketSetting.brokerPort);
            os = new ObjectOutputStream(brokerSocket.getOutputStream());
            is = new ObjectInputStream(brokerSocket.getInputStream());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        isConnected = true;
        Log.d(TAG, "broker connected");
        return true;
    }

    public boolean isConnect() {
        return isConnected;
    }
    public boolean sendTaskRequest() {
        try {
            os.writeUTF("getTask");
            os.flush();
            Log.d(TAG,"send Task request");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "wirte TaskData to broker failed");
            e.printStackTrace();
            isConnected = false;
            return false;
        }
        return true;
    }

    public ArrayList<TaskData> waitForTaskData() {
        ArrayList<TaskData> data = null;
        try {
            data = (ArrayList<nthu.nmsl.crowdsourcinggame.objects.TaskData>) is.readObject();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "get TaskData from broker failed");
            e.printStackTrace();
            isConnected = false;
            return null;
        }
        return data;
    }

    public boolean sendVideo(VideoObject object) {
        try {
            os.writeUTF("sendVideo");
            os.flush();
            os.reset();
            os.writeObject(object);
            os.flush();
            Log.d(TAG,"send video object");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d(TAG, "send video object failed");
            e.printStackTrace();
            isConnected = false;
            return false;
        }
        return true;
    }
}
