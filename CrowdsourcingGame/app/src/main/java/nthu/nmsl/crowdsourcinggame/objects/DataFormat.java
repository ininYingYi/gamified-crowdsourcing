package nthu.nmsl.crowdsourcinggame.objects;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by inin6 on 2015/11/6.
 */
public interface DataFormat {
    public long id = 0;
    public static final String TABLE_NAME = "localPostDatas";
    public static final String KEY_ID = "_id";
    public static final String DATABASE_SCHEMA = " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT )";

    public ContentValues getContentValues();

    public DataFormat cursorParse(Cursor cursor);

    public String getDBSchema();

    public String getTableName();
}
