package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;

public class Activity_Start_Banner extends AppCompatActivity {

    private Window window;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_banner);

        init();
        setStatusBarColor();
        checkExistsInDB();
    }

    @SuppressLint("CommitPrefEdits")
    private void init() {
        window = Activity_Start_Banner.this.getWindow();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        if (currentUser != null) {
            sharedPreferences = getSharedPreferences((currentUser).getUid(), MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }

    }

    private void setStatusBarColor() {
        window.setStatusBarColor(ContextCompat.getColor(Activity_Start_Banner.this, R.color.white));
    }

    private void checkExistsInDB() {
        if (currentUser != null) {
            userDataBase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        editor.putString(Model_User.PREFERENCES_PIN, "");
                        editor.putBoolean(Model_User.PREFERENCES_IS_HAS_FINGERPRINT, false);
                        editor.apply();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent;
        if (currentUser != null && !sharedPreferences.getString(Model_User.PREFERENCES_PIN, "").equals("")) {
            intent = new Intent(Activity_Start_Banner.this, Activity_Pin_Code_Enter.class);
        } else {
            intent = new Intent(Activity_Start_Banner.this, Activity_SignIn.class);
        }
        startActivity(intent);
    }
}