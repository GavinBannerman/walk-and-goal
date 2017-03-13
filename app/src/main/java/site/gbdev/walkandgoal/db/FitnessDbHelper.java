package site.gbdev.walkandgoal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gavin on 12/03/2017.
 */

public class FitnessDbHelper extends SQLiteOpenHelper {

    public FitnessDbHelper(Context context) {
        super(context, FitnessContract.DATABASE_NAME, null, FitnessContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FitnessContract.ActivityEntry.CREATE_TABLE);
        db.execSQL(FitnessContract.GoalEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(FitnessContract.ActivityEntry.DELETE_TABLE);
        db.execSQL(FitnessContract.GoalEntry.DELETE_TABLE);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
