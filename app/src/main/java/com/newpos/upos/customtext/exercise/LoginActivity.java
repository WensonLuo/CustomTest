package com.newpos.upos.customtext.exercise;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.newpos.upos.customtext.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameET;
    private EditText passwordET;
    private CheckBox rememberPasswordChk;
    private Button loginBtn;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String username, password;
    private boolean isRemember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        isRemember = pref.getBoolean("isRemember",false);
        if (isRemember){
            usernameET.setText(pref.getString("username",""));
            passwordET.setText(pref.getString("password",""));
            rememberPasswordChk.setChecked(true);
        }
    }

    private void initView() {
        usernameET = (EditText) findViewById(R.id.et_username);
        passwordET = (EditText) findViewById(R.id.et_password);
        rememberPasswordChk = (CheckBox) findViewById(R.id.chk_remember_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        username = usernameET.getText().toString();
        password = passwordET.getText().toString();
        if (username.equals("admin") && password.equals("123")){
            editor = pref.edit();
            if (rememberPasswordChk.isChecked()){
                editor.putString("username",username);
                editor.putString("password",password);
                editor.putBoolean("isRemember",true);
                editor.apply();
            }else {
                editor.clear();
            }
            Intent intent = new Intent(this, LoginSuccessActivity.class);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(LoginActivity.this, "用户名或密码错误，请重试！",Toast.LENGTH_SHORT).show();
        }

    }
}
