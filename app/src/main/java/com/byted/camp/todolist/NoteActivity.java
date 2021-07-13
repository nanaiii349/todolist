package com.byted.camp.todolist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private EditText editText;
    private Button addBtn;
    private int priorty=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        setTitle(R.string.take_a_note);

        editText = findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(editText, 0);
        }

        addBtn = findViewById(R.id.btn_add);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = editText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(NoteActivity.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = saveNote2Database(content.toString().trim());
                if (succeed) {
                    Toast.makeText(NoteActivity.this,
                            "Note added", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(NoteActivity.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        final RadioGroup pro=(RadioGroup) findViewById(R.id.RadioGroup1);//获取单选按钮组
        //为单选按钮组添加事件监听
        pro.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton RB=(RadioButton) findViewById(i);//获取被选择的单选按钮
                if(RB.getId()==2131230940) {
                    priorty = 1;
                    Log.i("NoteActivity", "In RadioGroup:lp");
                }
                if(RB.getId()==2131230941) {
                    priorty = 2;
                    Log.i("NoteActivity", "In RadioGroup:mp");
                }
                if(RB.getId()==2131230939) {
                    priorty = 3;
                    Log.i("NoteActivity", "In RadioGroup:hp");
                }
                Log.i("data","你的选择是："+RB.getText()+" id:"+RB.getId());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean saveNote2Database(String content) {
        // TODO 插入一条新数据，返回是否插入成功
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = new Date(System.currentTimeMillis());

        TodoDbHelper dbHelper = new TodoDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodoContract.TodoEntry.COLUMN_CONTENT,content);
        values.put(TodoContract.TodoEntry.COLUMN_DATE,simpleDateFormat.format(date));
        // TODO 修改
        values.put(TodoContract.TodoEntry.COLUMN_STATE, 0);
        values.put(TodoContract.TodoEntry.COLUMN_PRIORTY,priorty);
        long newRowId = db.insert(TodoContract.TodoEntry.TABLE_NAME,null,values);
        return newRowId != 0;
    }
}
