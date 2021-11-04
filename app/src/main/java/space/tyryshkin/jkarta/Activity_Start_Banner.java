package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Start_Banner extends AppCompatActivity {

    private Model_User user;

    private Window window;

    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";

    private ArrayList<String> listOfUID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_banner);

        init();
        setStatusBarColor();
    }

    private void init() {
        window = Activity_Start_Banner.this.getWindow();

        mAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    private void setStatusBarColor() {
        window.setStatusBarColor(ContextCompat.getColor(Activity_Start_Banner.this, R.color.white));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        Intent intent;
        if (currentUser != null) {
            intent = new Intent(Activity_Start_Banner.this, Activity_SignIn.class);
        } else {
            intent = new Intent(Activity_Start_Banner.this, Activity_SignIn.class);
        }
        startActivity(intent);
            /*userDataBase.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/



                    /*.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Intent intent = new Intent(Activity_Start_Banner.this, Activity_SignIn.class);
                        startActivity(intent);
                    } else {

                    }
                }
            });
        } else {
            Intent intent = new Intent(Activity_Start_Banner.this, Activity_SignIn.class);
            startActivity(intent);*/


        /*if (currentUser != null) {
            userDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean indicator = false;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        user = dataSnapshot.getValue(Model_User.class);

                        assert user != null;
                        if (!user.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                            user.setEmail(mAuth.getCurrentUser().getEmail());

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("email", mAuth.getCurrentUser().getEmail());

                            userDataBase.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                        }

                        assert user != null;
                        if (user.getID().equals(mAuth.getCurrentUser().getUid()) && !user.getPin_code().equals("")) {
                            indicator = true;
                            Intent intent = new Intent(Activity_Start_Banner.this, Activity_Pin_Code_Enter.class);
                            intent.putExtra("Model_User", user);
                            startActivity(intent);
                        }
                    }

                    //Это сделано для того, чтобы если вдруг какой-то user будет удален, то запускалась регистрация
                    Handler handler = new Handler();
                    boolean finalIndicator = indicator;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!finalIndicator) {
                                Intent intent = new Intent(Activity_Start_Banner.this, Activity_SignIn.class);
                                startActivity(intent);
                            }
                        }
                    }, 3000);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Intent intent = new Intent(Activity_Start_Banner.this, Activity_SignIn.class);
            startActivity(intent);
        }*/
    }
}