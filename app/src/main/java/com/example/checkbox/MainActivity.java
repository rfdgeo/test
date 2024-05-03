package com.example.checkbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    FragmentTransaction transaction;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment = new BreakfastFragment();

        findViewById(R.id.filtersTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment();

            }
        });
    }


    public void dismissFragment() {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.popBackStackImmediate();
        onBackPressed();
    }

    private void showFragment() {
        transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(R.id.frame, fragment, "filter");
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}