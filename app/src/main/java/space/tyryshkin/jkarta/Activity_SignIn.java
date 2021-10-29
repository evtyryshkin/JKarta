package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_SignIn extends AppCompatActivity {

    private TextInputLayout email_layout, password_layout;
    private EditText email_edit, password_edit;
    private Button btn_sign_in, btn_sign_up, btn_restore_password;

    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
        onClicks();
    }

    public void init() {
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        email_edit = findViewById(R.id.email_edit);
        password_edit = findViewById(R.id.password_edit);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        btn_restore_password = findViewById(R.id.btn_restore_password);

        mAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    private void onClicks() {
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validateEmail = checkValidateEmail();
                boolean validatePassword = checkValidatePassword();

                if (validateEmail && validatePassword) {
                    mAuth.signInWithEmailAndPassword(email_edit.getText().toString(), password_edit.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), Activity_General_Space_App.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_SignUp.class);
                startActivity(intent);
            }
        });
        btn_restore_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private boolean checkValidateEmail() {
        String email = email_edit.getText().toString();
        if (email.equals("")) {
            email_layout.setError("Поле обязательно для заполнения");
            return false;
        } else if (!email.contains("@")) {
            email_layout.setError("Пример: mail@example.com");
            return false;
        } else {
            email_layout.setError(null);
            return true;
        }
    }
    private boolean checkValidatePassword() {
        String password = password_edit.getText().toString();
        if (password.equals("")) {
            password_layout.setError("Поле обязательно для заполнения");
            return false;
        } else if (password.length() < 6) {
            password_layout.setError("Пароль должен состоять минимум из 6 символов");
            return false;
        } else {
            password_layout.setError(null);
            return true;
        }
    }
}