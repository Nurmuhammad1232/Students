package com.example.students;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.students.Adapters.GroupRecycleViewAdapter;
import com.example.students.Adapters.StudentsRecycleViewAdapter;
import com.example.students.DataBase.DbHandler;
import com.example.students.models.GroupModel;
import com.example.students.models.StudentModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GroupStudents extends AppCompatActivity {
    DbHandler db;
    TextView groupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_students);
        Intent intent = getIntent();
        GroupModel group = new GroupModel();
        group.name = intent.getStringExtra("name");
        group.faculty = intent.getStringExtra("faculty");
        group.flow = intent.getStringExtra("flow");
        groupname = findViewById(R.id.groupName);
        groupname.setText(group.name);
        db = new DbHandler(getApplicationContext());

        ArrayList<StudentModel> students = db.GetStudentsofGroup(group.name);
        RecyclerView recyclerView = findViewById(R.id.result_recycle_view);
        StudentsRecycleViewAdapter adapter = new StudentsRecycleViewAdapter(
                students,
                group,
                getApplicationContext(),
                getSupportFragmentManager()
        );
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }
}