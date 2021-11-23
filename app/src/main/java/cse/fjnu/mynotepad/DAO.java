package cse.fjnu.mynotepad;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAO {
    private static final String TestApp="TestApp";
    private DatabaseHelper mhelper;
    private static final String TABLE_NAME="Notes";
    private static final String DATABASE_NAME="NotePad";
    public DAO(Context context){
        //创建数据库
        mhelper = new DatabaseHelper(context);
    }
//    id integer,context varchar,Title varchar,CreateTime Time
    public void insert(Note oneNote){
        SQLiteDatabase db = mhelper.getWritableDatabase();
        System.out.println("拿到数据库"+db);
        //添加数据
        //1.写好sql语句  insert into table_name (id,GradeUnit,Unit,English,Chinese)
        //2.将sql语句交给SQLiteDatabase.execSQL()执行
        //3.将SQLiteDatabase资源关闭
        //String sql = "insert into "+ Database.TABLE_NAME + " (id,GradeUnit,Unit,English,Chinese) values (?,?,?,?,?)";
        //db.execSQL(sql,new Object[]{id,GradeUnit,Unit,En,Ch});

        //API插入数据

        ContentValues values = new ContentValues();
        values.put("id",oneNote.getId());
        values.put("context",oneNote.getContext());
        values.put("Title",oneNote.getTitle());
        values.put("CreateTime",oneNote.getCreateTime());
        db.insert(TABLE_NAME,null,values);
        System.out.println("插入数据库成功");
        db.close();
    }

    /**
     * 查询数据，得到数据库中id为‘id’的所有数据
     * @param
     */

    public List<Note> queryAll(){

        SQLiteDatabase db = mhelper.getReadableDatabase();
        System.out.println("拿到数据库"+db);

        /**
         * @param String[] columns 要提取的关键字键值key
         * @param String selection 查询条件的键值key
         * @param String[] selectionArgs 查询条件的value值
         * 相当于select String[] columns from table_name where String selection = "selectinoArgs"
         */

//        String[] columns = new String[]{"id","context","Title","CreateTime"};
//        String selection = "id =?";
//        String[] selectionArgs =  {String.valueOf(id)};
//        Cursor cursor= db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
//        //获取得到的所有笔记

        String getAll= "select * from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(getAll,null);


        List<Note> getNotes = new ArrayList<Note>();
        if(cursor.moveToFirst()){
         //   System.out.println("id为"+id+"的表里是有数据的");
        //输出总共有多少列数据即有多少本笔记
            System.out.println("总共有"+cursor.getCount()+"条数据");
            int   getId=Integer.parseInt(cursor.getString(0));
            String getContext=cursor.getString(1);
            String getTitle=cursor.getString(2);
            String getTime=cursor.getString(3);
            Note oneNote=new Note(getId,getContext,getTitle,getTime);
            getNotes.add(oneNote);
            //System.out.println("id为:"+getId+" context为："+getContext+" title为："+getTitle+" createTime为:"+getTime);
           // System.out.println(oneNote);
            while(cursor.moveToNext()){
                 getId=Integer.parseInt(cursor.getString(0));
                 getContext=cursor.getString(1);
                 getTitle=cursor.getString(2);
                 getTime=cursor.getString(3);
                oneNote=new Note(getId,getContext,getTitle,getTime);
                getNotes.add(oneNote);
                //System.out.println(oneNote);
            }
            Log.d(TestApp, "查找数据库成功");
            cursor.close();
            db.close();
            return getNotes;
        }
        else{
            System.out.println("表里是没有数据的");
            cursor.close();
            db.close();
            return null;
        }





    }

    /**
     * 对笔记进行修改
     * @param id 笔记的id
     * @param context 笔记的新内容
     * @param title 笔记的新title
     */
    public void change(int id,String context,String title){
        SQLiteDatabase db = mhelper.getReadableDatabase();
        System.out.println("拿到数据库"+db);
        ContentValues values = new ContentValues();
        values.put("context",context);
        values.put("title",title);
        String condition ="id = ?";
        String[] args = {String.valueOf(id)};
        db.update(TABLE_NAME,values,condition,args);
        db.close();
        System.out.println("修改成功");
    }

    public void delete(Note note){
        SQLiteDatabase db = mhelper.getWritableDatabase();
        //删除数据
        //1.写好sql语句 delete from table_name where conditions
        //2.将sql语句交给SQLiteDatabase.execSQL()执行
        //3.将SQLiteDatabase资源关闭
        String sql = "delete from " + TABLE_NAME +" where id =" + note.getId();
        db.execSQL(sql);
        db.close();
    }




    public boolean deleteDatabase(Context context)
    // 删除当前获得的数据库，如果删除成功的话返回true
    {
        return context.deleteDatabase(DATABASE_NAME);
    }


}
