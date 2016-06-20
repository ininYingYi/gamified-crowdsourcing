package nthu.nmsl.crowdsourcinggame.activities;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import nthu.nmsl.crowdsourcinggame.objects.TaskData;
import nthu.nmsl.crowdsourcinggame.objects.TaskDataManager;
import nthu.nmsl.crowdsourcinggame.objects.VideoObject;
import nthu.nmsl.crowdsourcinggame.settings.StorageSetting;
import nthu.nmsl.crowdsourcinggame.tools.BlueTooth;
import nthu.nmsl.crowdsourcinggame.tools.CommunicateSocket;
import nthu.nmsl.crowdsourcinggame.tools.SQLiteConnector;

/**
 * Created by inin6 on 2015/11/6.
 */
public class BackgroundManager {
    private static BackgroundManager objectSelf;
    private boolean running = true;
    private Context context;
    private ArrayList<TaskData> queueOfTask;
    private CommunicateSocket socket;
    private BlueTooth blueTooth = null;
    private static connectType uploadConnectType = connectType.WiFi;
    enum connectType {
        WiFi, BlueTooth
    }
    public static BackgroundManager getInstance(Context context) {
        if (objectSelf == null) {
            objectSelf = new BackgroundManager(context);
        }
        return objectSelf;
    }

    public BackgroundManager(Context context) {
        this.context = context;
        socket = CommunicateSocket.getInstance();
        if (uploadConnectType == connectType.BlueTooth) {
            blueTooth = BlueTooth.getInstance();
        }
        /*if (true) {
            SQLiteConnector<TaskData> sqliteConnector = new SQLiteConnector<TaskData>(this.context);
            sqliteConnector.createTable();
            TaskData tempData = new TaskData(0,"testName", 24.0, 140.0, 1, "13131");
            sqliteConnector.insert(tempData);
            Log.e("sqlCount", String.valueOf(sqliteConnector.getCount()));
            sqliteConnector.close();
        }*/
    }

    private Thread background = new Thread() {
        public void run() { // override Thread's run()
            Log.d("thread", "Here is the starting point of Thread.");
            socket.connectToBroker();
            if (uploadConnectType == connectType.BlueTooth) {
                blueTooth.connect();
            }
            // insert data to SQLite

            /*while (running) {
                System.out.println("User Created Thread");
                try {
                    // First step check internet download task
                    updateTask();
                    // Second update location

                    this.sleep(5000);
                } catch (InterruptedException e) {
                    Log.e("thread","thread");
                    e.printStackTrace();
                }
            }*/
        }
    };

    public boolean updateTask() {
        if (socket.isConnect()) {
            System.out.println("connected");
            socket.sendTaskRequest();
            System.out.println("sended");
            ArrayList<TaskData> temp = socket.waitForTaskData();
            if (temp != null) {
                for (int i = 0; i < temp.size(); i++) {
                    TaskDataManager.getInstance().insert(temp.get(i));
                }
            }
            System.out.println("added");
            return true;
        }
        return false;
    }

    public boolean uploadVideo(String fileName, long id) {
        switch (uploadConnectType) {
            case WiFi:
                if (socket.isConnect()) {
                    System.out.println("uploadVideo");
                    try {
                        File myFile = new File(StorageSetting.videoSavingPath + fileName);
                        int size = (int)myFile.length();
                        byte [] dataByteArray  = new byte[size];
                        FileInputStream fis = new FileInputStream(myFile);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        bis.read(dataByteArray, 0, dataByteArray.length);
                        VideoObject sendObject = new VideoObject(fileName, size, dataByteArray, id);
                        socket.sendVideo(sendObject);
                        System.out.println("sended");
                    } catch (IOException e) {
                        Log.e("uploadVideo","uploadVideo failed");
                        e.printStackTrace();
                    }

                    return true;
                }
                break;
            case BlueTooth:
                if (blueTooth.isConnect()) {
                    System.out.println("uploadVideo");
                    try {
                        File myFile = new File(StorageSetting.videoSavingPath + fileName);
                        int size = (int)myFile.length();
                        byte [] dataByteArray  = new byte[size];
                        FileInputStream fis = new FileInputStream(myFile);
                        BufferedInputStream bis = new BufferedInputStream(fis);
                        bis.read(dataByteArray, 0, dataByteArray.length);
                        VideoObject sendObject = new VideoObject(fileName, size, dataByteArray, id);
                        // object to bytes
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutput out = null;
                        try {
                            out = new ObjectOutputStream(bos);
                            out.writeObject(sendObject);
                            byte[] yourBytes = bos.toByteArray();
                            blueTooth.sendMessage(yourBytes);
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException ex) {
                                // ignore close exception
                            }
                            try {
                                bos.close();
                            } catch (IOException ex) {
                                // ignore close exception
                            }
                        }
                        System.out.println("sended");
                    } catch (IOException e) {
                        Log.e("uploadVideo","uploadVideo failed");
                        e.printStackTrace();
                    }

                }
                break;
        }
        return false;
    }

    public void start() {
        background.start();
    }

    public void close() {
        running = false;
    }
}
