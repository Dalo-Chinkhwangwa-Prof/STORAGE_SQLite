package com.coolcats.sqlitedatabaseprj.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.coolcats.sqlitedatabaseprj.R;
import com.coolcats.sqlitedatabaseprj.databinding.ActivityMainBinding;
import com.coolcats.sqlitedatabaseprj.model.User;
import com.coolcats.sqlitedatabaseprj.model.db.UserDatabaseHelper;
import com.coolcats.sqlitedatabaseprj.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.coolcats.sqlitedatabaseprj.model.db.UserDatabaseHelper.ID_COLUMN;
import static com.coolcats.sqlitedatabaseprj.model.db.UserDatabaseHelper.NAME_COLUMN;
import static com.coolcats.sqlitedatabaseprj.model.db.UserDatabaseHelper.POSITION_COLUMN;
import static com.coolcats.sqlitedatabaseprj.util.Logger.logMessage;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private UserDatabaseHelper dbHelper;

    private ActivityMainBinding binding;

    private List<String> options = new ArrayList<String>(Arrays.asList("ANDROID_DEVELOPER", "IOS_DEVELOPER", "MANAGER"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item_layout, options));
        dbHelper = new UserDatabaseHelper(this);


        User u1 = new User("Dalo TC", Position.ANDROID_DEVELOPER);
        User u2 = new User("Tony TC", Position.IOS_DEVELOPER);
        User u3 = new User("Vanessa TC", Position.MANAGER);

        dbHelper.insertUser(u1);
        dbHelper.insertUser(u2);
        dbHelper.insertUser(u3);
        readDB();

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("FIRST_TIME", true))
            showWelcomeDialog();
    }

    private void readDB() {

        Cursor dbC = dbHelper.getAllUsers();
        dbC.moveToPosition(-1);
        List<User> users = new ArrayList<>();

        while(dbC.moveToNext()){

            String name = dbC.getString(dbC.getColumnIndex(NAME_COLUMN));
            int id = dbC.getInt(dbC.getColumnIndex(ID_COLUMN));
            String posName = dbC.getString(dbC.getColumnIndex(POSITION_COLUMN));
            User user = new User(name, id, Position.valueOf(posName));
            users.add(user);
        }

        dbC.close();
        displayUsers(users);

    }

    private void displayUsers(List<User> users) {

        for(int i = 0; i < users.size(); i++)
            logMessage(users.get(i).toString());

//        users.forEach(new Consumer<User>() {
//            @Override
//            public void accept(User user) {
//
//            }
//        });
//

    }

    private void showWelcomeDialog() {

        sharedPreferences.edit().putBoolean("FIRST_TIME", false).apply();
        new AlertDialog
                .Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat))
                .setMessage("Welcome to the application")
                /*.setPositiveButton("+ve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(this, "Positive ")
                    }
                })*/.setNeutralButton("Thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })/*.setNegativeButton("-ve", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }
        )*/.create().show();

    }
}








