package com.example.shifty.task;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


//Класс Singletone для хранения данных в памяти

public class Users {
    private static Users _instance = null;

    //Для хранения данных используем ArrayList

    public ArrayList<String> users_email = new ArrayList<String>();
    public ArrayList<String> users_password = new ArrayList<String>();

    protected Users() {}

    public static Users GetUsers(){
        if( _instance == null){
            _instance = new Users();
        }
        return _instance;
    }

    //Метод для перевода строки в md5
    public static String md5(String s){

        try {

            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
