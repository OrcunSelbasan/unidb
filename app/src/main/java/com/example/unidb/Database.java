package com.example.unidb;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Database {
    private static SQLiteDatabase database = null;
    private final Application app;
    private String path = null;
    //Create and connect database via app
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
    //if database is null returns null and shows toast Message if it is not returns database
    public SQLiteDatabase getDatabase() {
        try {
            if (database != null) {
                return database;
            } else {
                throw new Exception("Database error!");
            }
        } catch (Exception e) {
            Toast.makeText(app.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    //to read all data from database
    protected ArrayList<HashMap> readAll(String table) {
        ArrayList<HashMap> results = new ArrayList<>();
        try {
            String statement = "SELECT * FROM " + table + ";";
            Cursor cursor = database.rawQuery(statement, null);

            while (cursor.moveToNext()) {
                HashMap record = new HashMap<>();
                //index will be given which is respected to column index and  put it into hashMap in the end shows index of id

                record.put("id", cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                switch (table) {
                    case Constants.faculty:
                        //index will be given which is respected to column index and  put it into hashMap in the end shows index of name
                        record.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));
                        break;
                    case Constants.department:
                        //index will be given which is respected to column index and  put it into hashMap in the end shows index of name
                        record.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));
                        //index will be given which is respected to column index and  put it into hashMap in the end shows index of code
                        record.put("code", cursor.getString(cursor.getColumnIndexOrThrow("code")));
                        break;
                    case Constants.lecturer:
                        //index will be given which is respected to column index and  put it into hashMap in the end shows index of name
                        record.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));
                        //index will be given which is respected to column index and  put it into hashMap in the end shows index of surname
                        record.put("surname", cursor.getString(cursor.getColumnIndexOrThrow("surname")));
                        break;
                    default:
                        throw new Exception("Unknown read error!");
                }
                //add all record inside results
                results.add(record);
            }

            return results;
        } catch (Exception e) {
            Toast.makeText(this.app.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return results;
    }

    protected long read(String table, HashMap data) {
        //hold WHERE clause which is respect to selected database
        String wClause = "";
        //array of values which is respect to selected database
        String[] values;
        try {
            // select specific database for each case
            switch (table) {
                case Constants.faculty:
                    wClause = "name=?";
                    values = new String[]{(String) data.get("name")};
                    break;
                case Constants.department:
                    wClause = "name=? AND code=?";
                    values = new String[]{(String) data.get("name"), (String) data.get("code")};
                    break;
                case Constants.lecturer:
                    wClause = "name=? AND surname=?";
                    values = new String[]{(String) data.get("name"), (String) data.get("surname")};
                    break;
                default:
                    throw new Exception("An error occured");
            }
            try {
                Cursor cursor = database.query(
                        table,   // The table to query
                        null,            // The array of columns to return (pass null to get all)
                        wClause,              // The columns for the WHERE clause
                        values,          // The values for the WHERE clause
                        null,                   // don't group the rows
                        null,                   // don't filter by row groups
                        null               // The sort order
                );
                if (cursor.moveToFirst()) {
                    //if there is a cursor data then return data id
                    return cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                }
            } catch (Exception e) {
                throw new Exception("Read error!" + e.getMessage());
            }
            throw new Exception("Invalid read");
        } catch (Exception e) {
            Toast.makeText(this.app.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return -1;
    }

    protected void create(String table, HashMap data) {
        try {
            String name, surname, code, gender, department, faculty, advisor, statement;
            ContentValues cv = new ContentValues();
            Cursor cursor;
            //create uid for student
            if (table.equals(Constants.student)) {
                StatementBuilder statementBuilder = new StatementBuilder(database);
                cv.put("id", statementBuilder.generateUID());
            }

            switch (table) {
                // select specific database for each case
                case Constants.faculty:
                    name = (String) data.get("name");
                    if (name.isEmpty()) {
                        throw new Exception("Empty faculty name");
                    }
                    //put value name into map of key "name"
                    cv.put("name", name);
                    //query for checking if name is exist
                    statement = "SELECT * FROM faculty WHERE name='"+name+"';";
                    cursor = database.rawQuery(statement, null);
                    //if exist then throw exception
                    if (cursor.moveToFirst()) {
                        throw new Exception("Faculty exists");
                    }
                    break;
                case Constants.department:
                    name = (String) data.get("name");
                    code = (String) data.get("code");

                    cv.put("name", name);
                    cv.put("code", code);
                    //checking if name or code is empty
                    if (name.isEmpty()) {
                        throw new Exception("Empty department name");
                    }

                    if (code.isEmpty()) {
                        throw new Exception("Empty department code");
                    }
                    //query for checking if name or code is exist
                    statement = "SELECT * FROM department WHERE name='"+name+"' OR code='" +code +"';";
                    cursor = database.rawQuery(statement, null);
                    //if exist then throw exception
                    if (cursor.moveToFirst()) {
                        throw new Exception("Department Exists");
                    }
                    break;
                case Constants.lecturer:
                    name = (String) data.get("name");
                    surname = (String) data.get("surname");

                    cv.put("name", name);
                    cv.put("surname", surname);
                    if (name.length() < 1) {
                        throw new Exception("Empty lecturer name");
                    }

                    if (surname.length() < 1) {
                        throw new Exception("Empty lecturer surname");
                    }
                    //query for checking if name and surname is exist
                    statement = "SELECT * FROM lecturer WHERE name='"+name+"' AND surname='" +surname +"';";
                    cursor = database.rawQuery(statement, null);
                    if (cursor.moveToFirst()) {
                        throw new Exception("Lecturer Exists");
                    }
                    break;
                default:
            }
            //insert all data that have been putted in cv
            long result = database.insert(table, null, cv);
            if (result < 0) {
                throw new Exception("Invalid Create");
            }
        } catch (Exception e) {
            Toast.makeText(this.app.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void udpate(String table, HashMap data, long ID) {
        String name, surname, code;
        ContentValues cv = new ContentValues();
        try {
            // select specific database for each case
            switch (table) {
                case Constants.faculty:
                    name = (String) data.get("name");
                    if (name.isEmpty()) {
                        throw new Exception("Empty faculty name");
                    }
                    //put name value that have been gotten from data
                    cv.put("name", name);
                    break;
                case Constants.department:
                    name = (String) data.get("name");
                    code = (String) data.get("code");
                    //put name value that have been gotten from data
                    //put vode value that have been gotten from data

                    cv.put("name", name);
                    cv.put("code", code);

                    if (name.isEmpty()) {
                        throw new Exception("Empty department name");
                    }

                    if (code.isEmpty()) {
                        throw new Exception("Empty department code");
                    }
                    break;
                case Constants.lecturer:
                    name = (String) data.get("name");
                    surname = (String) data.get("surname");
                    //put name value that have been gotten from data
                    //put surname value that have been gotten from data

                    cv.put("name", name);
                    cv.put("surname", surname);
                    if (name.length() < 1) {
                        throw new Exception("Empty lecturer name");
                    }

                    if (surname.length() < 1) {
                        throw new Exception("Empty lecturer surname");
                    }
                    break;
                default:
            }
            String id = Long.toString(ID);
            //update database which is respect to id
            int result = database.update(table, cv, "id=?", new String[]{id});
            if (result < 0) {
                throw new Exception("Invalid update");
            }
        } catch (Exception e) {
            Toast.makeText(app.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //deletes table which is respect to id
    protected void delete(String table, long id) {
        try {
            int result = database.delete(table, "id=?", new String[]{Long.toString(id)});
            if (result < 0) {
                throw new Exception("Invalid update");
            }
        } catch (Exception e) {
            Toast.makeText(app.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
