package cse.fjnu.mynotepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG="DatabaseHelper";
    private static final String TABLE_NAME="Notes";
    private static final String DATABASE_NAME="NotePad";
    public DatabaseHelper(Context context) {
        //        版本为1
        super(context, DATABASE_NAME, null,1,null);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//OnCreate 方法；创建时回调
        Log.d(TAG,"创建数据库");
        //sql : create table table_name(id integer,GradeUnit integer,Unit integer,English varchar, Chinese varchar)
        //Database.TABLE_NAME = "vocabulary"  是在常量文件中写好的
        String sql ="create table "+TABLE_NAME+" (id integer,context varchar,Title varchar,CreateTime varchar)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //OnUpdate方法 更新数据库时，回调
        Log.d(TAG,"更新数据库");
        //添加字段
        //alter table table_name add UNIT integer;
        String sql;

        //判断要采用的版本，进行修改
        switch (i){
            case 1:
                //版本一 则需要添加GradeUnit 和 Unit 字段
                //注意sql语句不要写错，注意sql语法
                sql = "alter table " + TABLE_NAME + " add id integer";
                sqLiteDatabase.execSQL(sql);
                sql = "alter table " + TABLE_NAME + " add context String";
                sqLiteDatabase.execSQL(sql);
                sql = "alter table " + TABLE_NAME + " add Title String";
                sqLiteDatabase.execSQL(sql);
                sql = "alter table " + TABLE_NAME + " add CreateTime String";
                sqLiteDatabase.execSQL(sql);
                break;
            case 2:
                //版本2 添加Title这个字段
                sql = "alter table " +TABLE_NAME + " add Title varchar(10)";
                sqLiteDatabase.execSQL(sql);
                break;
            case 3:

                break;
        }

    }

}
