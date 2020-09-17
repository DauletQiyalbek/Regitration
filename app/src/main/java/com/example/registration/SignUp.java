package com.example.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.registration.StoreDatabase.COLUMN_INFO;
import static com.example.registration.StoreDatabase.COLUMN_EMAIL;
import static com.example.registration.StoreDatabase.COLUMN_PASSWORD;
import static com.example.registration.StoreDatabase.COLUMN_CONPASSWORD;
import static com.example.registration.StoreDatabase.TABLE_USER;


import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    SQLiteDatabase sqLiteDatabase;
    StoreDatabase storeDatabase;

    TextInputLayout up_username,up_email,up_password,up_confirmPassword;
    TextView tv_signIn;
    Button btn_signUP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tv_signIn = findViewById(R.id.tv_signIn);
        btn_signUP = findViewById(R.id.btn_signUP);

        up_username = findViewById(R.id.up_username);
        up_email = findViewById(R.id.up_email);
        up_password = findViewById(R.id.up_password);
        up_confirmPassword = findViewById(R.id.up_confirmPassord);

        btn_signUP.setOnClickListener(this);
        tv_signIn.setOnClickListener(this);
        storeDatabase = new StoreDatabase(this);
        sqLiteDatabase = storeDatabase.getWritableDatabase();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signUP:
                boolean createAccount = true;
                String email = up_email.getEditText().getText().toString();
                String pass =up_password.getEditText().getText().toString();
                String pass2 =up_confirmPassword.getEditText().getText().toString();

                if (up_username.getEditText().getText().toString().isEmpty() ){
                    up_username.setError("Try again!");
                    createAccount = false;
                }
                if (email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    up_email.setError("Try again!");
                    createAccount = false;
                }
                if (pass.isEmpty() || !pass.equals(pass2) || pass.length() != 8){
                    up_password.setError("Try again!");
                    createAccount = false;
                }

                if (createAccount){

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_INFO,up_username.getEditText().getText().toString());
                    contentValues.put(COLUMN_EMAIL,up_email.getEditText().getText().toString());
                    contentValues.put(COLUMN_PASSWORD,up_password.getEditText().getText().toString());
                    contentValues.put(COLUMN_CONPASSWORD,up_confirmPassword.getEditText().getText().toString());

                    sqLiteDatabase.insert(TABLE_USER,null,contentValues);
                    Intent intent = new Intent(SignUp.this,Home.class);
                    startActivity(intent);
                    showDatabaseData();

                    Toast.makeText(this, "Create account success!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Fill all info,try again!", Toast.LENGTH_SHORT).show();
                }
               break;
            case R.id.tv_signIn:
                Intent intent = new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
                break;
        }
    }

    public void showDatabaseData(){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +TABLE_USER,null);

        if ((cursor != null && cursor.getCount() > 0)){
            while (cursor.moveToNext()){
                String fName = cursor.getString(cursor.getColumnIndex(COLUMN_INFO));
                String email = cursor.getString(cursor.getColumnIndex(COLUMN_INFO));
                String password = cursor.getString(cursor.getColumnIndex(COLUMN_INFO));
                String conPassword = cursor.getString(cursor.getColumnIndex(COLUMN_INFO));

                Log.i("Database","fullName:"+fName);
                Log.i("Database","email:"+email);
                Log.i("Database","password:"+password);
                Log.i("Database","conPassword:"+conPassword);
            }

        }




    }
}