package com.example.unidb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class StatementBuilder {
    private Random random;
    private SQLiteDatabase database;

    StatementBuilder(SQLiteDatabase database) {
        this.database = database;
        this.random = new Random();
    }

    protected String getCreateStatement(String table, HashMap map) throws Exception {
        String name, surname, code, gender, faculty, department, advisor;
        name = (String) map.get("name");
        surname = (String) map.get("surname");
        code = (String) map.get("code");
        gender = (String) map.get("gender");
        faculty = (String) map.get("faculty");
        department = (String) map.get("department");
        advisor = (String) map.get("advisor");
        switch (table) {
            case Constants.faculty:
                return insertIntoFaculty(name);
            case Constants.department:
                return insertIntoDepartment(name, code);
            case Constants.lecturer:
                return insertIntoLecturer(name, surname);
            case Constants.student:
                return insertIntoStudent(name, surname, gender, faculty, department, advisor);
            default:
                throw new Exception("Invalid table");
        }
    }

    private String insertIntoFaculty(String name) {
        String[] attrs = new String[]{"name"};
        String[] values = new String[]{name};
        return createInsertStatement(Constants.faculty, attrs, values);
    }

    private String insertIntoDepartment(String name, String code) {
        String[] attrs = new String[]{"name", "code"};
        String[] values = new String[]{name, code};
        return createInsertStatement(Constants.department, attrs, values);
    }

    private String insertIntoLecturer(String name, String surname) {
        String[] attrs = new String[]{"name", "surname"};
        String[] values = new String[]{name, surname};
        return createInsertStatement(Constants.lecturer, attrs, values);
    }

    private String insertIntoStudent(String name, String surname, String gender, String faculty, String department, String advisor) {
        String[] attrs = new String[]{"name", "surname", "gender", "faculty", "department", "advisor"};
        String[] values = new String[]{name, surname, gender, faculty, department, advisor};
        return createInsertStatement(Constants.student, attrs, values);
    }

    private String[] buildValueArray(String[] values) {
        ArrayList<String> parsedValues = new ArrayList<>();
        for (String value : values) {
            String quotedString = "'" + value + "'";
            parsedValues.add(quotedString);
        }

        int arraySize = parsedValues.size();
        String[] result = new String[arraySize];

        for (int i = 0; i < arraySize; i++) {
            result[i] = parsedValues.get(i);
        }

        return result;
    }

    private long generateUID() {
        boolean isAlreadyRegistered;
        Cursor cursor;
        long randomNumber;

        do {
            long lowerBound = 1_000_000_000L;
            long upperBound = 9_999_999_999L;
            randomNumber = lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound + 1));

            cursor = database.rawQuery("SELECT id FROM student WHERE id=" + randomNumber + ";", null);
            isAlreadyRegistered = cursor.moveToFirst();
        } while (isAlreadyRegistered);

        cursor.close();
        return randomNumber;
    }

    private String createInsertStatement(String table, String[] attributes, String[] values) {
        String[] parsedValues = this.buildValueArray(values);
        String concatenatedValues = String.join(", ", parsedValues);
        String concatenatedAttributes = String.join(", ", attributes);
        String statement = "INSERT INTO ";
        statement += table + " ";
        statement += "(";

        if (table.equals(Constants.student)) {
            statement += "id, ";
        }

        statement += concatenatedAttributes + ")";
        statement += " VALUES ";
        statement += "(";

        if (table.equals(Constants.student)) {
            statement += this.generateUID() + ", ";
        }

        statement += concatenatedValues;
        statement += ");";

        return statement;
    }
}
