package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Activity_SignIn extends AppCompatActivity {

    private TextInputLayout email_layout, password_layout;
    private EditText email_edit, password_edit;
    private Button btn_sign_in, btn_sign_up, btn_restore_password;

    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";

    private ArrayList<String> listOfEmail = new ArrayList<>();

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

        findAllEmail();
    }

    private void onClicks() {
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidateEmail() && checkValidatePassword()) {
                    mAuth.signInWithEmailAndPassword(email_edit.getText().toString(), password_edit.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(getApplicationContext(), Activity_General_Space_App.class);
                                        startActivity(intent);
                                    } else {
                                        password_layout.setError("Неверный Email или пароль");
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
                showDialogResetPassword();
            }
        });
    }

    private void showDialogResetPassword() {
        Dialog dialog = createDialog(R.layout.dialog_reset_password);

        placeDialogBottom(dialog);

        TextInputLayout email_layout = dialog.findViewById(R.id.email_layout);
        TextInputEditText edit = dialog.findViewById(R.id.edit);
        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton next = dialog.findViewById(R.id.btn_next);

        next.setOnClickListener(view -> {
            onChanges(email_layout, edit, next);
            String mail = edit.getText().toString();

            if (checkValidateEmail2(email_layout, edit, next)) {
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        hideKeyBoard();
                        Toast toast = Toast.makeText(Activity_SignIn.this, getResources().getString(R.string.reset_password_toast), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 400);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(getColor(R.color.teal_500));
                        text.setGravity(Gravity.CENTER);
                        toast.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(Activity_SignIn.this, getResources().getString(R.string.failure2), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 400);
                        View view = toast.getView();
                        view.getBackground().setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                        TextView text = view.findViewById(android.R.id.message);
                        text.setTextColor(getColor(R.color.error));
                        text.setGravity(Gravity.CENTER);
                        toast.show();
                    }
                });
            }
        });

        cancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void onChanges(TextInputLayout email_layout, EditText email_edit, MaterialButton next) {
        email_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkValidateEmail2(email_layout, email_edit, next);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private boolean checkValidateEmail() {
        String email = email_edit.getText().toString();
        if (email.equals("")) {
            email_layout.setError("Поле обязательно для заполнения");
            return false;
        } else if (!isEmailValid(email)) {
            email_layout.setError("Пример: mail@example.com");
            return false;
        } else {
            email_layout.setError(null);
            return true;
        }
    }
    private boolean checkValidateEmail2(TextInputLayout email_layout, EditText edit, MaterialButton next) {
        if (Objects.requireNonNull(edit.getText()).toString().equals("")) {
            email_layout.setError("Поле обязательно для заполнения");
            next.setEnabled(false);
            return false;
        } else if (!isEmailValid(edit.getText().toString())) {
            email_layout.setError("Пример: mail@example.com");
            next.setEnabled(false);
            return false;
        } else if (!containsIgnoreCase(listOfEmail, edit.getText().toString())) {
            email_layout.setError("Email не зарегистрирован");
            next.setEnabled(false);
            return false;
        } else {
            email_layout.setError(null);
            next.setEnabled(true);
            return true;
        }
    }
    private boolean checkValidatePassword() {
        String password = password_edit.getText().toString();
        if (password.equals("")) {
            password_layout.setError("Поле обязательно для заполнения");
            return false;
        } else {
            password_layout.setError(null);
            return true;
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Dialog createDialog(int layout) {
        Dialog dialog = new Dialog(Activity_SignIn.this);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.fon_with_margin));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        return dialog;
    }
    private void placeDialogBottom(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();

        windowParams.gravity = Gravity.BOTTOM;
        window.setAttributes(windowParams);
    }

    private void findAllEmail() {
        userDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfEmail.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String email = String.valueOf(ds.child("email").getValue());
                    listOfEmail.add(email);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean containsIgnoreCase(ArrayList<String> list, String string) {
        for (String i : list) {
            if (i.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
//Должно быть пустым!!!
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