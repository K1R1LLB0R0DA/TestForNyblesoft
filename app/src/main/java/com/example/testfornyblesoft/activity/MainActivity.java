package com.example.testfornyblesoft.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import com.example.testfornyblesoft.R;
import com.example.testfornyblesoft.adapter.FragmentAdapter;
import com.example.testfornyblesoft.fragment.OnListFragmentInteractionListener;
import com.example.testfornyblesoft.fragment.OnNavigationFragmentInteractionListener;

public class MainActivity extends FragmentActivity implements OnNavigationFragmentInteractionListener, OnListFragmentInteractionListener {


    private ViewPager vpMain;
    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initViewPager();
    }

    private void initViewPager() {
        vpMain = findViewById(R.id.vpMain);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), MainActivity.this);
        vpMain.setAdapter(fragmentAdapter);

    }

    @Override
    public void onFragmentInteraction() {

    }
}
