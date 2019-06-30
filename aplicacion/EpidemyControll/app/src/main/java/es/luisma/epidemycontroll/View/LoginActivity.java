package es.luisma.epidemycontroll.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.luisma.epidemycontroll.Controller.LoginController;
import es.luisma.epidemycontroll.R;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button botonLogin;
    private TextView registra;
    private LoginController controller;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getSharedPreferences("login",MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        controller = new LoginController();
        setContentView(R.layout.login);
        username = (EditText) findViewById(R.id.loginUser);
        password = (EditText) findViewById(R.id.loginPass);
        botonLogin = (Button) findViewById(R.id.buttonLogin);
        registra = (TextView) findViewById(R.id.textToRegister);
        botonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        registra.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    public void login() {
        if (!validate()) {
            return;
        }
        String Username = username.getText().toString();
        String pass = password.getText().toString();
        int t = controller.login(Username,pass);
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
                            "Datos incorrectos", Toast.LENGTH_SHORT);
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
