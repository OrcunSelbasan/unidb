package com.example.unidb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Random;

class IdGenerator {
    private Random random;
    private SQLiteDatabase database;

    IdGenerator(SQLiteDatabase database) {
        this.database = database;
        this.random = new Random();
    }

    // to generate unique ıd for each student
    public long generateUID() {
        boolean isAlreadyRegistered;
        Cursor cursor;
        long randomNumber;

        do {
            randomNumber = Constants.lowerBound + (long) (random.nextDouble() * (Constants.upperBound - Constants.lowerBound + 1));
            //query for randomId for studentID
            cursor = database.rawQuery("SELECT id FROM student WHERE id=" + randomNumber + ";", null);
            isAlreadyRegistered = cursor.moveToFirst();
        } while (isAlreadyRegistered);

        cursor.close();
        return randomNumber;
    }

}
