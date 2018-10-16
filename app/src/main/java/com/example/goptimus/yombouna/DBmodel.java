package com.example.goptimus.yombouna;

import android.provider.BaseColumns;

public class DBmodel {

    private DBmodel() {}

    public static class DBCOLUMN implements BaseColumns {
        public static final String TABLE_NAME = "PHONEINFO";
        public static final String COLUMN_NAME_IMEI = "imei";
    }
}
