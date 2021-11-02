package space.tyryshkin.jkarta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Objects;

public class Fragment_General_Settings extends Fragment {

    private View view;

    private RelativeLayout profileStroke, authStroke, notificationStroke, applicationStroke;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_general_settings, container, false);

        init();
        onClicks();
        onTouches();

        return view;
    }

    private void init() {
        //Изменяем текст в тулбаре
        Objects.requireNonNull(((AppCompatActivity) requireActivity())
                .getSupportActionBar())
                .setTitle(getResources().getString(R.string.settings));

        profileStroke = view.findViewById(R.id.profileStroke);
        authStroke = view.findViewById(R.id.authStroke);
        notificationStroke = view.findViewById(R.id.notificationStroke);
        applicationStroke = view.findViewById(R.id.applicationStroke);
    }

    private void onClicks() {
        profileStroke.setOnClickListener((view) -> {
            Intent intent = new Intent(getActivity(), Activity_Profile.class);
            startActivity(intent);
        });
        authStroke.setOnClickListener((view) -> {
            Toast.makeText(getActivity(), "Вход и авторизация", Toast.LENGTH_SHORT).show();
        });
        authStroke.setOnClickListener((view) -> {
            Toast.makeText(getActivity(), "Уведомления", Toast.LENGTH_SHORT).show();
        });
        authStroke.setOnClickListener((view) -> {
            Toast.makeText(getActivity(), "Приложение", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void onTouches() {
        //noinspection AndroidLintClickableViewAccessibility
        profileStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                profileStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return false;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                profileStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_white));
                return false;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        authStroke.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                authStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_grey));
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                authStroke.setBackgroundDrawable(getResources().getDrawable(R.drawable.fon_white));
                return true;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        notificationStroke.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                notificationStroke.setBackground(getResources().getDrawable(R.drawable.fon_grey));
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                notificationStroke.setBackground(getResources().getDrawable(R.drawable.fon_white));
                return true;
            }
            return false;
        });
        //noinspection AndroidLintClickableViewAccessibility
        applicationStroke.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                applicationStroke.setBackground(getResources().getDrawable(R.drawable.fon_grey));
                return true;
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                applicationStroke.setBackground(getResources().getDrawable(R.drawable.fon_white));
                return true;
            }
            return false;
        });
    }


}