package com.example.unidb;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final Application app;
    private final Database database;

    public ViewPagerAdapter(FragmentManager fragmentManager, Application app, Database database) {
        super(fragmentManager);
        this.app = app;
        this.database = database;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Set default fragment
        Fragment fragment = new FragmentAdministration();
        try {
            switch (position) {
                case 0:
                    fragment = new FragmentAdministration();
                    break;
                case 1:
                    fragment = new FragmentRegistration();
                    break;
                case 2:
                    fragment = new FragmentStudents();
                    break;
                default:
                    // Set default fragment again just in case
                    fragment = new FragmentAdministration();
                    throw new Exception("Invalid Fragment");
            }
        } catch (Exception e) {
            Toast.makeText(this.app, e.getMessage(), Toast.LENGTH_SHORT).show();
            return fragment;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        try {
            switch (position) {
                case 0:
                    title = "Administration";
                    break;
                case 1:
                    title = "Registration";
                    break;
                case 2:
                    title = "Students";
                    break;
                default:
                    throw new Exception("Invalid Page Title");
            }
        } catch (Exception e) {
            Toast.makeText(this.app, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return title;
    }
}