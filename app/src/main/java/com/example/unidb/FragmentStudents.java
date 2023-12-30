package com.example.unidb;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentStudents extends Fragment {
    private Database db;
    ListView list;
    StudentListAdapter listAdapter;
    ArrayList<HashMap> data = new ArrayList<>();
    Button refresh;

    public FragmentStudents(Database db) {
        this.db = db;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.students, container, false);
        this.list = v.findViewById(R.id.listViewRegisteredStudents);
        reload();
        this.listAdapter = new StudentListAdapter(v.getContext(), data);
        this.refresh = v.findViewById(R.id.refresh);
        this.list.setAdapter(listAdapter);
        this.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
                listAdapter.notifyDataSetChanged();
            }
        });
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View popUpView = inflater.inflate(R.layout.popup, null, false);
                TextView name, surname, stuId, gender, faculty, department, advisor;
                name = popUpView.findViewById(R.id.name);
                surname = popUpView.findViewById(R.id.surname);
                stuId = popUpView.findViewById(R.id.id);
                gender = popUpView.findViewById(R.id.gender);
                faculty = popUpView.findViewById(R.id.faculty);
                department = popUpView.findViewById(R.id.department);
                advisor = popUpView.findViewById(R.id.advisor);

                HashMap current = data.get(position);

                name.setText( (String) current.get("name"));
                surname.setText( (String) current.get("surname"));
                stuId.setText( (String) current.get("id"));
                gender.setText( (String) current.get("gender"));
                faculty.setText( (String) current.get("faculty"));
                department.setText( (String) current.get("department"));
                advisor.setText( (String) current.get("advisor"));

                PopupWindow pw = new PopupWindow(popUpView,1000,700, true);

                pw.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    void reload() {
        Cursor cursor = db.getDatabase().query("student",null,null,null,null, null, null);
        if (!this.data.isEmpty()) {
            this.data.clear();
        }
        while (cursor.moveToNext()) {
            HashMap map = new HashMap();
            map.put("id", Long.toString(cursor.getLong(cursor.getColumnIndexOrThrow("id"))));
            map.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));
            map.put("surname", cursor.getString(cursor.getColumnIndexOrThrow("surname")));
            map.put("gender", cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            map.put("faculty", cursor.getString(cursor.getColumnIndexOrThrow("faculty")));
            map.put("department", cursor.getString(cursor.getColumnIndexOrThrow("department")));
            map.put("advisor", cursor.getString(cursor.getColumnIndexOrThrow("advisor")));
            this.data.add(map);
        }
    }
}
