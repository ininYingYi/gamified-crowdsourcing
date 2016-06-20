package nthu.nmsl.crowdsourcinggame.tools;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by YingYi on 2016/3/23.
 */
public class BlueTooth {
    private static final String TAG = "THINBTCLIENT";
    private static final boolean D = true;
    private static BlueTooth objectSelf = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outStream = null;
    private boolean isInit = false;
    private boolean isConnected = false;
    // Well known SPP UUID (will *probably* map to
    // RFCOMM channel 1 (default) if not in use);
    // see comments in onResume().
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // ==> hardcode your server's MAC address here <==
    private static String address = "80:86:F2:62:DB:CF";

    public BlueTooth() {
    }

    public static BlueTooth getInstance() {
        if (objectSelf == null) {
            objectSelf = new BlueTooth();
        }
        return objectSelf;
    }

    public boolean init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }
        isInit = true;
        return true;
    }

    public boolean connect() {
        if (isConnected) return true;
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Socket creation failed.", e);
            return false;
        }
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket.connect();
            Log.e(TAG, "ON RESUME: BT connection established, data transfer link open.");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.e(TAG,"ON RESUME: Unable to close socket during connection failure", e2);
            }
            return false;
        }
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
        }
        return true;
    }

    public void sendMessage(byte[] message) {
        try {
            outStream.write(message);
        } catch (IOException e) {
            Log.e(TAG, "ON RESUME: Exception during write.", e);
        }
    }

    public boolean isConnect() {
        return isConnected;
    }
}
