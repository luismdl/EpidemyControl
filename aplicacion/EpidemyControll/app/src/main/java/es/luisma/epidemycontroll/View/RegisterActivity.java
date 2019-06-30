package es.luisma.epidemycontroll.View;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.luisma.epidemycontroll.Controller.LoginController;
import es.luisma.epidemycontroll.Controller.RegisterController;
import es.luisma.epidemycontroll.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText email;
    private Button botonRegister;

    private RegisterController controller;
    private EditText birthdate;

    SharedPreferences sp;

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("login",MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        controller = new RegisterController();
        username = (EditText) findViewById(R.id.registerUser);
        email = (EditText) findViewById(R.id.registerEmail);
        password = (EditText) findViewById(R.id.registerPass);
        botonRegister = (Button) findViewById(R.id.buttonRegister);
        birthdate = (EditText) findViewById(R.id.registerBirth);
        botonRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                register();
            }
        });

        birthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }
    final Calendar myCalendar = Calendar.getInstance();

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        birthdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void register(){
        if (!validate()) {
            return;
        }
        String Username = username.getText().toString();
        String pass = password.getText().toString();
        String mail = email.getText().toString();
        String birth = birthdate.getText().toString();
        int t = controller.register(Username,pass,mail,birth);
        if(t>=0){
            sp.edit().putString("user",Username).apply();
            sp.edit().putBoolean("logged",true).apply();
            sp.edit().putInt("admin",t).apply();
            Intent activityIntent = new Intent(this, MainActivity.class);
            startActivity(activityIntent);
            finish();
        }else{
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Datos ya existen", Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.TOP|Gravity.RIGHT,0,0);

            toast1.show();
        }

    }

    public boolean validate() {
        boolean valid = true;

        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (user.isEmpty() ||user.length()<5) {
            username.setError("Username should be longer");
            valid = false;
        } else {
            username.setError(null);
        }

        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

}
