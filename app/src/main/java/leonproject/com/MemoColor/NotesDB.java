package leonproject.com.MemoColor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fudan on 3/18/15.
 */
public class NotesDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "notes";
    public static final String CONTENT = "content";
    public static final String DETAILS = "details";
    public static final String PATH = "path";
    public static final String AUDIOPATH = "audio_path";
    public static final String ISCHECKED = "isChecked";
    public static final String ID = "_id";
    public static final String TIME = "time";

    public NotesDB(Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID
                + " STRING PRIMARY KEY," + CONTENT
                + " TEXT NOT NULL," + DETAILS
                + " TEXT NOT NULL," + PATH
                + " TEXT NOT NULL," + AUDIOPATH
                + " TEXT NOT NULL," + ISCHECKED
                + " INTEGER," + TIME + " TEXT NOT NULL)");





        ContentValues cv = new ContentValues();
        cv.put(NotesDB.ID, "0");
        cv.put(NotesDB.CONTENT, "This is your first memo");
        cv.put(NotesDB.DETAILS, "Now there's nothing yet, you can tap this note to edit or press the + button to add one memo. Remember, only when you check your memo done this note will show color on the list. ");
        cv.put(NotesDB.TIME, getTime());
        cv.put(NotesDB.ISCHECKED, 1);

        cv.put(NotesDB.AUDIOPATH, "null");

        cv.put(NotesDB.PATH, "null");

        db.insert(NotesDB.TABLE_NAME, null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss ");
        Date curDate = new Date();
        return format.format(curDate);
    }

}

