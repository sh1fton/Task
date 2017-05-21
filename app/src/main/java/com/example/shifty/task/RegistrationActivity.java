package com.example.shifty.task;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    DBHelper dbHelper;


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

        dbHelper = new DBHelper(this);

    }

    @Override
    public void onClick(View v) {

        //Создаём объект с правами записи и чтения БД
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Проверям нажатие кнопки
        if (v.getId() == R.id.btn_registration){
            //Проверям корректность заполнения полей и с помощью всплывающих сообщений уведомляем об ошибках ввода
            if(et_email.getText().toString().isEmpty() || et_password.getText().toString().isEmpty() || et_rep_password.getText().toString().isEmpty()){
                Toast.makeText(RegistrationActivity.this, "Не заполнены все поля", Toast.LENGTH_SHORT).show();
            }else if (!et_password.getText().toString().equals(et_rep_password.getText().toString())){
                Toast.makeText(RegistrationActivity.this, "Введённы пароли не совпадают ", Toast.LENGTH_SHORT).show();
            }else if (et_email.getText().toString().length() < 6){
                Toast.makeText(RegistrationActivity.this, "Введённый e-mail содержит менее 6 символов", Toast.LENGTH_SHORT).show();
            }else if (et_password.getText().toString().length() < 4){
                Toast.makeText(RegistrationActivity.this, "Введённый пароль содержит менее 4 символов", Toast.LENGTH_SHORT).show();
            }else {
                //Создаём массив условий
                String[] select_email = new String[]{et_email.getText().toString()};
                //Создаём указатель на результат запроса к БД по указанному условию (email = "введённое значение пользователем")
                Cursor c = database.query(DBHelper.TABLE_USERS, new String[] {DBHelper.KEY_EMAIL}, DBHelper.KEY_EMAIL + " = ?", select_email, null, null, null);

                if (c != null) {
                    if (c.moveToFirst()){
                        c.close();
                        Toast.makeText(RegistrationActivity.this, "Введённый e-mail уже использовался для регистрации", Toast.LENGTH_SHORT).show();
                    }else {
                        //Формируем строку со значением введённого email и паролья(предварительно переводим его в md5)
                        contentValues.put(DBHelper.KEY_EMAIL, et_email.getText().toString());
                        contentValues.put(DBHelper.KEY_PASSWORD, DBHelper.md5(et_password.getText().toString()));
                        //Вставляем строку в БД и закрываем dbHelper
                        database.insert(DBHelper.TABLE_USERS, null, contentValues);
                        dbHelper.close();
                        //Закрываем текущий экран
                        finish();
                    }

                }

            }

        }

    }
}
