package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class Activity_SignUp extends AppCompatActivity {

    private TextInputLayout email_layout, password_layout;
    private EditText email_edit, password_edit;
    private Button btn_sign_up;
    private ImageView btn_back;

    private FirebaseAuth mAuth;

    private String USER_KEY = "users";

    public static Activity_SignUp instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        onClicks();
        onTouches();
    }

    public void init() {
        instance = this;
        btn_back = findViewById(R.id.btn_back);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        email_edit = findViewById(R.id.email_edit);
        password_edit = findViewById(R.id.password_edit);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        mAuth = FirebaseAuth.getInstance();
    }

    private void onClicks() {
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Activity_First.class);
            startActivity(intent);
        });

        btn_sign_up.setOnClickListener(view -> {
            //Прячем клавиатуру
            hideKeyBoard();

            boolean validateEmail = checkValidateEmail();
            boolean validatePassword = checkValidatePassword();

            if (validateEmail && validatePassword) {
                mAuth.createUserWithEmailAndPassword(email_edit.getText().toString(), password_edit.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

                            String userID = mAuth.getCurrentUser().getUid();

                            Model_User newUser = new Model_User(userID, "", email_edit.getText().toString(),
                                    "", "", "", "");

                            userDataBase.child(mAuth.getCurrentUser().getUid()).setValue(newUser);

                            Intent intent = new Intent(getApplicationContext(), Activity_Profile.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(Activity_SignUp.this, "Данный e-mail уже зарегистрирован", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void onTouches() {
        //noinspection AndroidLintClickableViewAccessibility
        btn_back.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                btn_back.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_primary_200));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                btn_back.setBackgroundColor(getColor(R.color.primary_500));
            }
            return false;
        });
    }

    private boolean checkValidateEmail() {
        String email = email_edit.getText().toString();
        if (email.equals("")) {
            email_layout.setError("Поле обязательно для заполнения");
            btn_sign_up.setEnabled(false);
            return false;
        } else if (!email.contains("@")) {
            email_layout.setError("Пример: mail@example.com");
            btn_sign_up.setEnabled(false);
            return false;
        } else {
            email_layout.setError(null);
            btn_sign_up.setEnabled(true);
            return true;
        }
    }
    private boolean checkValidatePassword() {
        String password = password_edit.getText().toString();
        if (password.equals("")) {
            password_layout.setError("Поле обязательно для заполнения");
            btn_sign_up.setEnabled(false);
            return false;
        } else if (password.length() < 6) {
            password_layout.setError("Минимум 6 символов");
            btn_sign_up.setEnabled(false);
            return false;
        } else {
            password_layout.setError(null);
            btn_sign_up.setEnabled(true);
            return true;
        }
    }

    public static void hideKeyBoard() {
        View keyBoard = instance.getCurrentFocus();
        if (keyBoard != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) instance.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(keyBoard.getWindowToken(), 0);
        }
    }
}