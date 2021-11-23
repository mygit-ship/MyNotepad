package cse.fjnu.mynotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class addNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        TextView save_addnote=findViewById(R.id.save_addnote);
        transferList getTransferList =(transferList) getIntent().getSerializableExtra("getNotes_from_index");
        System.out.println("addnote拿到index发过来的信息了");
        List<Note> getList=getTransferList.getNotes();
        for(Note note: getList){
            System.out.println(note);
        }


        //给保存按钮添加监听事件
        save_addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              EditText getTitle=findViewById(R.id.addnote_ediy_title);
              String getTitle_text=getTitle.getText().toString();
              EditText getContext=findViewById(R.id.addnote_ediy_context);
              String getContext_text=getContext.getText().toString();
              int last_noteid=getList.get(getList.size()-1).getId()+1;
              if(getTitle_text.equals("")||getContext_text.equals("")){
                  Toast.makeText(addNote.this, "标题为空或者内容为空哦，请填写完整！", Toast.LENGTH_SHORT).show();
              }
              else{
                  Note new_addNote=new Note(last_noteid,getContext_text,getTitle_text);
                  //给要发回去的列表中添加新添加的note
                  getList.add(new_addNote);
                  //往数据库中添加该note信息
                 DAO dao=new DAO(getApplicationContext());
                 dao.insert(new_addNote);
                  Intent resultIntent=new Intent(addNote.this, SqlLiteTest.class);
                  resultIntent.putExtra("addNote_listToIndex",new transferList(getList));
                  //传回给index
                  setResult(3, resultIntent);
                  finish();
              }


            }
        });
    }
}