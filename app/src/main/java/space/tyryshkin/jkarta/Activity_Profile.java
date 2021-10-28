package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Profile extends AppCompatActivity {

    private Model_User user;

    private TextView avatar_text, login, city, sex, birthday;
    private RelativeLayout loginStroke, cityStroke, sexStroke, birthdayStroke;
    private CircleImageView profileSimpleImageView, profileImageView;
    private Button btn_ready;
    private ImageButton btn_remove_image;

    private FirebaseAuth mAuth;
    private DatabaseReference userDataBase;
    private String USER_KEY = "users";
    private Uri imageUri;
    private String myUri;
    private StorageReference storageProfileAvatar;
    private String CITY_KEY = "cities";
    private DatabaseReference citiesDataBase;

    private ArrayList<String> listOfLogin = new ArrayList<>();
    Map<String, ArrayList<String>> mapCitiesInRegion = new HashMap<>();
    String region; //Так и не нашел способа локализовать переменную в методе

    RadioButton radioButtonSex; //Так и не нашел способа локализовать переменную в методе

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        onClicks();
        onTouches();
    }

    @SuppressLint("CutPasteId")
    public void init() {
        profileSimpleImageView = findViewById(R.id.profile_simple_image);
        profileImageView = findViewById(R.id.profile_image);
        avatar_text = findViewById(R.id.avatar_text);
        login = findViewById(R.id.login);
        city = findViewById(R.id.city);
        sex = findViewById(R.id.sex);
        birthday = findViewById(R.id.birthday);
        loginStroke = findViewById(R.id.loginStroke);
        cityStroke = findViewById(R.id.cityStroke);
        sexStroke = findViewById(R.id.sexStroke);
        birthdayStroke = findViewById(R.id.birthdayStroke);

        btn_remove_image = findViewById(R.id.btn_remove_image);
        btn_ready = findViewById(R.id.btn_ready);

        mAuth = FirebaseAuth.getInstance();
        userDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        storageProfileAvatar = FirebaseStorage.getInstance().getReference().child("Avatars");

        citiesDataBase = FirebaseDatabase.getInstance().getReference(CITY_KEY);
        searchCitiesFromFirebase(null, null, null);

        loadProfileDataFromFirebase();
        listOfLogin = findAllLogin();
    }

    private void onClicks() {
        profileSimpleImageView.setOnClickListener(view -> {
            CropImage.activity().setAspectRatio(1, 1).start(this);
        });
        profileImageView.setOnClickListener(view -> {
            CropImage.activity().setAspectRatio(1, 1).start(this);
        });

        loginStroke.setOnClickListener(view -> openDialogLogin());

        cityStroke.setOnClickListener(view -> openDialogsRegionAndCities(R.layout.dialog_change_region, null));

        sexStroke.setOnClickListener(view -> openDialogSex());

        birthdayStroke.setOnClickListener(view -> openDialogBirthday());

        btn_remove_image.setOnClickListener(view -> removeImage());

        btn_ready.setOnClickListener(view -> {
            uploadProfileImage();
            saveProfileDataToFirebase();
            Intent intent = new Intent(Activity_Profile.this, Activity_General_Space_App.class);
            startActivity(intent);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void onTouches() {
        //noinspection AndroidLintClickableViewAccessibility
        btn_remove_image.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                btn_remove_image.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                btn_remove_image.setBackgroundColor(getColor(R.color.white));
            }
            return false;
        });

        //noinspection AndroidLintClickableViewAccessibility
        loginStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                loginStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                loginStroke.setBackgroundColor(getColor(R.color.white));
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        cityStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                cityStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                cityStroke.setBackgroundColor(getColor(R.color.white));
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        sexStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                sexStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                sexStroke.setBackgroundColor(getColor(R.color.white));
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        birthdayStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                birthdayStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                birthdayStroke.setBackgroundColor(getColor(R.color.white));
            }
            return false;
        });
    }

    private void openDialogLogin() {
        Dialog dialog = createDialog(R.layout.dialog_change_login);

        placeDialogBottom(dialog);

        TextInputLayout login_layout = dialog.findViewById(R.id.login_layout);
        TextInputEditText edit = dialog.findViewById(R.id.edit);
        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton save = dialog.findViewById(R.id.btn_save);

        edit.setText(login.getText());

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (Objects.requireNonNull(edit.getText()).toString().equals("")) {
                    login_layout.setError("Поле обязательно для заполнения");
                    save.setEnabled(false);
                } else if (edit.getText().toString().length() < 5) {
                    login_layout.setError("Минимум 5 символов");
                    save.setEnabled(false);
                } else if (edit.getText().toString().contains(" ")) {
                    login_layout.setError("Пробелы не разрешены");
                    save.setEnabled(false);
                } else if (containsIgnoreCase(listOfLogin, edit.getText().toString())) {
                    login_layout.setError("Логин уже существует");
                    save.setEnabled(false);
                } else {
                    login_layout.setError(null);
                    save.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        save.setOnClickListener(view -> {
            dialog.dismiss();
            avatar_text.setText(Objects.requireNonNull(edit.getText()).toString().substring(0, 1).toUpperCase());
            login.setText(Objects.requireNonNull(edit.getText()).toString());
        });

        cancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void openDialogsRegionAndCities(int layoutResourceFile, Dialog previousDialog) {
        Dialog dialog = createDialog(layoutResourceFile);

        RadioGroup radio_group_list = dialog.findViewById(R.id.radio_group_list);
        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton next = dialog.findViewById(R.id.btn_next);

        next.setEnabled(false);

        radio_group_list.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (radioGroup, position) -> {
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(position);
            if (previousDialog == null) {
                region = radioButton.getText().toString();
            } else {
                city.setText(radioButton.getText().toString());
            }
            next.setEnabled(true);
        });

        cancel.setOnClickListener(view -> dialog.dismiss());

        next.setOnClickListener(view -> {
            if (previousDialog == null) {
                openDialogsRegionAndCities(R.layout.dialog_change_city, dialog);
            } else {
                previousDialog.dismiss();
                dialog.dismiss();
            }
        });

        if (previousDialog == null) {
            ProgressDialog progressDialog = createProgressDialog();
            searchCitiesFromFirebase(progressDialog, dialog, radio_group_list);
        } else {
            createRadioButtonsProgrammatically(radio_group_list, Objects.requireNonNull(mapCitiesInRegion.get(region)));
            dialog.show();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void openDialogSex() {
        Dialog dialog = createDialog(R.layout.dialog_change_sex);
        placeDialogBottom(dialog);

        RadioGroup radio_group_list_of_sex = dialog.findViewById(R.id.radio_group_list_of_sex);
        RadioButton radio_btn_man = dialog.findViewById(R.id.radio_btn_man);
        RadioButton radio_btn_woman = dialog.findViewById(R.id.radio_btn_woman);
        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton save = dialog.findViewById(R.id.btn_save);

        save.setEnabled(false);

        if (sex != null && !sex.getText().toString().equals("")) {
            if (radio_btn_woman.getText().toString().equals(sex.getText().toString())) {
                radio_btn_woman.setChecked(true);
                radioButtonSex = radio_btn_woman;
            } else {
                radio_btn_man.setChecked(true);
                radioButtonSex = radio_btn_man;
            }
            save.setEnabled(true);
        }

        createColorOfRadioButton(radio_btn_man);
        createColorOfRadioButton(radio_btn_woman);

        radio_group_list_of_sex.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (radioGroup, position) -> {
            radioButtonSex = (RadioButton) radioGroup.findViewById(position);
            save.setEnabled(true);
        });

        cancel.setOnClickListener(view -> dialog.dismiss());

        save.setOnClickListener(view -> {
            sex.setText(radioButtonSex.getText().toString());
            dialog.dismiss();
        });
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void openDialogBirthday() {
        Dialog dialog = createDialog(R.layout.dialog_change_birthday);
        placeDialogBottom(dialog);

        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton save = dialog.findViewById(R.id.btn_save);

        DatePicker datePicker = dialog.findViewById(R.id.dialog_datePicker);
        Calendar calendar = Calendar.getInstance();
        if (!birthday.getText().equals("")) {
            int year = Integer.parseInt(birthday.getText().toString().substring(6));
            int month = Integer.parseInt(birthday.getText().toString().substring(3, 5));
            int day = Integer.parseInt(birthday.getText().toString().substring(0, 2));
            datePicker.updateDate(year, month - 1, day);
        }
        calendar.set(calendar.get(Calendar.YEAR) - 18, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        if (!login.getText().toString().equals("JKarta")) {
            datePicker.setMaxDate(calendar.getTimeInMillis());
        }

        cancel.setOnClickListener(view -> dialog.dismiss());

        save.setOnClickListener(view -> {
            String year = String.valueOf(datePicker.getYear());
            String month = String.valueOf(datePicker.getMonth() + 1);
            if (month.length() < 2) {
                month = "0" + month;
            }
            String day = String.valueOf(datePicker.getDayOfMonth());
            if (day.length() < 2) {
                day = "0" + day;
            }
            birthday.setText(day + "." + month + "." + year);
            dialog.dismiss();
        });
        dialog.show();
    }

    private ProgressDialog createProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(Activity_Profile.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialog_progress_bar);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return progressDialog;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Dialog createDialog(int layout) {
        Dialog dialog = new Dialog(Activity_Profile.this);
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

    private void createRadioButtonsProgrammatically(RadioGroup radioGroup, ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            RadioButton radioButton = new RadioButton(getApplicationContext());
            radioButton.setPadding(30, 0, 0, 0);

            radioButton.setText(list.get(i));
            radioButton.setTextColor(getColor(R.color.secondary_500));
            radioButton.setTextSize(14);

            createColorOfRadioButton(radioButton);

            radioGroup.addView(radioButton);
        }
    }

    private void createColorOfRadioButton(RadioButton radioButton) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        getResources().getColor(R.color.dark_grey),
                        getResources().getColor(R.color.primary_500),
                }
        );
        radioButton.setButtonTintList(colorStateList);
        radioButton.invalidate();
    }

    private void searchCitiesFromFirebase(ProgressDialog progressDialog, Dialog dialog, RadioGroup radioGroup) {
        citiesDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mapCitiesInRegion.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String region = String.valueOf(ds.child("region").getValue());
                    String city = String.valueOf(ds.child("city").getValue());

                    if (!mapCitiesInRegion.containsKey(region)) {
                        mapCitiesInRegion.put(region, new ArrayList<>());
                    }
                    mapCitiesInRegion.get(region).add(city);
                }

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                ArrayList<String> allRegionsArrayList = new ArrayList<>(mapCitiesInRegion.keySet());
                Collections.sort(allRegionsArrayList, String::compareToIgnoreCase);

                if (radioGroup != null) {
                    createRadioButtonsProgrammatically(radioGroup, allRegionsArrayList);
                }

                if (dialog != null) {
                    dialog.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
                    btn_remove_image.setVisibility(View.VISIBLE);
                    profileImageView.setVisibility(View.VISIBLE);
                    Picasso.get().load(user.getImage()).into(profileImageView);
                }
                assert user != null;
                if (user.getLogin().equals("")) {
                    login.setText(user.getEmail());
                } else {
                    login.setText(user.getLogin());
                }
                avatar_text.setText(Objects.requireNonNull(login.getText()).toString().substring(0, 1).toUpperCase());
                city.setText(user.getCity());
                sex.setText(user.getSex());
                birthday.setText(user.getBirthday());

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void removeImage() {
        profileSimpleImageView.setVisibility(View.VISIBLE);
        avatar_text.setVisibility(View.VISIBLE);
        avatar_text.setText(Objects.requireNonNull(login.getText()).toString().substring(0, 1).toUpperCase());
        btn_remove_image.setVisibility(View.INVISIBLE);
        profileImageView.setVisibility(View.INVISIBLE);
    }

    private void saveProfileDataToFirebase() {
        ProgressDialog progressDialog = createProgressDialog();

        user.setLogin(login.getText().toString());
        user.setCity(city.getText().toString());
        user.setSex(sex.getText().toString());
        user.setBirthday(birthday.getText().toString());

        userDataBase.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(user);

        progressDialog.dismiss();
    }

    //Устанавливает аватар, после обрезки на своё место
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileSimpleImageView.setVisibility(View.INVISIBLE);
            avatar_text.setVisibility(View.INVISIBLE);
            btn_remove_image.setVisibility(View.VISIBLE);
            profileImageView.setVisibility(View.VISIBLE);
            profileImageView.setImageURI(imageUri);
        }
    }

    private void uploadProfileImage() {
        if (imageUri != null) {
            StorageReference storageUser = storageProfileAvatar
                    .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + ".jpg");
            StorageTask uploadTask = storageUser.putFile(imageUri);

            uploadTask.continueWithTask((Continuation) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageUser.getDownloadUrl();
            }).addOnCompleteListener((OnCompleteListener<Uri>) task -> {
                if (task.isSuccessful()) {
                    Uri downloadUrl = task.getResult();
                    assert downloadUrl != null;
                    myUri = downloadUrl.toString();

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("image", myUri);

                    userDataBase.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);
                }
            });
        }
    }

    private ArrayList<String> findAllLogin() {
        userDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String login = String.valueOf(ds.child("login").getValue());
                    listOfLogin.add(login);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return listOfLogin;
    }

    private boolean containsIgnoreCase(ArrayList<String> list, String string) {
        for (String i : list) {
            if (i.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }
}