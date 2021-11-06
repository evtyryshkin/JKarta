package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.Executor;

public class Activity_Security extends AppCompatActivity {

    private RelativeLayout passwordStroke, accessCodeStroke, biometricStroke;
    private ImageButton btn_back;

    private FirebaseAuth mAuth;
    private androidx.biometric.BiometricManager biometricManager;
    private BiometricPrompt biometricPrompt;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        init();
        onClicks();
        onTouches();
    }

    @SuppressLint("WrongConstant")
    private void init() {
        btn_back = findViewById(R.id.btn_back);
        passwordStroke = findViewById(R.id.passwordStroke);
        accessCodeStroke = findViewById(R.id.accessCodeStroke);
        biometricStroke = findViewById(R.id.biometricStroke);

        mAuth = FirebaseAuth.getInstance();
        biometricManager = androidx.biometric.BiometricManager.from(getApplicationContext());
        if (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK)
                != BiometricManager.BIOMETRIC_SUCCESS) {
            biometricStroke.setVisibility(View.INVISIBLE);
        } else {
            biometricStroke.setVisibility(View.VISIBLE);
        }

        sharedPreferences = getSharedPreferences(Objects.requireNonNull(mAuth.getCurrentUser()).getUid(), MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void onClicks() {
        btn_back.setOnClickListener((view) -> {
            onBackPressed();
        });
        passwordStroke.setOnClickListener((view) -> {
            openDialogChangePassword();
        });
        accessCodeStroke.setOnClickListener((view) -> {
            openDialogChangeAccessCode();
        });
        biometricStroke.setOnClickListener((view) -> {
            openDialogFingerPrint();
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
        //noinspection AndroidLintClickableViewAccessibility
        passwordStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                passwordStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                passwordStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        accessCodeStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                accessCodeStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                accessCodeStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        biometricStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                biometricStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                biometricStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_white));
                return false;
            }
            return false;
        });
    }

    private void openDialogChangePassword() {
        Dialog dialog = createDialog(R.layout.dialog_change_password);

        placeDialogBottom(dialog);

        TextInputLayout password_layout = dialog.findViewById(R.id.password_layout);
        TextInputEditText password_edit = dialog.findViewById(R.id.password_edit);
        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton save = dialog.findViewById(R.id.btn_save);

        save.setOnClickListener(view -> {
            onChanges(password_layout, password_edit, save);
            String mail = password_edit.getText().toString();

            if (checkValidatePassword(password_layout, password_edit, save)) {
                mAuth.getCurrentUser().updatePassword(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        createCustomToast(getResources().getString(R.string.success_change_password), getColor(R.color.teal_500));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        createCustomToast(getResources().getString(R.string.failure1), getColor(R.color.error));
                    }
                });
            }
        });

        cancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void onChanges(TextInputLayout password_layout, EditText password_edit, MaterialButton next) {
        password_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkValidatePassword(password_layout, password_edit, next);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private boolean checkValidatePassword(TextInputLayout password_layout, EditText password_edit, MaterialButton save) {
        String password = password_edit.getText().toString();
        if (password.equals("")) {
            password_layout.setError("Поле обязательно для заполнения");
            save.setEnabled(false);
            return false;
        } else if (password.length() < 6) {
            password_layout.setError("Минимум 6 символов");
            save.setEnabled(false);
            return false;
        } else {
            password_layout.setError(null);
            return true;
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void openDialogChangeAccessCode() {
        Dialog dialog = createDialog(R.layout.dialog_set_access_code);
        placeDialogBottom(dialog);

        MaterialButton no = dialog.findViewById(R.id.btn_no);
        MaterialButton yes = dialog.findViewById(R.id.btn_yes);

        yes.setOnClickListener(view -> {
            dialog.dismiss();
            Intent intent = new Intent(Activity_Security.this, Activity_Pin_Code_Create.class);
            intent.putExtra("FROM_ACTIVITY", "Activity_Security");
            startActivity(intent);
        });

        no.setOnClickListener(view -> {
            dialog.dismiss();

            if (sharedPreferences.getString("PREFERENCES_PIN", "").equals("")) {
                createCustomToast(getResources().getString(R.string.cancel_access_code), getColor(R.color.error));
            } else {
                createCustomToast(getResources().getString(R.string.remove_access_code), getColor(R.color.error));
            }

            editor.putString(Model_User.PREFERENCES_PIN, "");
            editor.apply();
        });
        dialog.show();
    }

    @SuppressLint("WrongConstant")
    private void openDialogFingerPrint() {
        if (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK)
                == BiometricManager.BIOMETRIC_SUCCESS) {
            Dialog dialog = createDialog(R.layout.dialog_set_finger_print);
            placeDialogBottom(dialog);

            MaterialButton no = dialog.findViewById(R.id.btn_no);
            MaterialButton yes = dialog.findViewById(R.id.btn_yes);

            yes.setOnClickListener(view -> {
                dialog.dismiss();
                invokeDialogFingerprint();
            });

            no.setOnClickListener(view -> {
                dialog.dismiss();
                if (sharedPreferences.getBoolean("PREFERENCES_IS_HAS_FINGERPRINT", false)) {
                    createCustomToast(getResources().getString(R.string.cancel_finger_print), getColor(R.color.error));
                } else {
                    createCustomToast(getResources().getString(R.string.remove_finger_print), getColor(R.color.error));
                }
                editor.putBoolean(Model_User.PREFERENCES_IS_HAS_FINGERPRINT, false);
                editor.apply();
            });
            dialog.show();
        }
    }

    private void invokeDialogFingerprint() {
        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(Activity_Security.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                editor.putBoolean(Model_User.PREFERENCES_IS_HAS_FINGERPRINT, false);
                editor.apply();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                createCustomToast(getResources().getString(R.string.success), getColor(R.color.teal_500));
                editor.putBoolean(Model_User.PREFERENCES_IS_HAS_FINGERPRINT, true);
                editor.apply();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                editor.putBoolean(Model_User.PREFERENCES_IS_HAS_FINGERPRINT, false);
                editor.apply();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.add_finger_print))
                .setNegativeButtonText(getResources().getString(R.string.cancel2))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Dialog createDialog(int layout) {
        Dialog dialog = new Dialog(Activity_Security.this);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Activity_Security.this, Activity_General_Space_App.class);
        intent.putExtra("SPECIFIC_FRAGMENT", "Fragment_General_Settings");
        startActivity(intent);
    }
}