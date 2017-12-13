package com.wilson.android.capstone.database;

/**
 * Created by Home on 5/30/2016.
 */
public class TaskDbSchema {

    public static final class TaskTable {
        public static final String TABLE_NAME = "Tasks";

        public static final class Cols {
            public static final String UUID = "UUID";
            public static final String DATE = "Date";
            public static final String TITLE = "Task";
            public static final String COMPLETED = "Completed";
        }
    }
}
