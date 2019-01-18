package com.attendancesolution.bams.adapters;

/**
 * Created by Akshay on 4/6/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


// Search for "TODO", and make the appropriate changes.
public class DBAttendanceAdapter {

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_TOTAL_CLASSES = "total_classes";
    public static final String KEY_ATTENDED_CLASSES = "attended_classes";
    public static final String KEY_DAY = "day";
    public static final String KEY_DATE = "date";
    public static final String KEY_MONTH = "month";
    public static final String KEY_PERCENTAGE = "percentage";
    public static final String KEY_SLOT1 = "slot1";
    public static final String KEY_SLOT2 = "slot2";
    public static final String KEY_SLOT3 = "slot3";

    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_DATE, KEY_DAY, KEY_MONTH, KEY_TOTAL_CLASSES,
            KEY_ATTENDED_CLASSES, KEY_PERCENTAGE, KEY_SLOT1, KEY_SLOT2, KEY_SLOT3};

    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_DATE = 1;
    public static final int COL_DAY = 2;
    public static final int COL_MONTH = 3;
    public static final int COL_TOTAL_CLASSES = 4;
    public static final int COL_ATTENDED_CLASSES = 5;
    public static final int COL_PERCENTAGE = 6;
    public static final int COL_SLOT1 = 7;
    public static final int COL_SLOT2 = 8;
    public static final int COL_SLOT3 = 9;
    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MyDb2";
    public static final String DATABASE_TABLE = "attendanceTable";
    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

			/*
             * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!

                    + KEY_DATE + " TEXT not null, "
                    + KEY_DAY + " TEXT not null, "
                    + KEY_MONTH + " TEXT not null, "
                    + KEY_TOTAL_CLASSES + " numeric not null, "
                    + KEY_ATTENDED_CLASSES + " numeric not null, "
                    + KEY_PERCENTAGE + " numeric not null, "
                    + KEY_SLOT1 + " numeric, "
                    + KEY_SLOT2 + " numeric, "
                    + KEY_SLOT3 + " numeric"
                    // Rest  of creation:
                    + ");";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 2;
    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAttendanceAdapter";
    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public DBAttendanceAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAttendanceAdapter open() throws SQLException {

        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() throws SQLException {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String date, String day, String month, int totalClasses, int attendedClasses,int slot1,int slot2,int slot3) {
        /*
         * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_DATE, date);
        initialValues.put(KEY_DAY, day);
        initialValues.put(KEY_MONTH, month);
        initialValues.put(KEY_TOTAL_CLASSES, totalClasses);
        initialValues.put(KEY_ATTENDED_CLASSES, attendedClasses);
        int percentage = ((attendedClasses * 100) / totalClasses);
        initialValues.put(KEY_PERCENTAGE, percentage);
        initialValues.put(KEY_SLOT1, slot1);
        initialValues.put(KEY_SLOT2, slot2);
        initialValues.put(KEY_SLOT3, slot3);
        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, KEY_ROWID + " DESC", null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(String date) {
        String where = KEY_DATE + "=\"" + date + "\"";
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(String date, String day, String month, int totalClasses, int attendedClasses,int slot1,int slot2,int slot3) {
        String where = KEY_DATE + "=\"" + date + "\"";

		/*
         * CHANGE 4:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_DATE, date);
        newValues.put(KEY_DAY, day);
        newValues.put(KEY_MONTH, month);
        newValues.put(KEY_TOTAL_CLASSES, totalClasses);
        newValues.put(KEY_ATTENDED_CLASSES, attendedClasses);
        int percentage = ((attendedClasses * 100) / totalClasses);
        newValues.put(KEY_PERCENTAGE, percentage);
        newValues.put(KEY_SLOT1, slot1);
        newValues.put(KEY_SLOT2, slot2);
        newValues.put(KEY_SLOT3, slot3);
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.e(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
