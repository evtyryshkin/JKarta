package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_General_Space_App extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String SPECIFIC_FRAGMENT = "Fragment_General_Home";

    private Window window;

    private Model_User user;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private TextView avatar_text, login, city;
    private CircleImageView profileSimpleImageView, profileImageView;

    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_space_app);

        init();
        setStatusBarColor();

        createNavigationDrawer();

        loadProfileDataFromFirebase();
    }

    private void init() {
        if (getIntent().getStringExtra("SPECIFIC_FRAGMENT") != null) {
            SPECIFIC_FRAGMENT = getIntent().getStringExtra("SPECIFIC_FRAGMENT");
        }

        window = Activity_General_Space_App.this.getWindow();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        avatar_text = headerView.findViewById(R.id.avatar_text);
        login = headerView.findViewById(R.id.login);
        city = headerView.findViewById(R.id.city);
        profileSimpleImageView = headerView.findViewById(R.id.profile_simple_image);
        profileImageView = headerView.findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    private void createNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        if (SPECIFIC_FRAGMENT.equals("Fragment_General_Settings")) {
            changeFragment(new Fragment_General_Settings(), true);
            navigationView.setCheckedItem(R.id.nav_settings);
        } else {
            changeFragment(new Fragment_General_Home(), true);
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                changeFragment(new Fragment_General_Home(), true);
                break;
            case R.id.nav_cards:
                changeFragment(new Fragment_General_Cards(), true);
                break;
            case R.id.nav_settings:
                changeFragment(new Fragment_General_Settings(), true);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeFragment(Fragment fragment, boolean needToAddBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragment.getClass().getName());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (needToAddBackStack) {
            fragmentTransaction.addToBackStack(fragment.getTag());
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();

            int index = fragmentManager.getBackStackEntryCount() - 2;
            FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tag = backStackEntry.getName();
            Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment instanceof Fragment_General_Home) {
                navigationView.setCheckedItem(R.id.nav_home);
            } else if (fragment instanceof Fragment_General_Cards) {
                navigationView.setCheckedItem(R.id.nav_cards);
            } else if (fragment instanceof Fragment_General_Settings) {
                navigationView.setCheckedItem(R.id.nav_settings);
            }
        } else {
            finishAndRemoveTask();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (toggle != null) {
            toggle.syncState();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.fragment_container) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadProfileDataFromFirebase() {
        ProgressDialog progressDialog = createProgressDialog();

        userDataBase.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(Model_User.class);

                assert user != null;
                if (user.getImage() != null && !user.getImage().equals("")) {
                    profileSimpleImageView.setVisibility(View.INVISIBLE);
                    avatar_text.setVisibility(View.INVISIBLE);
                    profileImageView.setVisibility(View.VISIBLE);
                    Picasso.get().load(user.getImage()).into(profileImageView);
                }

                login.setText(user.getLogin());
                city.setText(user.getCity());

                avatar_text.setText(Objects.requireNonNull(login.getText()).toString().substring(0, 1).toUpperCase());

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(Activity_General_Space_App.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialog_progress_bar);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return progressDialog;
    }

    private void setStatusBarColor() {
        window.setStatusBarColor(ContextCompat.getColor(Activity_General_Space_App.this, R.color.primary_500));
    }
}