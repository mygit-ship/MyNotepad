package cse.fjnu.mynotepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 该页面用来显示所有符合条件的搜索到的笔记
 */
public class searchedNotes extends AppCompatActivity {
    //搜索到的list
    List<Note> newSearchedList=new ArrayList<Note>();
    //初始的所有list
    List<Note> getList=new ArrayList<Note>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_notes);
        ListView listView=findViewById(R.id.Listview_searched);
        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        TextView searchedTitle=findViewById(R.id.search_note);
        searchedTitle.setTypeface(font);
        searchedTitle.setText(getResources().getString(R.string.search_note));


        //获取从index传送过来的lsit
        transferList getTransferList =(transferList) getIntent().getSerializableExtra("getNotes_from_index");
        if(getTransferList!=null){
            getList=getTransferList.getNotes();
            //得到初始的所有list
            for(Note note: getList){
                System.out.println(note);
            }
        }

        Intent resultIntent=new Intent(searchedNotes.this, SqlLiteTest.class);
        resultIntent.putExtra("searchNote_listToIndex",new transferList(getList));
        //传回给index
        setResult(8, resultIntent);



        searchedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //得到
                newSearchedList= searchedList(getList);
                //给listview赋值符合搜索条件的note
                freshList(newSearchedList);
            }

        });
        //返回参数为7，7的到checknote页面的返回信息
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //得到所有笔记
                Intent myIntent = new Intent();
                myIntent.putExtra("getNotes_from_searchednoteto_checknote", (Serializable) newSearchedList.get((int) i));
                //设置跳转的页面
                myIntent.setClass(searchedNotes.this, checkNote.class);

                startActivityForResult(myIntent,7);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note=newSearchedList.get(i);
                if(note==null){
                    Toast.makeText(searchedNotes.this, "删除失败", Toast.LENGTH_SHORT).show();
                    return false;
                }
                else{
                    DAO dao=new DAO(getApplicationContext()) ;
                    dao.delete(note);
                    getList.remove(note);
                    newSearchedList.remove(note);
                    freshList(newSearchedList);
                    Toast.makeText(searchedNotes.this, "删除成功", Toast.LENGTH_SHORT).show();
                    return true;
                }

            }
        });
    }


    /**
     * 点击搜索后返回符合条件的note
     * @param getList
     */
    public  List<Note> searchedList(List<Note> getList){

        if(getList==null){
            Toast.makeText(searchedNotes.this, "目前没有任何笔记，无法进行搜索！！", Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            EditText searchedTitle=findViewById(R.id.searched_Title);
            String getSearchedTitle = searchedTitle.getText().toString();
            List<Note> newSearchedList=new ArrayList<Note>();
            Boolean isNull=false;
            for(Note note: getList){
                if( note.isExited(getSearchedTitle)==true){
                    newSearchedList.add(note);
                    isNull=true;
                }
            }
            if(isNull==false){
                return null;
            }else{
                return newSearchedList;
            }

        }


    }

    /**
     * 刷新本页面的listview
     */
    public  void freshList(List<Note> newSearchedList){
        ListView listView=findViewById(R.id.Listview_searched);
        if(newSearchedList==null){
            Toast.makeText(searchedNotes.this, "无符合搜索条件的笔记！！请重新搜索", Toast.LENGTH_SHORT).show();
            listView.setAdapter(null);
        }
        else{
            List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();


            Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");

            for(Note note : newSearchedList){
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

        Note note;
        //收到从checknote返回的信息时
        if(requestCode==7&&resultCode==7){
            System.out.println("接收到来自searc的数据:");
            Note note_from_checknote= (Note) data.getSerializableExtra("checkNote_listTosearchednote");
            System.out.println("为："+note_from_checknote);
            for(int i=0;i<getList.size();i++){
                if(getList.get(i).getId()==note_from_checknote.getId()){
                    getList.set(i,note_from_checknote);
                }
            }

            //重新检索一次
            newSearchedList= searchedList(getList);
            freshList(newSearchedList);

        }

    }
}