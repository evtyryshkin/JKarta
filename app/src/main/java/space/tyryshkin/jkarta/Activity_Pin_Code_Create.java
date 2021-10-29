package space.tyryshkin.jkarta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Activity_Pin_Code_Create extends AppCompatActivity {
    private Window window;

    private TextView set_access_code, num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9;
    private ImageView pin_1, pin_2, pin_3, pin_4, pin_5, pin_6, pin_7, pin_8, backspace;
    LinearLayout layout_pin2;
    private ImageButton skip;

    private String pin_code_1 = "";
    private String pin_code_2 = "";

    private Model_User user;
    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";

    private ArrayList<ImageView> listOfPin1 = new ArrayList<>();
    private ArrayList<ImageView> listOfPin2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_create);

        init();
        setStatusBarColor();
        onClicks();
        onTouches();
    }

    private void init() {
        window = Activity_Pin_Code_Create.this.getWindow();

        skip = findViewById(R.id.skip);
        set_access_code = findViewById(R.id.set_access_code);
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

        pin_1 = findViewById(R.id.pin_1);
        pin_2 = findViewById(R.id.pin_2);
        pin_3 = findViewById(R.id.pin_3);
        pin_4 = findViewById(R.id.pin_4);
        layout_pin2 = findViewById(R.id.pin2);
        pin_5 = findViewById(R.id.pin_5);
        pin_6 = findViewById(R.id.pin_6);
        pin_7 = findViewById(R.id.pin_7);
        pin_8 = findViewById(R.id.pin_8);
        listOfPin1.add(pin_1);
        listOfPin1.add(pin_2);
        listOfPin1.add(pin_3);
        listOfPin1.add(pin_4);
        listOfPin2.add(pin_5);
        listOfPin2.add(pin_6);
        listOfPin2.add(pin_7);
        listOfPin2.add(pin_8);

        backspace = findViewById(R.id.backspace);

        user = getIntent().getParcelableExtra("Model_User");
        mAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    private void onClicks() {
        skip.setOnClickListener(view -> {
            Intent intent = new Intent(Activity_Pin_Code_Create.this, Activity_Profile.class);
            startActivity(intent);
        });
        num_0.setOnClickListener(view -> {
            addNumber("0");
        });
        num_1.setOnClickListener(view -> {
            addNumber("1");
        });
        num_2.setOnClickListener(view -> {
            addNumber("2");
        });
        num_3.setOnClickListener(view -> {
            addNumber("3");
        });
        num_4.setOnClickListener(view -> {
            addNumber("4");
        });
        num_5.setOnClickListener(view -> {
            addNumber("5");
        });
        num_6.setOnClickListener(view -> {
            addNumber("6");
        });
        num_7.setOnClickListener(view -> {
            addNumber("7");
        });
        num_8.setOnClickListener(view -> {
            addNumber("8");
        });
        num_9.setOnClickListener(view -> {
            addNumber("9");
        });
        backspace.setOnClickListener(view -> {
            if (pin_code_1.length() < 4){
                if (pin_code_1.length() > 0) {
                    pin_code_1 = pin_code_1.substring(0, pin_code_1.length() - 1);
                    logicPin(listOfPin1, pin_code_1);
                }
            } else {
                if (pin_code_2.length() > 0) {
                    pin_code_2 = pin_code_2.substring(0, pin_code_2.length() - 1);
                    logicPin(listOfPin2, pin_code_2);
                }
            }
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
        //noinspection AndroidLintClickableViewAccessibility
        skip.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                skip.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                skip.setBackgroundColor(getColor(R.color.white));
            }
            return false;
        });
    }

    private void addNumber(String number) {
        if (pin_code_1.length() < 4) {
            pin_code_1 = pin_code_1 + number;
            logicPin(listOfPin1, pin_code_1);
        } else if (pin_code_1.length() == 4) {
            pin_code_2 = pin_code_2 + number;
            logicPin(listOfPin2, pin_code_2);
        }
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ShowToast"})
    private void logicPin(ArrayList<ImageView> listOfPin, String pin_code) {
        if (pin_code.length() == 0) {
            setColorPin(listOfPin, 0);
        } else if (pin_code.length() == 1) {
            setColorPin(listOfPin, 1);
        } else if (pin_code.length() == 2) {
            setColorPin(listOfPin, 2);
        } else if (pin_code.length() == 3) {
            setColorPin(listOfPin, 3);
        } else if (pin_code.length() == 4) {
            setColorPin(listOfPin, 4);

            if (pin_code_2.length() == 0) {
                set_access_code.setText(R.string.repeat_access_code);
                layout_pin2.setVisibility(View.VISIBLE);
            } else {
                if (pin_code_1.equals(pin_code_2)) {
                    user.setPin_code(pin_code_1);
                    userDataBase.child(mAuth.getCurrentUser().getUid()).setValue(user);
                    Intent intent = new Intent(Activity_Pin_Code_Create.this, Activity_Profile.class);
                    startActivity(intent);
                } else {
                    pin_code_1 = "";
                    pin_code_2 = "";
                    onShakeImage();

                    Toast toast = Toast.makeText(this, "Коды доступа не совпадают", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    View view = toast.getView();
                    view.getBackground().setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                    TextView text = view.findViewById(android.R.id.message);
                    text.setTextColor(getColor(R.color.error));
                    toast.show();
                }
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
        pin_5.startAnimation(shake);
        pin_6.startAnimation(shake);
        pin_7.startAnimation(shake);
        pin_8.startAnimation(shake);

        pin_1.setImageResource(R.drawable.pin_error);
        pin_2.setImageResource(R.drawable.pin_error);
        pin_3.setImageResource(R.drawable.pin_error);
        pin_4.setImageResource(R.drawable.pin_error);
        pin_5.setImageResource(R.drawable.pin_error);
        pin_6.setImageResource(R.drawable.pin_error);
        pin_7.setImageResource(R.drawable.pin_error);
        pin_8.setImageResource(R.drawable.pin_error);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pin_1.setImageResource(R.drawable.pin_grey);
                pin_2.setImageResource(R.drawable.pin_grey);
                pin_3.setImageResource(R.drawable.pin_grey);
                pin_4.setImageResource(R.drawable.pin_grey);
                pin_5.setImageResource(R.drawable.pin_grey);
                pin_6.setImageResource(R.drawable.pin_grey);
                pin_7.setImageResource(R.drawable.pin_grey);
                pin_8.setImageResource(R.drawable.pin_grey);
                set_access_code.setText(R.string.set_access_code);
                layout_pin2.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    private void setStatusBarColor() {
        window.setStatusBarColor(ContextCompat.getColor(Activity_Pin_Code_Create.this, R.color.white));
    }
}