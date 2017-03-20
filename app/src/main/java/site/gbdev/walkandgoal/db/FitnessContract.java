package site.gbdev.walkandgoal.db;

import android.provider.BaseColumns;

/**
 * Created by gavin on 12/03/2017.
 */

public class FitnessContract {

    public static final  int    DATABASE_VERSION = 6;
    public static final  String DATABASE_NAME = "fitness.db";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String DATE_TYPE = " DATE";
    private static final String COMMA_SEP = ",";

    private FitnessContract(){}

    public static class GoalEntry implements BaseColumns {
        public static final String TABLE_NAME = "goal";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_UNIT = "unit";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_ACTIVE = "active";
        public static final String COLUMN_NAME_FINISHED = "finished";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_DISTANCE + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_UNIT + INTEGER_TYPE + COMMA_SEP +
                COLUMN_NAME_ACTIVE + INTEGER_TYPE + " DEFAULT 0" + COMMA_SEP +
                COLUMN_NAME_FINISHED + INTEGER_TYPE + " DEFAULT 0" + COMMA_SEP +
                COLUMN_NAME_DATE + DATE_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ActivityEntry implements BaseColumns {
        public static final String TABLE_NAME = "activity";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_DATE = "date";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DISTANCE + REAL_TYPE + COMMA_SEP +
                COLUMN_NAME_DATE + DATE_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
