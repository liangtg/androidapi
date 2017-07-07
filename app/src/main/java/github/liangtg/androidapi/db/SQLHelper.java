package github.liangtg.androidapi.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import github.liangtg.androidapi.IApplication;

/**
 * Created by liangtg on 17-7-7.
 */

public class SQLHelper extends SQLiteOpenHelper {
    public SQLHelper() {
        super(IApplication.context(), "api", null, 1);
    }

    public static SQLHelper instance() {
        return SQLHolder.instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE title(_id INTEGER PRIMARY KEY AUTOINCREMENT, pid INTEGER DEFAULT 0, oid INTEGER DEFAULT 0, cn_name TEXT, en_name TEXT);");
        db.execSQL("CREATE TABLE  content  ( _id INTEGER PRIMARY KEY AUTOINCREMENT, tid INTEGER DEFAULT 0, page INTEGER DEFAULT 0, cn TEXT, en TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static final class SQLHolder {
        public static final SQLHelper instance = new SQLHelper();
    }

}
