package site.gbdev.walkandgoal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import site.gbdev.walkandgoal.models.Goal;

/**
 * Created by gavin on 12/03/2017.
 */

public class FitnessDbWrapper {

    public static int addAGoal(Goal goal, Context context){

        SQLiteDatabase db = getWritableDatabase(context);

        ContentValues values = new ContentValues();
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_NAME, goal.getName());
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE, goal.getDistance());
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_UNIT, goal.getUnit());
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_DATE, goal.getDate().getTime());

        // Insert the new row, returning the primary key value of the new row
        int id = (int) db.insert(FitnessContract.GoalEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public static int setActive(Goal goal, Context context){

        SQLiteDatabase db = getWritableDatabase(context);

        db.execSQL("UPDATE " + FitnessContract.GoalEntry.TABLE_NAME + " SET " + FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE + " = 0");

        ContentValues values = new ContentValues();
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE, 1);

        String selection = FitnessContract.GoalEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(goal.getId())};

        int count = db.update(
                FitnessContract.GoalEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();
        return count;
    }

    public static Goal getActiveGoal(Context context){

        SQLiteDatabase db = getReadableDatabase(context);

        String[] projection = {
                FitnessContract.GoalEntry.COLUMN_NAME_ID,
                FitnessContract.GoalEntry.COLUMN_NAME_NAME,
                FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE,
                FitnessContract.GoalEntry.COLUMN_NAME_UNIT,
                FitnessContract.GoalEntry.COLUMN_NAME_DATE
        };

        String selection = FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE + " = ?";
        String[] selectionArgs = {String.valueOf(1)};

        String sortOrder =
                FitnessContract.GoalEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                FitnessContract.GoalEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        Goal goal = null;
        while(cursor.moveToNext()) {
            int goalId = cursor.getInt(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_ID));
            String goalName = cursor.getString(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_NAME));
            double goalDistance = cursor.getDouble(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE));
            int goalUnit = cursor.getInt(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_UNIT));
            Date goalDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DATE))*1000);
            goal = new Goal(goalId, goalName, goalDistance, goalUnit, goalDate);
        }
        cursor.close();
        db.close();
        return goal;
    }

    public static List<Goal> getAllInactiveGoals(Context context) {
        SQLiteDatabase db = getReadableDatabase(context);

        String[] projection = {
                FitnessContract.GoalEntry.COLUMN_NAME_ID,
                FitnessContract.GoalEntry.COLUMN_NAME_NAME,
                FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE,
                FitnessContract.GoalEntry.COLUMN_NAME_UNIT,
                FitnessContract.GoalEntry.COLUMN_NAME_DATE
        };

        String selection = FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE + " = ?";
        String[] selectionArgs = {String.valueOf(0)};

        String sortOrder =
                FitnessContract.GoalEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                FitnessContract.GoalEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Goal> goals = new ArrayList<>();
        while(cursor.moveToNext()) {
            int goalId = cursor.getInt(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_ID));
            String goalName = cursor.getString(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_NAME));
            double goalDistance = cursor.getDouble(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE));
            int goalUnit = cursor.getInt(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_UNIT));
            Date goalDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DATE))*1000);
            goals.add(new Goal(goalId, goalName, goalDistance, goalUnit, goalDate));
        }
        cursor.close();

        db.close();
        return goals;
    }

    private static SQLiteDatabase getReadableDatabase(Context context){

        FitnessDbHelper mDbHelper = new FitnessDbHelper(context);

        return mDbHelper.getReadableDatabase();
    }

    private static SQLiteDatabase getWritableDatabase(Context context){

        FitnessDbHelper mDbHelper = new FitnessDbHelper(context);

        return mDbHelper.getWritableDatabase();
    }
}
