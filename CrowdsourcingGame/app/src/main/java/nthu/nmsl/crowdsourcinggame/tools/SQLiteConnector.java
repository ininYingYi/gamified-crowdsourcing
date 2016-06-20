package nthu.nmsl.crowdsourcinggame.tools;

/**
 * Created by InIn on 2015/10/31.
 */
public class SQLiteConnector {
    /*private SQLiteHelper dbhelper;
    // 表格名稱

    private static final String KEY_ID = "id";
    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE = "CREATE TABLE if not exists ";
    // 資料庫物件
    private SQLiteDatabase db;
    // 建構子，一般的應用都不需要修改
    public SQLiteConnector(Context context) {
        if (dbhelper == null) {
            dbhelper = new SQLiteHelper(context);
            db = dbhelper.getDatabase(context);
        }
    }
    public boolean createTable() {
        db.execSQL(CREATE_TABLE + dataFormat.getTableName() + dataFormat.getDBSchema());
        return true;
    }
    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        dbhelper.close();
        db.close();
    }

    // 新增參數指定的物件
    public dataFormat insert(dataFormat item) {
        // 建立準備新增資料的ContentValues物件
        long id = db.insert(item.getTableName(), null, item.getContentValues());
        // 設定編號
        item.id = id;
        // 回傳結果
        return item;
    }

    // 修改參數指定的物件
    public boolean update(dataFormat item) {
        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.id;
        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(dataFormat.getTableName(), item.getContentValues(), where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id) {
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(dataFormat.getTableName(), where, null) > 0;
    }

    // 讀取所有記事資料
    public List<dataFormat> getAll() {
        List<dataFormat> result = new ArrayList<dataFormat>();
        Cursor cursor = db.query(dataFormat.getTableName(), null, null, null, null, null, "_id DESC", null);
        while (cursor.moveToNext()) {
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public dataFormat get(long id) {
        // 準備回傳結果用的物件
        dataFormat item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(dataFormat.getTableName(), null, where, null, null, null, null, null);
        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }
        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    public dataFormat getRecord(Cursor cursor) {
        // 回傳結果
        return null;
        //return (dataFormat) dataFormat.cursorParse((cursor));
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + dataFormat.getTableName(), null);
        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }*/
}
