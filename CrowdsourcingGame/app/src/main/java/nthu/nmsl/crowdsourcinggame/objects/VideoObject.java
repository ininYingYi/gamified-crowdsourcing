package nthu.nmsl.crowdsourcinggame.objects;

import java.io.Serializable;

/**
 * Created by inin6 on 2015/11/9.
 */
public class VideoObject implements Serializable {
    private long taskID;
    private String name;
    private int size;
    private byte[] data;
    public VideoObject(String name, int size, byte[] data, long taskID) {
        this.name = name;
        this.size = size;
        this.data = data;
        this.taskID = taskID;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public byte[] getData() {
        return data;
    }

    public long getTaskID() {
        return this.taskID;
    }
}
