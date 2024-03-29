package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Activity_SignUp extends AppCompatActivity {

    private TextInputLayout email_layout, password_layout;
    private EditText email_edit, password_edit;
    private Button btn_sign_up;
    private ImageView btn_back;

    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";

    private ArrayList<String> listOfEmail = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        onClicks();
        onTouches();
        findAllEmails();
    }

    public void init() {
        btn_back = findViewById(R.id.btn_back);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);
        email_edit = findViewById(R.id.email_edit);
        password_edit = findViewById(R.id.password_edit);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        mAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        sharedPreferences = getSharedPreferences(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void onClicks() {
        btn_back.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Activity_SignIn.class);
            intent.putExtra("FROM_ACTIVITY", "Activity_SignUp");
            startActivity(intent);
        });

        btn_sign_up.setOnClickListener(view -> {
            hideKeyBoard();
            onChanges();

            boolean validateEmail = checkValidateEmail();
            boolean validatePassword = checkValidatePassword();

            if (validateEmail && validatePassword) {
                mAuth.createUserWithEmailAndPassword(email_edit.getText().toString(), password_edit.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            editor.putString(Model_User.PREFERENCES_PIN, "");
                            editor.putBoolean(Model_User.PREFERENCES_IS_HAS_FINGERPRINT, false);
                            editor.apply();

                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    
                                }
                            });

                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            Model_User newUser = new Model_User(userID,
                                    "",
                                    firebaseUser.getEmail(), //email
                                    Objects.requireNonNull(firebaseUser.getEmail()).substring(0, firebaseUser.getEmail().indexOf("@")), //login
                                    "",
                                    "",
                                    "");

                            userDataBase.child(mAuth.getCurrentUser().getUid()).setValue(newUser);

                            Intent intent = new Intent(getApplicationContext(), Activity_Pin_Code_Create.class);
                            intent.putExtra("FROM_ACTIVITY", "Activity_SignUp");
                            startActivity(intent);

                        } else {
                            createCustomToast(getResources().getString(R.string.failure2), getColor(R.color.error));
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

    private void onChanges() {
        email_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkValidateEmail() && checkValidatePassword()) {
                    btn_sign_up.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        password_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkValidatePassword() && checkValidateEmail()) {
                    btn_sign_up.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private boolean checkValidateEmail() {
        String email = email_edit.getText().toString();
        if (TextUtils.isEmpty(email)) {
            email_layout.setError("Поле обязательно для заполнения");
            btn_sign_up.setEnabled(false);
            return false;
        } else if (listOfEmail.contains(email)) {
            email_layout.setError("Email уже зарегистрирован");
            btn_sign_up.setEnabled(false);
            return false;
        } else if (!isEmailValid(email)) {
            email_layout.setError("Пример: mail@example.com");
            btn_sign_up.setEnabled(false);
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
            btn_sign_up.setEnabled(false);
            return false;
        } else if (password.length() < 6) {
            password_layout.setError("Минимум 6 символов");
            btn_sign_up.setEnabled(false);
            return false;
        } else {
            password_layout.setError(null);
            return true;
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void findAllEmails() {
        userDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfEmail.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String email = String.valueOf(dataSnapshot.child("email").getValue());
                    listOfEmail.add(email);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void createCustomToast(String message, int color) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 400);
        View view = toast.getView();
        view.getBackground().setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(color);
        text.setGravity(Gravity.CENTER);
        toast.show();
    }

    public void hideKeyBoard() {
        View keyBoard = this.getCurrentFocus();
        if (keyBoard != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            assert inputMethodManager != null;
            inputMethodManager.hideSoftInputFromWindow(keyBoard.getWindowToken(), 0);
        }
    }
}