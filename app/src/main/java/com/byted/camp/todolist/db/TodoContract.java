package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {


    private TodoContract() {
    }

    public static class TodoEntry implements BaseColumns{
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_PRIORTY = "priority";//0-3优先级由低到高
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_STATE = "state";//1为已完成，0为未完成
    }

    // TODO 定义表结构和 SQL 语句常量
    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoEntry.TABLE_NAME
                    +" (" +TodoEntry._ID + " INTEGER PRIMARY KEY autoincrement,"
                    +TodoEntry.COLUMN_CONTENT + " TEXT,"
                    +TodoEntry.COLUMN_DATE + " TEXT,"
                    +TodoEntry.COLUMN_STATE + " TEXT,"
                    +TodoEntry.COLUMN_PRIORTY+ " INTEGER )";

    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TodoEntry.TABLE_NAME;


}
