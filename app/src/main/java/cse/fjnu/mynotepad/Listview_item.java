package cse.fjnu.mynotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class Listview_item extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_item);
        Typeface font = Typeface.createFromAsset(getAssets(), "iconfont.ttf");
        TextView edit_note=findViewById(R.id.edit_note);
        edit_note.setTypeface(font);
        edit_note.setText(getResources().getString(R.string.edit_note));
    }
}