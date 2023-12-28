package com.example.unidb;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Database {
    Random random = new Random();
    private static SQLiteDatabase database = null;
    private Application app = null;
    private String path = null;

    public Database(Application app) {
        this.app = app;
        try {
            if (!this.isDbExists(this.path)) {
                database = SQLiteDatabase.openDatabase(getPath(app), null, SQLiteDatabase.CREATE_IF_NECESSARY);
                this.createTables();
            }
        } catch (Exception e) {
            Toast.makeText(app.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void read(String table, HashMap map) {
        // TODO START SEARCH FUNCTIONALITY
    }

    protected void create(String table, HashMap map) {
        try {
            StatementBuilder statementBuilder = new StatementBuilder(this.database);
            String statement = statementBuilder.getCreateStatement(table, map);
            // TODO CONT CREATE
        } catch (Exception e) {
            Toast.makeText(this.app.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void udpate(String table, HashMap map) {
        // TODO START UPDATE FUNCTIONALITY
    }

    protected void delete(String table, HashMap map) {
        // TODO START DELETE FUNCTIONALITY
    }

    // helper method to check if the db exists
    private boolean isDbExists(String path) {
        if (path == null) {
            return false;
        }
        File dbFile = new File(path);
        return dbFile.exists();
    }

    // Get db path
    private String getPath(Application app) {
        if (this.path != null) {
            return this.path;
        }
        File dbPath = app.getFilesDir();
        this.path = dbPath + "/" + Constants.dbName;
        return this.path;
    }

    // Create necessary tables (faculty, department, lecturer, student)
    private void createTables() {
        String facultyTableStatement = "CREATE TABLE IF NOT EXISTS " + Constants.faculty + " (id integer PRIMARY KEY autoincrement, name text);";
        String departmentTableStatement = "CREATE TABLE IF NOT EXISTS " + Constants.department + " (id integer PRIMARY KEY autoincrement, name text, code text);";
        String lecturerTableStatement = "CREATE TABLE IF NOT EXISTS " + Constants.lecturer + " (id integer PRIMARY KEY autoincrement, name text, surname text);";
        String studentTableStatement = "CREATE TABLE IF NOT EXISTS " + Constants.student + " (id integer PRIMARY KEY autoincrement, name text, surname text, gender text, faculty text, department text, advisor text);";

        database.execSQL(facultyTableStatement);
        database.execSQL(departmentTableStatement);
        database.execSQL(lecturerTableStatement);
        database.execSQL(studentTableStatement);

    }

}
