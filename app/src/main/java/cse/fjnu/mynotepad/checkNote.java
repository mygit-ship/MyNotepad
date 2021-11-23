package cse.fjnu.mynotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Collection;

public class checkNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_note);
        Note note =(Note) getIntent().getSerializableExtra("getNotes_from_indexto_checknote");

        Note note_from_search =(Note) getIntent().getSerializableExtra("getNotes_from_searchednoteto_checknote");
        EditText getTitle=findViewById(R.id.check_ediy_title);
        EditText getContext=findViewById(R.id.check_ediy_context);
        TextView save=findViewById(R.id.save_checknote);
        if(note!=null){
            getTitle.setText(note.getTitle());
            getContext.setText(note.getContext());
            System.out.println(note);
        }else{
            getTitle.setText(note_from_search.getTitle());
            getContext.setText(note_from_search.getContext());
            System.out.println(note_from_search+"kk");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=getTitle.getText().toString();
                String context=getContext.getText().toString();
                System.out.println("title:"+title);
                System.out.println("context:"+context);
                if(title.equals("")||context.equals("")){
                    Toast.makeText(checkNote.this, "输入框为空，请重新输入", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(note!=null){
                        DAO dao=new DAO(getApplicationContext());
                        dao.change(note.getId(),context,title);
                        Intent resultIntent=new Intent(checkNote.this, SqlLiteTest.class);
                        Note newnote =new Note(note.getId(),context,title);

                        resultIntent.putExtra("checkNote_listToIndex",newnote);
                        //传回给index
                        setResult(4, resultIntent);
                        finish();
                    }else{
                        DAO dao=new DAO(getApplicationContext());
                        dao.change(note_from_search.getId(),context,title);
                        Intent resultIntent=new Intent(checkNote.this, searchedNotes.class);
                        Note newnote =new Note(note_from_search.getId(),context,title);

                        resultIntent.putExtra("checkNote_listTosearchednote",newnote);
                        //传回给index
                        setResult(7, resultIntent);
                        finish();
                    }

                }

            }
        });
    }

    /**
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     * data 为返回的intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 得到返回的数据
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //从intent取出bundle
            Bundle myBundle = data.getBundleExtra("Bundle");
            // 获取bundle中数据
           Note note  = (Note) myBundle.getSerializable("note");
           System.out.println("返回过来的note信息是："+note);

            // do something
        }
    }


}