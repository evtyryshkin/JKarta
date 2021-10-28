package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Pin_Code extends AppCompatActivity {

    private CircleImageView profile_simple_image, profile_image;
    private TextView avatar_text, login, num_0, num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8, num_9, exit;
    private ImageView pin_1, pin_2, pin_3, pin_4, backspace;

    private String pin_code = "";

    private Model_User user;
    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);

        init();
        setUserData();
        onClicks();
        onTouches();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void logicPin() {
        if (pin_code.length() == 0) {
            pin_1.setImageResource(R.drawable.pin_grey);
            pin_2.setImageResource(R.drawable.pin_grey);
            pin_3.setImageResource(R.drawable.pin_grey);
            pin_4.setImageResource(R.drawable.pin_grey);
        } else if (pin_code.length() == 1) {
            pin_1.setImageResource(R.drawable.pin_orange);
            pin_2.setImageResource(R.drawable.pin_grey);
            pin_3.setImageResource(R.drawable.pin_grey);
            pin_4.setImageResource(R.drawable.pin_grey);
        } else if (pin_code.length() == 2) {
            pin_1.setImageResource(R.drawable.pin_orange);
            pin_2.setImageResource(R.drawable.pin_orange);
            pin_3.setImageResource(R.drawable.pin_grey);
            pin_4.setImageResource(R.drawable.pin_grey);
        } else if (pin_code.length() == 3) {
            pin_1.setImageResource(R.drawable.pin_orange);
            pin_2.setImageResource(R.drawable.pin_orange);
            pin_3.setImageResource(R.drawable.pin_orange);
            pin_4.setImageResource(R.drawable.pin_grey);
        } else if (pin_code.length() == 4) {
            pin_1.setImageResource(R.drawable.pin_orange);
            pin_2.setImageResource(R.drawable.pin_orange);
            pin_3.setImageResource(R.drawable.pin_orange);
            pin_4.setImageResource(R.drawable.pin_orange);

            if (user.getPin_code().equals(pin_code)) {
                Intent intent = new Intent(Activity_Pin_Code.this, Activity_General_Space_App.class);
                startActivity(intent);
            }
        }
    }

    private void init() {
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
        backspace = findViewById(R.id.backspace);

        mAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    private void onClicks() {
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
            removeNumber();
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
        if (pin_code.length() < 5) {
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

    private void setUserData() {
        userDataBase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(Model_User.class);

                assert user != null;
                if (user.getImage() != null && !user.getImage().equals("")) {
                    profile_simple_image.setVisibility(View.INVISIBLE);
                    avatar_text.setVisibility(View.INVISIBLE);
                    profile_image.setVisibility(View.VISIBLE);
                    Picasso.get().load(user.getImage()).into(profile_image);
                }
                assert user != null;
                if (user.getLogin().equals("")) {
                    login.setText(user.getEmail());
                } else {
                    login.setText(user.getLogin());
                }
                avatar_text.setText(Objects.requireNonNull(login.getText()).toString().substring(0, 1).toUpperCase());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}