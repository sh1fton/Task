package com.example.shifty.task;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_logout;
    TextView tv_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Получаем Intent, по кторому попали на этот экран
        Intent intent = getIntent();

        //Заполняем TextView с использование переданных данных через Intent
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_email.setText("Вы авторизовались с помощью e-mail: " + intent.getStringExtra("user_email"));

        //Назначаем обработчик на кнопку
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //Проверяем нажатие кнопки
        if(v.getId() == R.id.btn_logout) {
            //По нажатию закрываем текущий экран
            finish();
        }
    }
}
