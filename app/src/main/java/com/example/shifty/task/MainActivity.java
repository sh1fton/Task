package com.example.shifty.task;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_login;
    Button btn_registration;

    EditText et_email;
    EditText et_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Находим все View элементы
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_registration = (Button) findViewById(R.id.btn_registration);

        //Назначаем обработчик на кнопкки
        btn_login.setOnClickListener(this);
        btn_registration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        //Проверям какая кнопка нажата
        switch (v.getId()){
            case R.id.btn_login:
                //Проверям наличие введённого email и при нахождении сверяем ввндённый пароль (предварительно переводим его в md5)
                if(Users.GetUsers().users_email.contains(et_email.getText().toString())){
                    if(Users.md5(et_password.getText().toString()).equals(Users.GetUsers().users_password.get(Users.GetUsers().users_email.indexOf(et_email.getText().toString())))){
                        //Создаём Intent, заносим в него данные о email, с помощью которого была произведена авторизация
                        intent = new Intent(this, LoginActivity.class);
                        intent.putExtra("user_email", et_email.getText().toString());
                        //С помощью Intent открываем экран авторизации
                        startActivity(intent);
                    }else {
                        //С помощью всплывающих сообщений уведомляем об ошибках ввода
                        Toast.makeText(MainActivity.this, "Неверный логин и/или пароль", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Неверный логин и/или пароль", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_registration:
                //С помощью Intent открываем экран регистрации
                intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }
    }

}
