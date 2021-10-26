package space.tyryshkin.jkarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Activity_Profile extends AppCompatActivity {

    private TextView avatar_text, login, city, sex, birthday;
    private RelativeLayout loginStroke, cityStroke, sexStroke, birthdayStroke;
    private Button btn_ready;

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

    public void init() {
        avatar_text = findViewById(R.id.avatar_text);
        login = findViewById(R.id.login);
        city = findViewById(R.id.city);
        sex = findViewById(R.id.sex);
        birthday = findViewById(R.id.birthday);
        loginStroke = findViewById(R.id.loginStroke);
        cityStroke = findViewById(R.id.cityStroke);
        sexStroke = findViewById(R.id.sexStroke);
        birthdayStroke = findViewById(R.id.birthdayStroke);
        btn_ready = findViewById(R.id.btn_ready);
    }

    private void onClicks() {
        loginStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogLogin();
            }
        });
        cityStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogsRegionAndCities(R.layout.dialog_change_region, null);
            }
        });
        sexStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSex();
            }
        });
        birthdayStroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogBirthday();
            }
        });
        btn_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void onTouches() {
        //noinspection AndroidLintClickableViewAccessibility
        loginStroke.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    loginStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    loginStroke.setBackgroundColor(getColor(R.color.white));
                }
                return false;
            }
        });
        //noinspection AndroidLintClickableViewAccessibility
        cityStroke.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    cityStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    cityStroke.setBackgroundColor(getColor(R.color.white));
                }
                return false;
            }
        });
        //noinspection AndroidLintClickableViewAccessibility
        sexStroke.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    sexStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    sexStroke.setBackgroundColor(getColor(R.color.white));
                }
                return false;
            }
        });
        //noinspection AndroidLintClickableViewAccessibility
        birthdayStroke.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    birthdayStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    birthdayStroke.setBackgroundColor(getColor(R.color.white));
                }
                return false;
            }
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
                if (edit.getText().toString().equals("")) {
                    login_layout.setError("Поле обязательно для заполнения");
                    save.setEnabled(false);
                } else if (edit.getText().toString().length() < 5) {
                    login_layout.setError("Минимум 5 символов");
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                login.setText(edit.getText().toString());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogsRegionAndCities(int layoutResourceFile, Dialog previousDialog) {
        Dialog dialog = createDialog(layoutResourceFile);

        RadioGroup radio_group_list = dialog.findViewById(R.id.radio_group_list);
        MaterialButton cancel = dialog.findViewById(R.id.btn_cancel);
        MaterialButton next = dialog.findViewById(R.id.btn_next);

        next.setEnabled(false);

        radio_group_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int position) {
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(position);
                if (previousDialog == null) {
                    region = radioButton.getText().toString();
                } else {
                    city.setText(radioButton.getText().toString());
                }
                next.setEnabled(true);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previousDialog == null) {
                    openDialogsRegionAndCities(R.layout.dialog_change_city, dialog);
                } else {
                    previousDialog.dismiss();
                    dialog.dismiss();
                }
            }
        });

        if (previousDialog == null) {
            ProgressDialog progressDialog = createProgressDialog();
            searchDataFromFirebase(progressDialog, dialog, radio_group_list);
        } else {
            createRadioButtonsProgrammatically(radio_group_list, mapCitiesInRegion.get(region));
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

        radio_group_list_of_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int position) {
                radioButtonSex = (RadioButton) radioGroup.findViewById(position);
                save.setEnabled(true);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sex.setText(radioButtonSex.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

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
        datePicker.setMaxDate(calendar.getTimeInMillis());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
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
            }
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

    private void searchDataFromFirebase(ProgressDialog progressDialog, Dialog dialog, RadioGroup radioGroup) {
        String CITY_KEY = "cities";
        DatabaseReference citiesDataBase = FirebaseDatabase.getInstance().getReference(CITY_KEY);
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

                progressDialog.dismiss();

                ArrayList<String> allRegionsArrayList = new ArrayList<>(mapCitiesInRegion.keySet());
                Collections.sort(allRegionsArrayList, new Comparator<String>() {
                    @Override
                    public int compare(String s, String t1) {
                        return s.compareToIgnoreCase(t1);
                    }
                });

                createRadioButtonsProgrammatically(radioGroup, allRegionsArrayList);

                dialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}