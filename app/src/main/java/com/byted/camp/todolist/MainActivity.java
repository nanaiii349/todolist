package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.beans.State;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.operation.activity.DatabaseActivity;
import com.byted.camp.todolist.operation.activity.DebugActivity;
import com.byted.camp.todolist.operation.activity.SettingActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;
    private TodoDbHelper dbHelper;

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
            new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //创建数据库
        dbHelper = new TodoDbHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);

        try {
            notesAdapter.refresh(loadNotesFromDatabase());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            case R.id.action_database:
                startActivity(new Intent(this, DatabaseActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            try {
                notesAdapter.refresh(loadNotesFromDatabase());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Note> loadNotesFromDatabase() throws ParseException {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        List<Note> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] projection = {
                TodoContract.TodoEntry._ID,
                TodoContract.TodoEntry.COLUMN_CONTENT,
                TodoContract.TodoEntry.COLUMN_DATE,
                TodoContract.TodoEntry.COLUMN_STATE,
                TodoContract.TodoEntry.COLUMN_PRIORTY
        };
        Cursor cursor = db.query(TodoContract.TodoEntry.TABLE_NAME,projection,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
//                Log.d("data", String.valueOf(cursor.getColumnIndex("_id")));
                Note tempNote = new Note(cursor.getLong(cursor.getColumnIndex("_id")));
                String content = cursor.getString(cursor.getColumnIndex("content"));
//                Log.d("data", " In Main Load content is " + content);
                tempNote.setContent(content);

                String date = cursor.getString(cursor.getColumnIndex("date"));
//                Log.d("data", " In Main Load date is " + date);
                tempNote.setDate(SIMPLE_DATE_FORMAT.parse(date));

                Integer priority = cursor.getInt(cursor.getColumnIndex("priority"));
//                Log.d("data", " In Main Load priority is " + priority);
                tempNote.setPriority(priority);

                int state = cursor.getInt(cursor.getColumnIndex("state"));
                Log.d("data", " In Main Load state is " + State.from(state));
                tempNote.setState(State.from(state));

                list.add(tempNote);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = TodoContract.TodoEntry._ID +" LIKE ? ";
        String[] selectionArgs = {String.valueOf(note.getId())};
        db.delete(TodoContract.TodoEntry.TABLE_NAME,selection,selectionArgs);
        try {
            notesAdapter.refresh(loadNotesFromDatabase());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void updateNode(Note note) {
        // 更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.d("data", " In Main Load In updateNode state is " + State.refrom(note.getState()));
        values.put(TodoContract.TodoEntry.COLUMN_STATE, State.refrom(note.getState()));
        String selection = TodoContract.TodoEntry._ID+" LIKE ? ";
        String[] selectionArgs = {String.valueOf(note.getId())};
        db.update(TodoContract.TodoEntry.TABLE_NAME, values, selection, selectionArgs);
    }

}
