package com.example.unidb;

class Constants {
    //Database Names
    protected static final String dbName = "unidb";
    protected static final String faculty = "faculty";
    protected static final String department = "department";
    protected static final String lecturer = "lecturer";
    protected static final String student = "student";
    //Constant Queries for updating database that responsible for specific database (facultydb, departmentdb,lecturerdb,studentdb)
    protected static final String UPDATE_FACULTY = "UPDATE faculty SET name=? WHERE id=?";
    protected static final String UPDATE_DEPARTMENT = "UPDATE department SET name=?, code=? WHERE id=?";
    protected static final String UPDATE_LECTURER = "UPDATE lecturer SET name=?, surname=? WHERE id=?";
    protected static final String UPDATE_STUDENT = "UPDATE faculty SET name=?, surname=?, gender=?, faculty=?, department=?, advisor=? WHERE id=?";
}
