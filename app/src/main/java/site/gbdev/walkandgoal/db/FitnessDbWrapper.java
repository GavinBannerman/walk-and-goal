package site.gbdev.walkandgoal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import site.gbdev.walkandgoal.models.Goal;
import site.gbdev.walkandgoal.models.Units;

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

        // Insert the new row, returning the primary key value of the new row
        int id = (int) db.insert(FitnessContract.GoalEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public static double getTotalActivityForDate(Units.Unit unit, Date date, Context context){

        if (date == null){
            date = new Date();
        }

        SQLiteDatabase db = getWritableDatabase(context);
        String[] selectionArgs = {String.valueOf(getStartOfDay(date).getTime()), String.valueOf(getEndOfDay(date).getTime())};

        Cursor cursor = db.rawQuery(
                "SELECT SUM(" + FitnessContract.ActivityEntry.COLUMN_NAME_DISTANCE + ") AS 'total' " +
                        "FROM " + FitnessContract.ActivityEntry.TABLE_NAME +
                        " WHERE " + FitnessContract.ActivityEntry.COLUMN_NAME_DATE + " >= ? AND "
                        + FitnessContract.ActivityEntry.COLUMN_NAME_DATE + " < ?;",
                selectionArgs                            // The values for the WHERE clause
        );

        double total = 0;

        while(cursor.moveToNext()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }

        cursor.close();
        db.close();
        return (total * unit.getConversion());
    }

    public static List<Double> getActivityForHistory(Units.Unit unit, Context context, List<Goal> goals){

        SQLiteDatabase db = getReadableDatabase(context);
        List<Double> progress = new ArrayList<>();

        for (Goal goal : goals) {
            String[] selectionArgs = {String.valueOf(getStartOfDay(goal.getDate()).getTime()), String.valueOf(getEndOfDay(goal.getDate()).getTime())};
            Cursor cursor = db.rawQuery(
                    "SELECT SUM(" + FitnessContract.ActivityEntry.COLUMN_NAME_DISTANCE + ") AS 'total' " +
                            "FROM " + FitnessContract.ActivityEntry.TABLE_NAME +
                            " WHERE " + FitnessContract.ActivityEntry.COLUMN_NAME_DATE + " >= ? AND "
                            + FitnessContract.ActivityEntry.COLUMN_NAME_DATE + " < ?;",
                    selectionArgs                            // The values for the WHERE clause
            );

            double total = 0;

            while (cursor.moveToNext()) {
                total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            }

            cursor.close();
            progress.add(total * unit.getConversion());
        }
        db.close();
        return progress;
    }

    public static List<Double> getDailyActivityForRange(Units.Unit unit, Date fromDate, Date toDate, Context context){

        SQLiteDatabase db = getReadableDatabase(context);
        List<Double> progress = new ArrayList<>();

        Cursor cursor;

        if (fromDate == null && toDate == null){

            cursor = db.rawQuery(
                    "SELECT date((" + FitnessContract.ActivityEntry.COLUMN_NAME_DATE + "/1000), 'unixepoch') AS 'groupdate', SUM(" + FitnessContract.ActivityEntry.COLUMN_NAME_DISTANCE + ") AS 'total' " +
                            "FROM " + FitnessContract.ActivityEntry.TABLE_NAME +
                            " GROUP BY groupdate;",
                    null                            // The values for the WHERE clause
            );

        } else {

            if (toDate == null){
                toDate = new Date();
            }

            String[] selectionArgs = {String.valueOf(getStartOfDay(fromDate).getTime()), String.valueOf(getEndOfDay(toDate).getTime())};

            cursor = db.rawQuery(
                    "SELECT date((" + FitnessContract.ActivityEntry.COLUMN_NAME_DATE + "/1000), 'unixepoch') AS 'groupdate', SUM(" + FitnessContract.ActivityEntry.COLUMN_NAME_DISTANCE + ") AS 'total' " +
                            "FROM " + FitnessContract.ActivityEntry.TABLE_NAME +
                            " WHERE " + FitnessContract.ActivityEntry.COLUMN_NAME_DATE + " >= ? AND "
                            + FitnessContract.ActivityEntry.COLUMN_NAME_DATE + " < ?" +
                            " GROUP BY groupdate;",
                    selectionArgs                            // The values for the WHERE clause
            );
        }

        double total = 0;

        while(cursor.moveToNext()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            progress.add(total * unit.getConversion());
        }

        cursor.close();
        db.close();

        return progress;
    }

    public static int addActivity(double distance, Date date, Context context){

        if (date == null){
            date = new Date();
        }

        SQLiteDatabase db = getWritableDatabase(context);

        ContentValues values = new ContentValues();
        values.put(FitnessContract.ActivityEntry.COLUMN_NAME_DISTANCE, distance);
        values.put(FitnessContract.ActivityEntry.COLUMN_NAME_DATE, date.getTime());

        // Insert the new row, returning the primary key value of the new row
        int id = (int) db.insert(FitnessContract.ActivityEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public static int setActive(Goal goal, Date date, Context context){

        if (date == null){
            date = new Date();
        }

        SQLiteDatabase db = getWritableDatabase(context);

        db.execSQL("UPDATE " + FitnessContract.GoalEntry.TABLE_NAME + " SET " + FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE + " = 0");

        ContentValues values = new ContentValues();
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE, 1);
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_DATE, date.getTime());

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

    public static int setFinished(Goal goal, Date date, Context context){

        if (date == null){
            date = new Date();
        }

        SQLiteDatabase db = getWritableDatabase(context);

        db.execSQL("UPDATE " + FitnessContract.GoalEntry.TABLE_NAME + " SET " + FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE + " = 0");

        ContentValues values = new ContentValues();
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE, 0);
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_DATE, date.getTime());
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_FINISHED, 1);

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

    public static int updateGoal(Goal goal, Context context){

        SQLiteDatabase db = getWritableDatabase(context);

        ContentValues values = new ContentValues();
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_NAME, goal.getName());
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE, goal.getDistance());
        values.put(FitnessContract.GoalEntry.COLUMN_NAME_UNIT, goal.getUnit());

        String selection = FitnessContract.GoalEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(goal.getId()) };

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

    public static void deleteGoal(Goal goal, Context context){

        SQLiteDatabase db = getWritableDatabase(context);

        String selection = FitnessContract.GoalEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(goal.getId())};
        db.delete(FitnessContract.GoalEntry.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    public static List<Goal> getAllInactiveUnfinishedGoals(Context context) {
        SQLiteDatabase db = getReadableDatabase(context);

        String[] projection = {
                FitnessContract.GoalEntry.COLUMN_NAME_ID,
                FitnessContract.GoalEntry.COLUMN_NAME_NAME,
                FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE,
                FitnessContract.GoalEntry.COLUMN_NAME_UNIT,
                FitnessContract.GoalEntry.COLUMN_NAME_DATE
        };

        String selection = FitnessContract.GoalEntry.COLUMN_NAME_ACTIVE + " = ? AND " + FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
        String[] selectionArgs = {String.valueOf(0), String.valueOf(0)};

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
            Date goalDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DATE)));
            goals.add(new Goal(goalId, goalName, goalDistance, goalUnit, goalDate));
        }
        cursor.close();

        db.close();
        return goals;
    }

    public static List<Goal> getAllFinishedGoals(Units.Unit unit, Date fromDate, Date toDate, Context context) {
        SQLiteDatabase db = getReadableDatabase(context);

        String[] projection = {
                FitnessContract.GoalEntry.COLUMN_NAME_ID,
                FitnessContract.GoalEntry.COLUMN_NAME_NAME,
                FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE,
                FitnessContract.GoalEntry.COLUMN_NAME_UNIT,
                FitnessContract.GoalEntry.COLUMN_NAME_DATE
        };

        String selection = null;
        String[] selectionArgs = null;

        if (fromDate == null && toDate == null) {

            selection = FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
            selectionArgs = new String[]{String.valueOf(1)};
        } else if (toDate == null){
            selection = FitnessContract.GoalEntry.COLUMN_NAME_DATE + " > ?" + " AND " + FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
            selectionArgs = new String[]{String.valueOf(fromDate.getTime()), String.valueOf(1)};
        } else {
            selection = "(" + FitnessContract.GoalEntry.COLUMN_NAME_DATE + " BETWEEN ? AND ? ) AND " + FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
            selectionArgs = new String[]{String.valueOf(fromDate.getTime()), String.valueOf(toDate.getTime()), String.valueOf(1)};
        }

        String sortOrder =
                FitnessContract.GoalEntry.COLUMN_NAME_DATE + " DESC";

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
            int goalUnit = Units.getIdFromString(unit.getName());
            Date goalDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DATE)));
            goals.add(new Goal(goalId, goalName, goalDistance, goalUnit, goalDate));
        }
        cursor.close();

        db.close();
        return goals;
    }

    public static List<Goal> getAllFinishedGoalsGroupedByName(Units.Unit unit, Date fromDate, Date toDate, Context context) {
        SQLiteDatabase db = getReadableDatabase(context);

        String[] projection = {
                FitnessContract.GoalEntry.COLUMN_NAME_ID,
                FitnessContract.GoalEntry.COLUMN_NAME_NAME,
                FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE,
                FitnessContract.GoalEntry.COLUMN_NAME_UNIT,
                FitnessContract.GoalEntry.COLUMN_NAME_DATE
        };

        String selection = null;
        String[] selectionArgs = null;

        if (fromDate == null && toDate == null) {

            selection = FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
            selectionArgs = new String[]{String.valueOf(1)};
        } else if (toDate == null){
            selection = FitnessContract.GoalEntry.COLUMN_NAME_DATE + " > ?" + " AND " + FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
            selectionArgs = new String[]{String.valueOf(fromDate.getTime()), String.valueOf(1)};
        } else {
            selection = "(" + FitnessContract.GoalEntry.COLUMN_NAME_DATE + " BETWEEN ? AND ? ) AND " + FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
            selectionArgs = new String[]{String.valueOf(fromDate.getTime()), String.valueOf(toDate.getTime()), String.valueOf(1)};
        }

        String sortOrder =
                FitnessContract.GoalEntry.COLUMN_NAME_NAME + " DESC";

        String groupBy = FitnessContract.GoalEntry.COLUMN_NAME_NAME;

        Cursor cursor = db.query(
                FitnessContract.GoalEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                groupBy,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        List<Goal> goals = new ArrayList<>();
        while(cursor.moveToNext()) {
            int goalId = cursor.getInt(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_ID));
            String goalName = cursor.getString(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_NAME));
            double goalDistance = cursor.getDouble(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE));
            int goalUnit = Units.getIdFromString(unit.getName());
            Date goalDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DATE)));
            goals.add(new Goal(goalId, goalName, goalDistance, goalUnit, goalDate));
        }
        cursor.close();

        db.close();
        return goals;
    }

    public static List<Goal> getAllFinishedGoalsWithName(String name, Units.Unit unit, Date fromDate, Date toDate, Context context) {
        SQLiteDatabase db = getReadableDatabase(context);

        String[] projection = {
                FitnessContract.GoalEntry.COLUMN_NAME_ID,
                FitnessContract.GoalEntry.COLUMN_NAME_NAME,
                FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE,
                FitnessContract.GoalEntry.COLUMN_NAME_UNIT,
                FitnessContract.GoalEntry.COLUMN_NAME_DATE
        };

        String selection = null;
        String[] selectionArgs = null;

        if (fromDate == null && toDate == null) {

            selection = FitnessContract.GoalEntry.COLUMN_NAME_NAME + " = ? AND " + FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
            selectionArgs = new String[]{name, String.valueOf(1)};
        } else if (toDate == null){
            selection = "(" + FitnessContract.GoalEntry.COLUMN_NAME_NAME + " = ? AND " + FitnessContract.GoalEntry.COLUMN_NAME_DATE + " > ? )" + " AND " + FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
            selectionArgs = new String[]{name, String.valueOf(fromDate.getTime()), String.valueOf(1)};
        } else {
            selection = "(" + FitnessContract.GoalEntry.COLUMN_NAME_DATE + " BETWEEN ? AND ? ) AND (" + FitnessContract.GoalEntry.COLUMN_NAME_NAME + " = ? AND " + FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ? )";
            selectionArgs = new String[]{String.valueOf(fromDate.getTime()), String.valueOf(toDate.getTime()), name, String.valueOf(1)};
        }

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
            int goalUnit = Units.getIdFromString(unit.getName());
            Date goalDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DATE)));
            goals.add(new Goal(goalId, goalName, goalDistance, goalUnit, goalDate));
        }
        cursor.close();

        db.close();
        return goals;
    }

    public static List<Goal> getAllGoals(Date fromDate, Date toDate, Context context) {
        SQLiteDatabase db = getReadableDatabase(context);

        String[] projection = {
                FitnessContract.GoalEntry.COLUMN_NAME_ID,
                FitnessContract.GoalEntry.COLUMN_NAME_NAME,
                FitnessContract.GoalEntry.COLUMN_NAME_DISTANCE,
                FitnessContract.GoalEntry.COLUMN_NAME_UNIT,
                FitnessContract.GoalEntry.COLUMN_NAME_DATE
        };

        String selection = null;
        String[] selectionArgs = null;

        if (fromDate == null && toDate == null) {

            selection = null;
            selectionArgs = null;
        } else if (toDate == null){
            selection = FitnessContract.GoalEntry.COLUMN_NAME_DATE + " > ?";
            selectionArgs = new String[]{String.valueOf(fromDate.getTime())};
        } else {
            selection = FitnessContract.GoalEntry.COLUMN_NAME_DATE + " BETWEEN ? AND ?";
            selectionArgs = new String[]{String.valueOf(fromDate.getTime()), String.valueOf(toDate.getTime())};
        }

        String sortOrder =
                FitnessContract.GoalEntry.COLUMN_NAME_DATE + " DESC";

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
            Date goalDate = new Date(cursor.getLong(cursor.getColumnIndexOrThrow(FitnessContract.GoalEntry.COLUMN_NAME_DATE)));
            goals.add(new Goal(goalId, goalName, goalDistance, goalUnit, goalDate));
        }
        cursor.close();

        db.close();
        return goals;
    }

    public static void deleteHistory(Context context){

        SQLiteDatabase db = getWritableDatabase(context);

        String selection = FitnessContract.GoalEntry.COLUMN_NAME_FINISHED + " = ?";
        String[] selectionArgs = { String.valueOf(1)};
        db.delete(FitnessContract.GoalEntry.TABLE_NAME, selection, selectionArgs);

        String selection2 = FitnessContract.ActivityEntry.COLUMN_NAME_DATE + " < ?";
        String[] selectionArgs2 = { String.valueOf(getStartOfDay(new Date()).getTime())};
        db.delete(FitnessContract.ActivityEntry.TABLE_NAME, selection2, selectionArgs2);

        db.close();
    }

    private static SQLiteDatabase getReadableDatabase(Context context){

        FitnessDbHelper mDbHelper = new FitnessDbHelper(context);

        return mDbHelper.getReadableDatabase();
    }

    private static SQLiteDatabase getWritableDatabase(Context context){

        FitnessDbHelper mDbHelper = new FitnessDbHelper(context);

        return mDbHelper.getWritableDatabase();
    }

    private static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    private static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
