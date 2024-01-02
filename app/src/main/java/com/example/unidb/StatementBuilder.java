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

    // to generate unique ıd for each student
    public long generateUID() {
        boolean isAlreadyRegistered;
        Cursor cursor;
        long randomNumber;

        do {
            long lowerBound = 1_000_000_000L;
            long upperBound = 9_999_999_999L;
            randomNumber = lowerBound + (long) (random.nextDouble() * (upperBound - lowerBound + 1));
            //query for randomId for studentID
            cursor = database.rawQuery("SELECT id FROM student WHERE id=" + randomNumber + ";", null);
            isAlreadyRegistered = cursor.moveToFirst();
        } while (isAlreadyRegistered);

        cursor.close();
        return randomNumber;
    }

}
