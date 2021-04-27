package com.coolcats.sqlitedatabaseprj.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.coolcats.sqlitedatabaseprj.R;
import com.coolcats.sqlitedatabaseprj.databinding.ActivityMainBinding;
import com.coolcats.sqlitedatabaseprj.model.User;
import com.coolcats.sqlitedatabaseprj.model.db.UserDatabaseHelper;
import com.coolcats.sqlitedatabaseprj.util.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.coolcats.sqlitedatabaseprj.model.db.UserDatabaseHelper.ID_COLUMN;
import static com.coolcats.sqlitedatabaseprj.model.db.UserDatabaseHelper.NAME_COLUMN;
import static com.coolcats.sqlitedatabaseprj.model.db.UserDatabaseHelper.POSITION_COLUMN;
import static com.coolcats.sqlitedatabaseprj.util.Logger.logMessage;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private SharedPreferences sharedPreferences;

    private UserDatabaseHelper dbHelper;

    private ActivityMainBinding binding;

    private List<String> options = new ArrayList<String>(Arrays.asList("ANDROID_DEVELOPER", "IOS_DEVELOPER", "MANAGER"));
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayAdapter<String> adapter=  new ArrayAdapter<String>(this, R.layout.spinner_item_layout,R.id.item_text,  options);
//
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(this);
        dbHelper = new UserDatabaseHelper(this);

        readDB();

        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("FIRST_TIME", true))
            showWelcomeDialog();

        binding.addUserButton.setOnClickListener(view -> {

            if(checkInput()) {
                User newUser = new User(binding.usernameEdittext.getText().toString().trim(), Position.valueOf(options.get(id)));
                dbHelper.insertUser(newUser);
                readDB();
                binding.usernameEdittext.setText("");
            }
        });
    }

    private boolean checkInput() {

        if (binding.usernameEdittext.getText().toString().trim().isEmpty()){
            toastMessage("Name cannot be empty.");
            return false;
        } else if(binding.usernameEdittext.getText().toString().trim().length() < 4){
            toastMessage("Name should be at least 3 characters.");
            return false;
        }
        return true;
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void readDB() {

        Cursor dbC = dbHelper.getAllUsers();
        dbC.moveToPosition(-1);
        List<User> users = new ArrayList<>();

        StringBuilder sBuilder = new StringBuilder();

        while(dbC.moveToNext()){
            String name = dbC.getString(dbC.getColumnIndex(NAME_COLUMN));
            int id = dbC.getInt(dbC.getColumnIndex(ID_COLUMN));
            String posName = dbC.getString(dbC.getColumnIndex(POSITION_COLUMN));
            User user = new User(name, id, Position.valueOf(posName));
            users.add(user);
            sBuilder.append(user.getName()).append(" : ").append(user.getPosition().name()).append("\n");
        }

        binding.outputTextview2.setText(sBuilder.toString());

        dbC.close();
        displayUsers(users);

    }

    private void displayUsers(List<User> users) {

        for(int i = 0; i < users.size(); i++)
            logMessage(users.get(i).toString());


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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        toastMessage(options.get(i) + ", "+l);
        id = i;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        toastMessage("Please make a selection!");
    }
}








