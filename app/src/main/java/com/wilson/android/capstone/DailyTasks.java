package com.wilson.android.capstone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wilson.android.capstone.database.TaskBaseHelper;
import com.wilson.android.capstone.database.TaskCursorWrapper;
import com.wilson.android.capstone.database.TaskDbSchema.TaskTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class DailyTasks {
    private static DailyTasks sTasksCompleted;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DailyTasks get(Context context){
        if(sTasksCompleted == null){
            sTasksCompleted = new DailyTasks(context);
        }
        return sTasksCompleted;
    }

    private DailyTasks(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();
    }

    public void addTask(Task task){
        ContentValues values = getContentValues(task);

        mDatabase.insert(TaskTable.TABLE_NAME, null, values);
    }

    public void updateTask(Task task){
        String uuid = task.getId().toString();
        ContentValues values = getContentValues(task);

        mDatabase.update(TaskTable.TABLE_NAME, values,
                TaskTable.Cols.UUID + " = ?",
                new String[]{uuid});
    }

    public Task getTask(UUID id){
        Task task;

        TaskCursorWrapper cursor = queryTasks(
                TaskTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            task = cursor.getTask();
        }
        finally{
            cursor.close();
        }

        return task;

    }

    public List<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<Task>();

        TaskCursorWrapper cursor = queryTasks(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return tasks;
    }

    private static ContentValues getContentValues(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskTable.Cols.UUID, task.getId().toString());
        contentValues.put(TaskTable.Cols.TITLE, task.getTaskName());
        contentValues.put(TaskTable.Cols.DATE, task.getDateCompleted().toString());
        contentValues.put(TaskTable.Cols.COMPLETED, task.getIsCompleted() ? 1 : 0);

        return contentValues;
    }

    private TaskCursorWrapper queryTasks(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                TaskTable.TABLE_NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new TaskCursorWrapper(cursor);
    }


}
