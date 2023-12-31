package com.example.unidb;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final Application app;
    private final Database database;
    private FragmentActivity fm = new FragmentActivity();

    public ViewPagerAdapter(FragmentManager fragmentManager, Application app, Database database) {
        super(fragmentManager);
        this.app = app;
        this.database = database;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Set default fragment
        Fragment fragment = new FragmentAdministration(this.database);
        try {
            switch (position) {
                case 0:
                    fragment = new FragmentAdministration(this.database);
                    break;
                case 1:
                    fragment = new FragmentRegistration(this.database);
                    break;
                case 2:
                    fragment = new FragmentStudents(this.database);
                    break;
                default:
                    // Set default fragment again just in case
                    fragment = new FragmentAdministration(this.database);
                    throw new Exception(Constants.invalidFragment);
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
                    title = Constants.administration;
                    break;
                case 1:
                    title = Constants.registration;
                    break;
                case 2:
                    title = Constants.students;
                    break;
                default:
                    throw new Exception(Constants.invalidPageTitle);
            }
        } catch (Exception e) {
            Toast.makeText(this.app, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return title;
    }
}