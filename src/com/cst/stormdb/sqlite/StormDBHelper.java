package com.cst.stormdb.sqlite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.cst.stormdb.Config;
import com.cst.stormdb.annotation.Table;

/**
 * storm db helper
 * 
 * @author Storm
 * 
 */
public class StormDBHelper extends SQLiteOpenHelper {
	//singleton
	private static StormDBHelper instance;

	public StormDBHelper(Context context) {
		this(context, null);
	}

	public StormDBHelper(Context context, CursorFactory factory) {
		super(context, Config.DB_NAME, factory, Config.DB_VERSION);
	}

	public StormDBHelper getInstance(Context context) {
		return getInstance(context, null);
	}

	public StormDBHelper getInstance(Context context, CursorFactory factory) {
		if (instance == null) {
			instance = new StormDBHelper(context, factory);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			upgradeWithoutDataChanged(db);
		}
	}

	/**
	 * upgrade database and retain all data, include removing or adding columns (rename column is not supported)
	 * 
	 * @param db
	 */
	protected void upgradeWithoutDataChanged(SQLiteDatabase db) {
		try {
			db.beginTransaction();

			// drop temp table
			dropAllTemp(db);

			// create new table, if old table not exist then create a new one, this step makes all tables created
			onCreate(db);

			// rename all tables to temp
			renameAllToTemp(db);

			// create new tables
			onCreate(db);

			// insert data from temp table to new table
			insertAllFromTemp(db);

			// drop temp table
			dropAllTemp(db);

			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * insert all from temp tables
	 * 
	 * @param db
	 */
	private void insertAllFromTemp(SQLiteDatabase db) {
//		insertFromTemp(db, RoomChat.TABLE_NAME_TEMP, RoomChat.TABLE_NAME);
	}

	/**
	 * insert from temp table
	 * 
	 * @param db
	 */
	private void insertFromTemp(SQLiteDatabase db, String oldTableName, String newTableName) {
		String columns = getCommonColumns(db, oldTableName, newTableName);
		db.execSQL("INSERT INTO " + newTableName + " (" + columns + ") SELECT " + columns + " FROM " + oldTableName);
	}

	/**
	 * rename all table to temp
	 * 
	 * @param db
	 */
	private void renameAllToTemp(SQLiteDatabase db) {
		//db.execSQL("ALTER TABLE " + RoomChat.TABLE_NAME + " RENAME TO " + RoomChat.TABLE_NAME_TEMP);
	}

	/**
	 * drop all temp tables
	 * 
	 * @param db
	 */
	private void dropAllTemp(SQLiteDatabase db) {
		//db.execSQL("DROP TABLE IF EXISTS " + RoomChat.TABLE_NAME_TEMP);
	}

	/**
	 * get all common columns from table old and new
	 * 
	 * @param db
	 * @param tableName
	 * @return
	 */
	private String getCommonColumns(SQLiteDatabase db, String oldTableName, String newTableName) {
		List<String> oldTableColumns = null;
		List<String> newTableColumns = null;

		oldTableColumns = getTableColumns(db, oldTableName);
		newTableColumns = getTableColumns(db, newTableName);

		Map<String, Integer> newColumns = new HashMap<String, Integer>();
		for (String oColumn : oldTableColumns) {
			newColumns.put(oColumn, 1);
		}

		for (String nColumn : newTableColumns) {
			if (newColumns.containsKey(nColumn)) {
				newColumns.put(nColumn, 2);
			}
		}

		//ID will not insert
		newColumns.remove(BaseColumns._ID);

		List<String> columns = new ArrayList<String>();
		for (Iterator<String> iter = newColumns.keySet().iterator(); iter.hasNext();) {
			String c = iter.next();
			if (newColumns.get(c) == 2) {
				columns.add(c);
			}
		}

		String columnsSeperated = TextUtils.join(",", columns);
		return columnsSeperated;
	}

	/**
	 * get columns of a table
	 * 
	 * @param db
	 * @param tableName
	 * @return
	 */
	private List<String> getTableColumns(SQLiteDatabase db, String tableName) {
		ArrayList<String> columns = new ArrayList<String>();
		Cursor c = null;
		try {
			c = db.rawQuery("select * from " + tableName + " limit 1", null);
			if (c != null) {
				columns = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null)
				c.close();
		}
		return columns;
	}

}
