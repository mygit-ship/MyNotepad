package cse.fjnu.mynotepad;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SqlLiteTest extends AppCompatActivity {
    List<Note>  getNotes;
    int id_Note=0;
    String nowsort="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_lite_test);

        ListView listview=findViewById(R.id.listview);
        //给所有图标放上图标
        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        TextView add_note=findViewById(R.id.add_note);
        TextView search_note=findViewById(R.id.search_note);
        TextView sort_note=findViewById(R.id.sort_note);
        TextView help=findViewById(R.id.help);
        TextView settings=findViewById(R.id.settings);
        add_note.setTypeface(font);
        add_note.setText(getResources().getString(R.string.add_note));
        search_note.setTypeface(font);
        search_note.setText(getResources().getString(R.string.search_note));
        sort_note.setTypeface(font);
        sort_note.setText(getResources().getString(R.string.sort_note));
        help.setTypeface(font);
        help.setText(getResources().getString(R.string.help));
        settings.setTypeface(font);
        settings.setText(getResources().getString(R.string.settings));

//给listview放上数据
        //获得所有note

        DAO dao=new DAO(getApplicationContext());
        if(dao.queryAll()==null){
            System.out.println("目前没有东西");
            id_Note+=1;
            Note aaNote=new Note(id_Note,"初始测试用的context","初始测试用的title");
            dao.insert(aaNote);
        }
        getNotes=dao.queryAll();
        for (int i = 0; i < getNotes.size(); i++) {
            //Note Note = (Note) getNotes.get(i);
            id_Note=getNotes.get(i).id;
            System.out.println(getNotes.get(i));
        }
        freshList(getNotes);
    /**
     * 给addnote添加点击事件（请求code为3，返回code也为3）
     */
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //                获取查询到的所有笔记封装到这个list
                Intent myIntent = new Intent();
                transferList transferGetNotes=new transferList(getNotes);
                myIntent.putExtra("getNotes_from_index", (Serializable) transferGetNotes);

                //设置跳转的页面并接收返回的笔记列表
                myIntent.setClass(SqlLiteTest.this, addNote.class);
                startActivityForResult(myIntent, 3);

            }
        });

    /**
     * 给search_note添加点击事件
      */

        search_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                transferList transferGetNotes=new transferList(getNotes);
                myIntent.putExtra("getNotes_from_index", (Serializable) transferGetNotes);

                //设置跳转的页面
                myIntent.setClass(SqlLiteTest.this, searchedNotes.class);
//                startActivity(myIntent);
                startActivityForResult(myIntent,8);
            }
        });


        listview.setOnItemClickListener(new  AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position,long  id) {
                System.out.println("position 是"+position);
                System.out.println("parent 是"+parent);
                System.out.println("view 是"+view);
                System.out.println("id 是"+id);
                //得到所有笔记
                Intent myIntent = new Intent();
                myIntent.putExtra("getNotes_from_indexto_checknote", (Serializable) getNotes.get((int) id));
                //设置跳转的页面
                myIntent.setClass(SqlLiteTest.this, checkNote.class);

                startActivityForResult(myIntent,4);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note=getNotes.get(i);
                if(note==null){
                    Toast.makeText(SqlLiteTest.this, "删除失败", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    dao.delete(note);
                    getNotes.remove(note);
                    freshList(getNotes);
                    Toast.makeText(SqlLiteTest.this, "删除成功", Toast.LENGTH_SHORT).show();
                    return true;
                }

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type="settings";
                showDialogWindow(type);

            }
        });
        sort_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type="sort";
                showDialogWindow(type);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("你点击了一下help");
                String type="help";
                showDialogWindow(type);
            }
        });
    }

    /**
     * 刷新本页面的listview
     */
    public  void freshList(List<Note> getNotes){
        ListView listView=findViewById(R.id.listview);
        if(getNotes==null){
            Toast.makeText(SqlLiteTest.this, "目前没有任何笔记！！", Toast.LENGTH_SHORT).show();
            listView.setAdapter(null);
        }
        else{
            List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();


            Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
            if(nowsort.equals("sortTitleUp")){
                sortListUp(getNotes);
            }
            else if(nowsort.equals("sortTitleDown")){
                sortListDown(getNotes);
            }
            for(Note note : getNotes){
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("id", note.getId());
                item.put("context", note.getContext());
                item.put("title", note.getTitle());
                item.put("createTime", note.getCreateTime());

                data.add(item);
            }

            //创建SimpleAdapter适配器将数据绑定到item显示控件上
            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_listview_item,
                    new String[]{"title", "createTime"}, new int[]{R.id.title, R.id.createTime});
            //实现列表的显示

            listView.setAdapter(adapter);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Note> getListfrom_addNote=new ArrayList<Note>();
        //本页面去往addnote页面后返回的数据
        if (requestCode == 3 && resultCode == 3) {
            System.out.println("拿到那边发过来的list了");
            //得到传送来的List

            transferList list_from_addNote= (transferList) data.getSerializableExtra("addNote_listToIndex");

            getNotes=list_from_addNote.getNotes();

//            DAO dao=new DAO(getApplicationContext());
//            getListfrom_addNote=dao.queryAll();
            //刷新该页面
            freshList(getNotes);

        }
        //本页面去往checknote页面后返回的数据
        else if (requestCode == 4 && resultCode == 4) {
            System.out.println("拿到checknote发过来的note了");
            Note note= (Note) data.getSerializableExtra("checkNote_listToIndex");
           for(int i=0;i<getNotes.size();i++){
               if(getNotes.get(i).getId()==note.getId()){
                   getNotes.set(i,note);
               }
           }
//            DAO dao=new DAO(getApplicationContext());
//            getListfrom_addNote=dao.queryAll();
            //刷新该页面
            freshList(getNotes);
        }
        //本页面去往searchednote页面后返回的数据
        else if(requestCode == 8 && resultCode == 8){
            System.out.println("接收到来自本页面去往searchednote页面后返回的数据");
            id_Note=0;
            DAO dao=new DAO(getApplicationContext());
            if(dao.queryAll()==null){
                System.out.println("目前没有东西");
                id_Note+=1;
                Note aaNote=new Note(id_Note,"初始测试用的context","初始测试用的title");
                dao.insert(aaNote);
            }
            getNotes=dao.queryAll();
            for (int i = 0; i < getNotes.size(); i++) {
                //Note Note = (Note) getNotes.get(i);

                id_Note=getNotes.get(i).id;
                System.out.println(getNotes.get(i));
            }
            freshList(getNotes);
        }

    }

    public void showDialogWindow(String type){
        //help窗口
        if(type.equals("help")){
            AlertDialog.Builder builder = new AlertDialog.Builder(SqlLiteTest.this);
            builder.setTitle("温馨提示");
            builder.setIcon(R.drawable.giao);
            String message="单击笔记本可以进入编辑页面\n长按笔记本即为删除笔记\n单击sort可以对笔记本的展示顺序进行排序(标题首字升序或者降序)\n单击settings可以对笔记本的背景进行修改";
            builder.setMessage(message);
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                /**
                 *
                 * @param arg0
                 * @param arg1
                 */
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(SqlLiteTest.this, "你点击了确定", Toast.LENGTH_SHORT).show();
                }
            });

            builder.show();
        }
        //sort窗口
        else if(type.equals("sort")){
            View view= LayoutInflater.from(SqlLiteTest.this).inflate(R.layout.activity_sort_win,null);
            AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
//            AlertDialog.Builder builder = new AlertDialog.Builder(SqlLiteTest.this);
//            builder.setTitle("分类");
//            builder.setIcon(R.drawable.giao);
//            String message="还没有完成sort功能";
//            builder.setMessage(message);

            TextView upTitleSort=view.findViewById(R.id.upTitleSort);
            TextView downTitleSort=view.findViewById(R.id.downTitleSort);
            upTitleSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sortListUp(getNotes);
                    freshList(getNotes);
                }
            });
            downTitleSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sortListDown(getNotes);
                    freshList(getNotes);
                }
            });
            dialog.show();
        }
        //settings窗口
        else if(type.equals("settings")){
            View view= LayoutInflater.from(SqlLiteTest.this).inflate(R.layout.activity_settings_style,null);
             AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();

            TextView color0=view.findViewById(R.id.backgroundColor0);
            TextView color1=view.findViewById(R.id.backgroundColor1);
            TextView color2=view.findViewById(R.id.backgroundColor2);
            TextView color3=view.findViewById(R.id.backgroundColor3);
            TextView color4=view.findViewById(R.id.backgroundColor4);
            TextView color5=view.findViewById(R.id.backgroundColor5);
            TextView color6=view.findViewById(R.id.backgroundColor6);
            color0.setOnClickListener(new View.OnClickListener() {
                String bc="";
                @Override
                public void onClick(View view) {
                    bc="#FFFFFF";
                    findViewById(R.id.listview).setBackgroundColor(Color.parseColor(bc));
                }
            });
            color1.setOnClickListener(new View.OnClickListener() {
                String bc="";
                @Override
                public void onClick(View view) {
                    bc="#FFF68F";
                    findViewById(R.id.listview).setBackgroundColor(Color.parseColor(bc));
                    Toast.makeText(SqlLiteTest.this, "颜色更改成功", Toast.LENGTH_SHORT).show();
                }
            });
            color2.setOnClickListener(new View.OnClickListener() {
                String bc="";
                @Override
                public void onClick(View view) {
                    bc="#FFE7BA";
                    findViewById(R.id.listview).setBackgroundColor(Color.parseColor(bc));
                    Toast.makeText(SqlLiteTest.this, "颜色更改成功", Toast.LENGTH_SHORT).show();
                }
            });
            color3.setOnClickListener(new View.OnClickListener() {
                String bc="";
                @Override
                public void onClick(View view) {
                    bc="#FFC0CB";
                    findViewById(R.id.listview).setBackgroundColor(Color.parseColor(bc));
                    Toast.makeText(SqlLiteTest.this, "颜色更改成功", Toast.LENGTH_SHORT).show();
                }
            });
            color4.setOnClickListener(new View.OnClickListener() {
                String bc="";
                @Override
                public void onClick(View view) {
                    bc="#FFF0F5";
                    findViewById(R.id.listview).setBackgroundColor(Color.parseColor(bc));
                    Toast.makeText(SqlLiteTest.this, "颜色更改成功", Toast.LENGTH_SHORT).show();
                }
            });
            color5.setOnClickListener(new View.OnClickListener() {
                String bc="";
                @Override
                public void onClick(View view) {
                    bc="#FFE1FF";
                    findViewById(R.id.listview).setBackgroundColor(Color.parseColor(bc));
                    Toast.makeText(SqlLiteTest.this, "颜色更改成功", Toast.LENGTH_SHORT).show();
                }
            });
            color6.setOnClickListener(new View.OnClickListener() {
                String bc="";
                @Override
                public void onClick(View view) {
                    bc="#CAE1FF";
                    findViewById(R.id.listview).setBackgroundColor(Color.parseColor(bc));
                    Toast.makeText(SqlLiteTest.this, "颜色更改成功", Toast.LENGTH_SHORT).show();
                }
            });

//            ColorDrawable colorDrawable= (ColorDrawable) color0.getBackground();//获
//            System.out.println(colorDrawable.getAlpha());
            dialog.show();
        }
        else{
            Toast.makeText(SqlLiteTest.this, "窗口打开错误", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 给list升序排序
     * @param
     */
    public void sortListUp(List<Note> list){
        Collections.sort(list, new ComparatorTitleUp());
        nowsort="sortTitleUp";
    }
    public void sortListDown(List<Note> list){
        Collections.sort(list, new ComparatorTitleDown());
        nowsort="sortTitleDown";
    }

}