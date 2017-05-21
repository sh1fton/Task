package com.example.shifty.task;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    DBHelper dbHelper;

    //Создаём константу с название файла настроек
    public static final String APP_PREFERENCES = "settings";
    //Создаём константу с название ключа в файле настроек, в котором будем хранить залогиненого пользователя
    public static final String APP_PREFERENCES_CURRENT_USER = "current_user";
    private SharedPreferences sPref;

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

        //Создаём экземпляр класса DBHelper
        dbHelper = new DBHelper(this);

        sPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        //Проверяем содержит ли файл настроек поле с текущим пользователем
        if (sPref.contains(APP_PREFERENCES_CURRENT_USER)) {
            //Считываем значения поля текущего пользователя
            String current_user = sPref.getString(APP_PREFERENCES_CURRENT_USER, "");
            //Если оно не пустое то
            if(!current_user.equals("")){
                //Создаём Intent, заносим в него данные о email, с помощью которого была произведена авторизация
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("user_email", current_user);
                //С помощью Intent открываем экран успешного входа
                startActivity(intent);
            }
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        Cursor c;
        //Создаём объект с правами чтения БД
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] select_email;
        String current_password = null;

        //Проверям какая кнопка нажата
        switch (v.getId()){
            case R.id.btn_login:
                //Проверям на пустые поля ввода и выводим всплывающее сообщение при наличии ошибки
                if(!et_email.getText().toString().isEmpty()){
                    //Создаём массив условий
                    select_email = new String[]{et_email.getText().toString()};
                    //Создаём указатель на результат запроса к БД по указанному условию (email = "введённое значение пользователем")
                    c = database.query(DBHelper.TABLE_USERS, new String[] {DBHelper.KEY_PASSWORD}, DBHelper.KEY_EMAIL + " = ?", select_email, null, null, null);
                    //Проверяем получен ли резулатат запроса к БД
                    if (c != null) {
                        //Проверяем получен ли резулатат запроса к БД
                        if (c.moveToFirst()) {
                            //Находим поле с паролем и записываем его значение в переменную current_password
                            do {
                                for (String cn : c.getColumnNames()) {

                                    if(cn.equals(DBHelper.KEY_PASSWORD)){
                                        current_password = c.getString(c.getColumnIndex(cn));
                                    }
                                }
                            } while (c.moveToNext());

                            c.close();
                            dbHelper.close();

                            //Сверяем введённый пароль с паролем из БД(предварительно переводим в md5)
                            if(DBHelper.md5(et_password.getText().toString()).equals(current_password)){

                                //Заносим значение текущего пользователя в файл с настройками приложения
                                SharedPreferences.Editor editor = sPref.edit();
                                editor.putString(APP_PREFERENCES_CURRENT_USER, et_email.getText().toString());
                                editor.apply();

                                //Создаём Intent, заносим в него данные о email, с помощью которого была произведена авторизация
                                intent = new Intent(this, LoginActivity.class);
                                intent.putExtra("user_email", et_email.getText().toString());
                                //С помощью Intent открываем экран успешного входа
                                startActivity(intent);

                            }else {
                                Toast.makeText(MainActivity.this, "Неверный логин и/или пароль", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, "Неверный логин и/или пароль", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Нет доступа к базе данных", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MainActivity.this, "Не заполнено поле email", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_registration:
                //С помощью Intent открываем экран регистрации
                intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
        }

    }


    //Создаём метод при переходе обратно к главному экрану приложения
    @Override
    protected void onResume() {
        super.onResume();
        //Проверяем содержит ли файл настроек поле с текущим пользователем
        if (sPref.contains(APP_PREFERENCES_CURRENT_USER)) {
            //Сбрасываем значение текущего пользователя на пустое поле
            SharedPreferences.Editor editor = sPref.edit();
            editor.putString(APP_PREFERENCES_CURRENT_USER, "");
            editor.apply();
        }
    }

}
