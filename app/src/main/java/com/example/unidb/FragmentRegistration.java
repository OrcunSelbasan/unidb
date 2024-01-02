package com.example.unidb;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentRegistration extends Fragment {
    /*
    Registration Page; Button, EditText, RadioGroup, RadioButton, Spinner, arrayAdapter, ArrayList, IdGenerator variables initialized.
    */
    Button addStudent, cancelStudent, updateStudent, searchStudent, refresh;
    EditText inputId, inputName, inputSurname;
    RadioGroup gender;
    RadioButton male, female;
    Spinner facSpinner, depSpinner, lecSpinner;
    ArrayAdapter facAdapter, depAdapter, lecAdapter;
    ArrayList<HashMap> faculties, departments, lecturers;
    IdGenerator generator;
    long facId, depId, lecId, stuId;
    Database db;
    /*
    . Reading the database that is in the Registration Page.
    */
    public FragmentRegistration(Database db) {
        this.db = db;
        try {
            faculties = db.readAll(Constants.faculty);
            departments = db.readAll(Constants.department);
            lecturers = db.readAll(Constants.lecturer);
            generator = new IdGenerator(db.getDatabase());
            stuId = generator.generateUID();
        } catch (Exception e) {
            return;
        }
    }
    /*
     . administration.xml has paired with FragmentAdministration.java.
     . Initializing the fields.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration, container, false);
        this.addStudent = view.findViewById(R.id.btnRegister);
        this.cancelStudent = view.findViewById(R.id.btnCancel);
        this.updateStudent = view.findViewById(R.id.btnUpdateRegistration);
        this.searchStudent = view.findViewById(R.id.btnSearchRegistration);
        this.refresh = view.findViewById(R.id.btnRefresh);
        this.inputId = view.findViewById(R.id.editTextStudentID);
        this.inputId.setFocusable(false);
        this.inputId.setEnabled(false);
        this.inputId.setCursorVisible(false);
        this.inputId.setKeyListener(null);
        this.inputId.setText(Long.toString(stuId));
        this.inputName = view.findViewById(R.id.editTextName);
        this.inputSurname = view.findViewById(R.id.editTextLastName);
        this.gender = view.findViewById(R.id.radioGroupGender);
        this.male = view.findViewById(R.id.radioButtonMale);
        this.female = view.findViewById(R.id.radioButtonFemale);
        this.facSpinner = view.findViewById(R.id.spinnerFaculty);
        this.depSpinner = view.findViewById(R.id.spinnerDepartment);
        this.lecSpinner = view.findViewById(R.id.spinnerAdvisor);

        this.facAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, faculties);
        this.lecAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, lecturers);
        this.depAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, departments);

        facSpinner.setAdapter(facAdapter);
        lecSpinner.setAdapter(lecAdapter);
        depSpinner.setAdapter(depAdapter);
        /*
         . Faculty Spinner which helps us to select the item in the list.
        */
        facSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    facId = (long) faculties.get(position).get("id");
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            /*
                . It is not necessary to use in here but it is required to have it in this file
                . due to AdapterView.OnItemSelectedListener.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {
                    facId = (long) faculties.get(parent.getSelectedItemPosition()).get("id");
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        depSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    depId = (long) departments.get(position).get("id");
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {
                    depId = (long) departments.get(parent.getSelectedItemPosition()).get("id");
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        lecSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    lecId = (long) lecturers.get(position).get("id");
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {
                    lecId = (long) lecturers.get(parent.getSelectedItemPosition()).get("id");
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        /*
         . Adding, registering the student
         */
        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("id", stuId);
                    cv.put("name", inputName.getText().toString());
                    cv.put("surname", inputSurname.getText().toString());
                    cv.put("gender", getGender(v));
                    cv.put("faculty", getValue("fac", facId));
                    cv.put("department", getValue("dep", depId));
                    cv.put("advisor", getValue("lec", lecId));
                    db.getDatabase().insert(Constants.student, null, cv);
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String[] args = new String[]{inputName.getText().toString(), inputSurname.getText().toString()};
                    db.getDatabase().delete(Constants.student, "name=? AND surname=?", args);
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String[] args = new String[]{inputName.getText().toString(), inputSurname.getText().toString(), getGender(v)};
                    Cursor cursor = db.getDatabase().query(Constants.student, null, "name=? AND surname=? AND gender=?", args, null, null, null);
                    if (cursor.moveToFirst()) {
                        int colName = cursor.getColumnIndexOrThrow("name");
                        int colSurname = cursor.getColumnIndexOrThrow("surname");
                        int colGender = cursor.getColumnIndexOrThrow("gender");
                        int colID = cursor.getColumnIndexOrThrow("id");
                        String message = Long.toString(cursor.getLong(colID)) + "-" + cursor.getString(colName) + " " + cursor.getString(colSurname) + "(" + cursor.getString(colGender) + ")";
                        Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        updateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ContentValues cv = new ContentValues();
                    cv.put("name", inputName.getText().toString());
                    cv.put("surname", inputSurname.getText().toString());
                    cv.put("gender", getGender(v));
                    cv.put("faculty", getValue("fac", facId));
                    cv.put("department", getValue("dep", depId));
                    cv.put("advisor", getValue("lec", lecId));
                    long id = -1;

                    String[] args = new String[]{inputName.getText().toString(), inputSurname.getText().toString()};
                    Cursor cursor = db.getDatabase().query(Constants.student, null, "name=? AND surname=?", args, null, null, null);
                    if (cursor.moveToFirst()) {
                        int colName = cursor.getColumnIndexOrThrow("name");
                        int colSurname = cursor.getColumnIndexOrThrow("surname");
                        int colGender = cursor.getColumnIndexOrThrow("gender");
                        int colID = cursor.getColumnIndexOrThrow("id");
                        id = cursor.getLong(colID);
                        String message = Long.toString(cursor.getLong(colID)) + "-" + cursor.getString(colName) + " " + cursor.getString(colSurname) + "(" + cursor.getString(colGender) + ")";
                        Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    db.getDatabase().update(Constants.student, cv,"id=?", new String[]{Long.toString(id)});
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecords();
            }
        });

        return view;
    }

    private ArrayList<HashMap> getArr(String type) throws Exception {
        ArrayList<HashMap> arr;
        switch (type) {
            case "fac":
                arr = faculties;
                break;
            case "dep":
                arr = departments;
                break;
            case "lec":
                arr = lecturers;
                break;
            default:
                throw new Exception(Constants.unknownType);
        }
        return arr;
    }

    private String getValue(String type, long id) {
        try {
            ArrayList<HashMap> arr = getArr(type);
            for (HashMap map : arr) {
                if ((long) map.get("id") == id) {
                    if (!type.equals("lec")) {
                        return (String) map.get("name");
                    } else {
                        return (String) map.get("name") + (String) map.get("surname");
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.getView().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return "";
    }

    public String getGender(View v) throws Exception {
        int id = gender.getCheckedRadioButtonId();
        if (id == male.getId()) {
            return male.getText().toString();
        } else if (id == female.getId()) {
            return female.getText().toString();
        } else {
            throw new Exception(Constants.unknownGender);
        }
    }

    void updateRecords() {
        try {
            faculties = db.readAll(Constants.faculty);
            departments = db.readAll(Constants.department);
            lecturers = db.readAll(Constants.lecturer);

            lecAdapter.clear();
            lecAdapter.addAll(lecturers);
            lecAdapter.notifyDataSetChanged();

            depAdapter.clear();
            depAdapter.addAll(departments);
            depAdapter.notifyDataSetChanged();

            facAdapter.clear();
            facAdapter.addAll(faculties);
            facAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            return;
        }
    }
}
