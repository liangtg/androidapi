package github.liangtg.androidapi.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by liangtg on 17-7-10.
 */

public class DataManager {

    private final String TABLE_TITLE = "title";

    public static final DataManager instance() {
        return DataManagerHolder.instance;
    }

    public ArrayList<TitleItem> getTitleList() {
        return getTitleList(0);
    }

    public ArrayList<TitleItem> getTitleList(long pid) {
        ArrayList<TitleItem> result = new ArrayList<>();
        Cursor cursor = SQLHelper.instance().getReadableDatabase().query(TABLE_TITLE,
                null,
                "pid=?",
                new String[]{String.valueOf(pid)},
                null,
                null,
                "page");
        while (cursor.moveToNext()) {
            TitleItem item = cursorToTItle(cursor);
            result.add(item);
            if (item.pageCount > 0) {
                result.addAll(getTitleList(item.id));
            }
        }
        cursor.close();
        return result;
    }

    public TitleItem addTitle(long pid, String cn, String en) {
        SQLiteDatabase writableDatabase = SQLHelper.instance().getWritableDatabase();
        writableDatabase.beginTransaction();
        TitleItem item = new TitleItem();
        item.pid = pid;
        TitleItem parent = getTitle(pid);
        ContentValues values = new ContentValues();
        if (null != parent) {
            item.page = parent.pageCount;
            ContentValues update = new ContentValues();
            update.put("page_count", parent.pageCount + 1);
            writableDatabase.update(TABLE_TITLE, update, "_id=?", new String[]{String.valueOf(pid)});
        }
        item.cnName = cn;
        item.enName = en;
        values.put("pid", pid);
        values.put("page", item.page);
        values.put("cn_name", cn);
        values.put("en_name", en);
        item.id = writableDatabase.insert(TABLE_TITLE, null, values);
        writableDatabase.setTransactionSuccessful();
        writableDatabase.endTransaction();
        return item;
    }

    public TitleItem getTitle(long id) {
        TitleItem item = null;
        Cursor cursor = SQLHelper.instance().getReadableDatabase().query(TABLE_TITLE,
                null,
                "_id=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            item = cursorToTItle(cursor);
        }
        cursor.close();
        return item;
    }

    public ArrayList<TitleItem> getTitleListSingle(long pid) {
        ArrayList<TitleItem> result = new ArrayList<>();
        Cursor cursor = SQLHelper.instance().getReadableDatabase().query(TABLE_TITLE,
                null,
                "pid=?",
                new String[]{Long.toString(pid)},
                null,
                null,
                "page");
        while (cursor.moveToNext()) {
            TitleItem item = cursorToTItle(cursor);
            result.add(item);
        }
        cursor.close();
        return result;
    }

    @NonNull
    private TitleItem cursorToTItle(Cursor cursor) {
        TitleItem item = new TitleItem();
        item.id = cursor.getInt(0);
        item.pid = cursor.getInt(1);
        item.page = cursor.getInt(2);
        item.pageCount = cursor.getInt(3);
        item.cnName = cursor.getString(4);
        item.enName = cursor.getString(5);
        return item;
    }

    private static final class DataManagerHolder {
        private static final DataManager instance = new DataManager();
    }

}
