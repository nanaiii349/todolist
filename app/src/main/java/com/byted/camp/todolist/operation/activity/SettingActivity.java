package com.byted.camp.todolist.operation.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.byted.camp.todolist.R;

public class SettingActivity extends AppCompatActivity {

    private static final String KEY_IS_NEED_SORT = "is_need_sort";

    private Switch commentSwitch;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Context context = this;

        //todo 从sp读出数据更新isOpen字段
        SharedPreferences sp = getPreferences(context.MODE_PRIVATE);
        boolean isOpen = sp.getBoolean("isopen",false);
        Log.d("data","In SettingActivity  In Read isOpen:"+isOpen);

        commentSwitch = findViewById(R.id.switch_comment);
        commentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //todo 存储开关值进sp
                Log.d("data","In SettingActivity  In Write isOpen:"+!isOpen);
//                SharedPreferences sp = getPreferences(context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isopen",!isOpen);
                editor.commit();
            }
        });
        Log.d("data","In SettingActivity In setChecked isOpen:"+isOpen);
        commentSwitch.setChecked(sp.getBoolean("isopen",false));
    }

}
