package com.example.shifty.task;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_registration;

    EditText et_email;
    EditText et_password;
    EditText et_rep_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Находим все View элементы
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_rep_password = (EditText) findViewById(R.id.et_rep_password);

        btn_registration = (Button) findViewById(R.id.btn_registration);
        //Назначаем обработчки для кнопки
        btn_registration.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //Проверям нажатие кнопки
        if (v.getId() == R.id.btn_registration){
            //Проверям корректность заполнения полей и с помощью всплывающих сообщений уведомляем об ошибках ввода
            if(et_email.getText().toString().isEmpty() || et_password.getText().toString().isEmpty() || et_rep_password.getText().toString().isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Не заполнены все поля", Toast.LENGTH_SHORT).show();
            }else if (!et_password.getText().toString().equals(et_rep_password.getText().toString())){
                Toast.makeText(RegistrationActivity.this, "Введённы пароли не совпадают ", Toast.LENGTH_SHORT).show();
            }else if (et_email.getText().toString().length() < 6){
                Toast.makeText(RegistrationActivity.this, "Введённый e-mail содержит менее 6 символов", Toast.LENGTH_SHORT).show();
            }else if (et_password.getText().toString().length() < 4){
                Toast.makeText(RegistrationActivity.this, "Введённый пароль содержит менее 4 символов", Toast.LENGTH_SHORT).show();
            }else if (Users.GetUsers().users_email.contains(et_email.getText().toString())){
                Toast.makeText(RegistrationActivity.this, "Введённый e-mail уже использовался для регистрации", Toast.LENGTH_SHORT).show();
            }else {
                //Заносим полученные данные в массивы
                Users.GetUsers().users_email.add(et_email.getText().toString());
                Users.GetUsers().users_password.add(Users.md5(et_password.getText().toString()));
                finish();

            }

        }

    }
}
