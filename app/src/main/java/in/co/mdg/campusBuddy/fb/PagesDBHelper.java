
package in.co.mdg.campusBuddy.fb;


/**
 * Created by root on 14/5/15.
 */



import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rc on 13/5/15.
 */


public class PagesDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "pages_fb_liked.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PagesDB.PagesEntry.TABLE_NAME + " (" +


                    PagesDB.PagesEntry.COLUMN_NAME_Pages_ID + TEXT_TYPE + COMMA_SEP +
                    PagesDB.PagesEntry.COLUMN_NAME_Page_name + TEXT_TYPE  +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PagesDB.PagesEntry.TABLE_NAME;

    private static PagesDBHelper instance;


    private PagesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized PagesDBHelper getInstance(Context context)
    {
        if (instance == null)
            instance = new PagesDBHelper(context);

        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public void putInformation(SQLiteDatabase db, String page_ID, String page_name ) {

        ContentValues values = new ContentValues();
        values.put(PagesDB.PagesEntry.COLUMN_NAME_Pages_ID, page_ID);
        values.put(PagesDB.PagesEntry.COLUMN_NAME_Page_name, page_name);

        db.insert(
                PagesDB.PagesEntry.TABLE_NAME,
                null,
                values);

    }}
