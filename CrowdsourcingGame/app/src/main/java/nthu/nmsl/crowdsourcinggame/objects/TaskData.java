package nthu.nmsl.crowdsourcinggame.objects;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by inin6 on 2015/11/6.
 */
public class TaskData implements Serializable {
    public long id = 0;
    private int creatureID;
    private long taskID;
    private String taskName;
    private double latitude;
    private double longitude;
    private double areaSize;
    private String deadLine;
    /*private int isDone;
    private String doneTime;
    private long doneUserId;
    private String videoPath;*/

    /*private static final String KEY_ID = "_id";
    private static final String TASK_NAME_COLUMN = "name";
    private static final String LATITUDE_COLUMN = "latitude";
    private static final String LONGITUDE_COLUMN = "longitude";
    private static final String AREA_SIZE_COLUMN = "area_size";
    private static final String DEAD_LINE_COLUMN = "dead_line";
    /*private static final String IS_DONE_COLUMN = "is_done";
    private static final String DONE_TIME_COLUMN = "done_time";
    private static final String DONE_USER_ID_COLUMN = "done_user_id";
    private static final String VIDEO_PATH_COLUMN = "video_path";*/
    /*public static final String TABLE_NAME = "tasks";
    private static final String DATABASE_SCHEMA = " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT )" +
            TASK_NAME_COLUMN + " TEXT NOT NULL, " +
            LATITUDE_COLUMN + " FLOAT, " +
            LONGITUDE_COLUMN + " FLOAT, " +
            AREA_SIZE_COLUMN + " FLOAT, " +
            DEAD_LINE_COLUMN + " INT )";*/

    public TaskData(long taskID, String taskName, double latitude, double longitude, double areaSize, String deadLine, int creatureID) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.areaSize = areaSize;
        this.deadLine = deadLine;
        this.creatureID = creatureID;
    }

    public double getLatitude() {
        return this.latitude;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public String getTaskName() {
        return this.taskName;
    }
    public int getCreatureID() {
        return this.creatureID;
    }
    public long getTaskID() {
        return this.taskID;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    /*public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, this.id);
        cv.put(TASK_NAME_COLUMN, this.taskName);
        cv.put(LATITUDE_COLUMN, this.latitude);
        cv.put(LONGITUDE_COLUMN, this.longitude);
        cv.put(AREA_SIZE_COLUMN, this.areaSize);
        cv.put(DEAD_LINE_COLUMN, this.deadLine);
        cv.put(IS_DONE_COLUMN, this.isDone);
        cv.put(DONE_TIME_COLUMN, this.doneTime);
        cv.put(DONE_USER_ID_COLUMN, this.doneUserId);
        cv.put(VIDEO_PATH_COLUMN, this.videoPath);
        return cv;
    }

    public TaskData cursorParse(Cursor cursor) {
        long id = cursor.getLong(0);
        String taskName = cursor.getString(1);
        double latitude = cursor.getDouble(2);
        double longitude = cursor.getDouble(3);
        double areaSize = cursor.getDouble(4);
        String deadLine = cursor.getString(5);
        return new TaskData(id, taskName, latitude, longitude, areaSize, deadLine);
    }

    public String getDBSchema() {
        return TaskData.DATABASE_SCHEMA;
    }

    public String getTableName() {
        return TaskData.TABLE_NAME;
    }
    */
}
