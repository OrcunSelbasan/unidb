package com.example.unidb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Application;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.database = new Database(getApplication());
        //initialize tabLayout
        initTabLayout(getApplication());
    }

    public void initTabLayout(Application app) {
        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), app, this.database);
        viewPager.setAdapter(viewPagerAdapter);
        // Get TabLayout from the layout file
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Link TabLayout with ViewPager
        tabLayout.setupWithViewPager(viewPager);
    }
    //create option menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    //when the option is selected toast message will be displayed
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {//
            Toast.makeText(getApplication(), Constants.menuAboutMessage, Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}