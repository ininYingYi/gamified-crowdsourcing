package nthu.nmsl.crowdsourcinggame.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by InIn on 2015/10/31.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    // 資料庫名稱
    public static final String DATABASE_NAME = "CrowdSourcingGame.db";
    // 資料庫版本，資料結構改變的時候要更改這個數字，通常是加一
    public static final int VERSION = 2;
    // 資料庫物件，固定的欄位變數
    private SQLiteDatabase database;
    private Context context;

    // 建構子，在一般的應用都不需要修改
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public synchronized SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new SQLiteHelper(context).getWritableDatabase();
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (database == null || !database.isOpen()) {
            Log.e("database", "null");
            //database = new SQLiteHelper(context).getWritableDatabase();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 刪除原有的表格
        //db.execSQL("DROP TABLE IF EXISTS " + postDataDAO.TABLE_NAME);
        // 呼叫onCreate建立新版的表格
        onCreate(db);
    }
}
