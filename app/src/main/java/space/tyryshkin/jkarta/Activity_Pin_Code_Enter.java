package space.tyryshkin.jkarta;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Pin_Code_Enter extends AppCompatActivity {

    private Window window;

    private CircleImageView profile_simple_image, profile_image;
    private TextView avatar_text, login, num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, exit;
    private ImageView pin_1, pin_2, pin_3, pin_4, backspace;

    private String pin_code = "";

    private Model_User user;
    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";
    private androidx.biometric.BiometricManager biometricManager;
    private BiometricPrompt biometricPrompt;

    private ArrayList<ImageView> listOfPin = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_enter);

        init();
        setStatusBarColor();
        setUserData();
        onClicks();
        onTouches();

    }

    @SuppressLint("SwitchIntDef")
    private void init() {
        window = Activity_Pin_Code_Enter.this.getWindow();
        profile_simple_image = findViewById(R.id.profile_simple_image);
        profile_image = findViewById(R.id.profile_image);
        avatar_text = findViewById(R.id.avatar_text);
        login = findViewById(R.id.login);
        num_0 = findViewById(R.id.num_0);
        num_1 = findViewById(R.id.num_1);
        num_2 = findViewById(R.id.num_2);
        num_3 = findViewById(R.id.num_3);
        num_4 = findViewById(R.id.num_4);
        num_5 = findViewById(R.id.num_5);
        num_6 = findViewById(R.id.num_6);
        num_7 = findViewById(R.id.num_7);
        num_8 = findViewById(R.id.num_8);
        num_9 = findViewById(R.id.num_9);
        exit = findViewById(R.id.exit);

        pin_1 = findViewById(R.id.pin_1);
        pin_2 = findViewById(R.id.pin_2);
        pin_3 = findViewById(R.id.pin_3);
        pin_4 = findViewById(R.id.pin_4);
        listOfPin.add(pin_1);
        listOfPin.add(pin_2);
        listOfPin.add(pin_3);
        listOfPin.add(pin_4);

        backspace = findViewById(R.id.backspace);

        user = getIntent().getParcelableExtra("Model_User");
        mAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        if (user.getFingerAuth().equals("yes")) {
            biometricManager = androidx.biometric.BiometricManager.from(getApplicationContext());
            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    backspace.setImageResource(R.drawable.ic_fingerprint);
                    Log.d("FINGER_PRINT", "Приложение может аутентифицировать при помощи сканера отпечатка пальца");
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    Log.e("FINGER_PRINT", "На устройстве отсутствует сканер отпечатка пальца");
                    backspace.setImageResource(R.drawable.ic_backspace);
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Log.e("FINGER_PRINT", "Сканер отпечатка пальца сейчас недоступен");
                    backspace.setImageResource(R.drawable.ic_backspace);
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    backspace.setImageResource(R.drawable.ic_backspace);
                    break;
            }
        }
        invokeDialogFingerprint();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint({"UseCompatLoadingForDrawables", "WrongConstant"})
    private void onClicks() {
        num_0.setOnClickListener(view -> addNumber("0"));
        num_1.setOnClickListener(view -> addNumber("1"));
        num_2.setOnClickListener(view -> addNumber("2"));
        num_3.setOnClickListener(view -> addNumber("3"));
        num_4.setOnClickListener(view -> addNumber("4"));
        num_5.setOnClickListener(view -> addNumber("5"));
        num_6.setOnClickListener(view -> addNumber("6"));
        num_7.setOnClickListener(view -> addNumber("7"));
        num_8.setOnClickListener(view -> addNumber("8"));
        num_9.setOnClickListener(view -> addNumber("9"));
        backspace.setOnClickListener(view -> {
            if (pin_code.length() != 0) {
                removeNumber();
            } else {
                if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS && user.getFingerAuth().equals("yes")) {
                    invokeDialogFingerprint();
                }

            }
        });
        exit.setOnClickListener(view -> {
            openDialogExit();
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void onTouches() {
        //noinspection AndroidLintClickableViewAccessibility
        num_0.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_0.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_0.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_1.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_1.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_1.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_2.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_2.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_2.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_3.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_3.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_3.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_4.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_4.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_4.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_5.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_5.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_5.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_6.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_6.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_6.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_7.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_7.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_7.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_8.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_8.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_8.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        num_9.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                num_9.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                num_9.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        exit.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                exit.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                exit.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        backspace.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                backspace.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                backspace.setBackgroundColor(getColor(R.color.white));
                return false;
            }
            return false;
        });
    }

    private void addNumber(String number) {
        if (pin_code.length() < 4) {
            pin_code = pin_code + number;
            logicPin();
        }
    }

    private void removeNumber() {
        if (pin_code != null && pin_code.length() > 0) {
            pin_code = pin_code.substring(0, pin_code.length() - 1);
            logicPin();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void logicPin() {
        if (pin_code.length() == 0) {
            if (user.getFingerAuth().equals("yes")) {
                backspace.setImageResource(R.drawable.ic_fingerprint);
            } else {
                backspace.setImageResource(R.drawable.ic_backspace);
            }
            setColorPin(listOfPin, 0);
        } else if (pin_code.length() == 1) {
            backspace.setImageResource(R.drawable.ic_backspace);
            setColorPin(listOfPin, 1);
        } else if (pin_code.length() == 2) {
            backspace.setImageResource(R.drawable.ic_backspace);
            setColorPin(listOfPin, 2);
        } else if (pin_code.length() == 3) {
            backspace.setImageResource(R.drawable.ic_backspace);
            setColorPin(listOfPin, 3);
        } else if (pin_code.length() == 4) {
            backspace.setImageResource(R.drawable.ic_backspace);
            setColorPin(listOfPin, 4);

            if (user.getPin_code().equals(pin_code)) {
                Intent intent = new Intent(Activity_Pin_Code_Enter.this, Activity_General_Space_App.class);
                startActivity(intent);
            } else {
                pin_code = "";
                if (user.getFingerAuth().equals("yes")) {
                    backspace.setImageResource(R.drawable.ic_fingerprint);
                } else {
                    backspace.setImageResource(R.drawable.ic_backspace);
                }
                onShakeImage();
            }
        }
    }

    private void setColorPin(ArrayList<ImageView> listOfPin, int count) {
        for (int i = 0; i < 4; i++) {
            listOfPin.get(i).setImageResource(R.drawable.pin_grey);
        }
        for (int i = 0; i < count; i++) {
            listOfPin.get(i).setImageResource(R.drawable.pin_orange);
        }
    }

    private void onShakeImage() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);

        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        pin_1.startAnimation(shake);
        pin_2.startAnimation(shake);
        pin_3.startAnimation(shake);
        pin_4.startAnimation(shake);

        pin_1.setImageResource(R.drawable.pin_error);
        pin_2.setImageResource(R.drawable.pin_error);
        pin_3.setImageResource(R.drawable.pin_error);
        pin_4.setImageResource(R.drawable.pin_error);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pin_1.setImageResource(R.drawable.pin_grey);
                pin_2.setImageResource(R.drawable.pin_grey);
                pin_3.setImageResource(R.drawable.pin_grey);
                pin_4.setImageResource(R.drawable.pin_grey);
            }
        }, 1000);
    }

    private void setUserData() {
        if (user.getLogin().equals("")) {
            login.setText(user.getEmail());
        } else {
            login.setText(user.getLogin());
        }
        avatar_text.setText(Objects.requireNonNull(login.getText()).toString().substring(0, 1).toUpperCase());
        if (user.getImage() != null && !user.getImage().equals("")) {
            profile_simple_image.setVisibility(View.INVISIBLE);
            avatar_text.setVisibility(View.INVISIBLE);
            profile_image.setVisibility(View.VISIBLE);
            Picasso.get().load(user.getImage()).into(profile_image);
        }
    }

    private void setStatusBarColor() {
        window.setStatusBarColor(ContextCompat.getColor(Activity_Pin_Code_Enter.this, R.color.white));
    }

    private void openDialogExit() {
        Dialog dialog = createDialog(R.layout.dialog_exit_pin_code);

        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton save = dialog.findViewById(R.id.btn_save);

        save.setOnClickListener(view -> {
            dialog.dismiss();
            user.setPin_code("");
            user.setFingerAuth("no");
            userDataBase.child(mAuth.getCurrentUser().getUid()).setValue(user);
            Intent intent = new Intent(Activity_Pin_Code_Enter.this, Activity_SignIn.class);
            startActivity(intent);
        });

        cancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Dialog createDialog(int layout) {
        Dialog dialog = new Dialog(Activity_Pin_Code_Enter.this);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.fon_with_margin));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        return dialog;
    }

    private void invokeDialogFingerprint() {
        Executor executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(Activity_Pin_Code_Enter.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Intent intent = new Intent(Activity_Pin_Code_Enter.this, Activity_General_Space_App.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.enter_app))
                .setNegativeButtonText(getResources().getString(R.string.cancel2))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    public void onBackPressed() {
//Должно быть пустым!!!
    }
}