package com.example.unidb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentAdministration extends Fragment {
    Button addFaculty, deleteFaculty, updateFaculty, searchFaculty;
    Button addDepartment, deleteDepartment, updateDepartment, searchDepartment;
    Button addLecturer, deleteLecturer, updateLecturer, searchLecturer;

    EditText enterFacultyName;
    EditText enterDepartmentName, enterDepartmentCode;
    EditText enterLecturerName, enterLecturerSurname;

    Spinner facultySpinner, departmentSpinner, lecturerSpinner;

    ArrayAdapter facultyAdapter, departmentAdapter, lectuererAdapter;
    ArrayList<HashMap> faculties, departments, lecturers;

    long lecId, depId, facId;

    private Database db = null;

    public FragmentAdministration(Database db) {
        this.db = db;
        try {
            faculties = db.readAll("faculty");
            departments = db.readAll("department");
            lecturers = db.readAll("lecturer");
        } catch (Exception e) {
            return;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.administration, container, false);
        initFields(view);

        return view;
    }

    public void initFields(View view) {
        this.addFaculty = view.findViewById(R.id.btnAddFaculty);
        this.deleteFaculty = view.findViewById(R.id.btnDeleteFaculty);
        this.updateFaculty = view.findViewById(R.id.btnUpdateFaculty);
        this.searchFaculty = view.findViewById(R.id.btnSearchFaculty);

        this.addDepartment = view.findViewById(R.id.btnAddDepartment);
        this.deleteDepartment = view.findViewById(R.id.btnDeleteDepartment);
        this.updateDepartment = view.findViewById(R.id.btnUpdateDepartment);
        this.searchDepartment = view.findViewById(R.id.btnSearchDepartment);

        this.addLecturer = view.findViewById(R.id.btnAddLecturer);
        this.deleteLecturer = view.findViewById(R.id.btnDeleteLecturer);
        this.updateLecturer = view.findViewById(R.id.btnUpdateLecturer);
        this.searchLecturer = view.findViewById(R.id.btnSearchLecturer);

        this.enterFacultyName = view.findViewById(R.id.editTextFacultyName);

        this.enterDepartmentName = view.findViewById(R.id.editTextDepartmentName);
        this.enterDepartmentCode = view.findViewById(R.id.editTextDepartmentCode);

        this.enterLecturerName = view.findViewById(R.id.editTextLecturerName);
        this.enterLecturerSurname = view.findViewById(R.id.editTextLecturerSurname);

        this.facultySpinner = view.findViewById(R.id.facultySpinner);
        this.departmentSpinner = view.findViewById(R.id.departmentSpinner);
        this.lecturerSpinner = view.findViewById(R.id.lecturerSpinner);
        try {
            this.facultyAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, faculties);
            this.departmentAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, departments);
            this.lectuererAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, lecturers);

            facultySpinner.setAdapter(facultyAdapter);
            departmentSpinner.setAdapter(departmentAdapter);
            lecturerSpinner.setAdapter(lectuererAdapter);
            facultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        facId = (long) faculties.get(position).get("id");
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    try {
                        facId = (long) faculties.get(parent.getSelectedItemPosition()).get("id");
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        depId = (long) departments.get(position).get("id");
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    try {
                        depId = (long) departments.get(parent.getSelectedItemPosition()).get("id");
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            lecturerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        lecId = (long) lecturers.get(position).get("id");
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    try {
                        lecId = (long) lecturers.get(parent.getSelectedItemPosition()).get("id");
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        initDeleteButtons(view);
        initCreateButtons(view);
        initUpdateButtons(view);
        initSearchButtons(view);
    }

    void initCreateButtons(View v) {
        this.addFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = enterFacultyName.getText().toString();
                    HashMap map = new HashMap<>();
                    map.put("name", name);
                    db.create(Constants.faculty, map);
                    updateRecords();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.addDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = enterDepartmentName.getText().toString();
                    String code = enterDepartmentCode.getText().toString();
                    HashMap map = new HashMap<>();
                    map.put("name", name);
                    map.put("code", code);
                    db.create(Constants.department, map);
                    updateRecords();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.addLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = enterLecturerName.getText().toString();
                    String surname = enterLecturerSurname.getText().toString();
                    HashMap map = new HashMap<>();
                    map.put("name", name);
                    map.put("surname", surname);
                    db.create(Constants.lecturer, map);
                    updateRecords();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void initDeleteButtons(View v) {
        try {
            deleteFaculty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        db.delete("faculty", facId);
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            deleteDepartment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        db.delete("department", depId);
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            deleteLecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        db.delete("lecturer", lecId);
                        updateRecords();
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    void updateRecords() {
        try {
            faculties = db.readAll("faculty");
            departments = db.readAll("department");
            lecturers = db.readAll("lecturer");

            lectuererAdapter.clear();
            lectuererAdapter.addAll(lecturers);
            lectuererAdapter.notifyDataSetChanged();

            departmentAdapter.clear();
            departmentAdapter.addAll(departments);
            departmentAdapter.notifyDataSetChanged();

            facultyAdapter.clear();
            facultyAdapter.addAll(faculties);
            facultyAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            return;
        }
    }

    void initUpdateButtons(View v) {
        this.updateFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = enterFacultyName.getText().toString();
                    HashMap map = new HashMap<>();
                    map.put("name", name);
                    db.udpate(Constants.faculty, map, facId);
                    updateRecords();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.updateDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = enterDepartmentName.getText().toString();
                    String code = enterDepartmentCode.getText().toString();
                    HashMap map = new HashMap<>();
                    map.put("name", name);
                    map.put("code", code);
                    db.udpate(Constants.department, map, depId);
                    updateRecords();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.updateLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = enterLecturerName.getText().toString();
                    String surname = enterLecturerSurname.getText().toString();
                    HashMap map = new HashMap<>();
                    map.put("name", name);
                    map.put("surname", surname);
                    db.udpate(Constants.lecturer, map, lecId);
                    updateRecords();
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void initSearchButtons(View v) {
        this.searchFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HashMap data = new HashMap<>();
                    data.put("name", enterFacultyName.getText().toString());
                    long exists = db.read(Constants.faculty, data);
                    if (exists > -1) {
                        int pos = -1;
                        for (int i = 0; i < faculties.size(); i++) {
                            if ((long) faculties.get(i).get("id") == exists) {
                                pos = i;
                                break;
                            }
                        }
                        if (pos == -1) {
                            throw new Exception("Something went wrong!");
                        }
                        facultySpinner.setSelection(pos);
                        updateRecords();
                    } else {
                        throw new Exception("Unknown search error!");
                    }

                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.searchDepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HashMap data = new HashMap<>();
                    data.put("name", enterDepartmentName.getText().toString());
                    data.put("code", enterDepartmentCode.getText().toString());
                    long exists = db.read(Constants.department, data);
                    if (exists > -1) {
                        int pos = -1;
                        for (int i = 0; i < departments.size(); i++) {
                            if ((long) departments.get(i).get("id") == exists) {
                                pos = i;
                                break;
                            }
                        }
                        if (pos == -1) {
                            throw new Exception("Something went wrong!");
                        }
                        departmentSpinner.setSelection(pos);
                        updateRecords();
                    } else {
                        throw new Exception("Unknown search error!");
                    }

                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        this.searchLecturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HashMap data = new HashMap<>();
                    data.put("name", enterLecturerName.getText().toString());
                    data.put("surname", enterLecturerSurname.getText().toString());
                    long exists = db.read(Constants.lecturer, data);
                    if (exists > -1) {
                        int pos = -1;
                        for (int i = 0; i < lecturers.size(); i++) {
                            if ((long) lecturers.get(i).get("id") == exists) {
                                pos = i;
                                break;
                            }
                        }
                        if (pos == -1) {
                            throw new Exception("Something went wrong!");
                        }
                        lecturerSpinner.setSelection(pos);
                        updateRecords();
                    } else {
                        throw new Exception("Unknown search error!");
                    }

                } catch (Exception e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
