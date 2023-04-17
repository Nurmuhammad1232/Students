package com.example.students.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.students.Adapters.GroupRecycleViewAdapter;
import com.example.students.DataBase.DbHandler;
import com.example.students.R;
import com.example.students.models.GroupModel;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {
    EditText  name;
    EditText  faculty;
    EditText  flow;
    Button register;
    DbHandler db;

    public GroupFragment() {
        // Required empty public constructor
    }

    public static GroupFragment newInstance() {
        GroupFragment groupFragment = new GroupFragment();
        return groupFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DbHandler(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        name = view.findViewById(R.id.name);
        faculty = view.findViewById(R.id.faculty);
        flow = view.findViewById(R.id.flow);
        register = view.findViewById(R.id.register);
        register.setOnClickListener(view1 -> {
        registerGroup();
        });
        return view;
    }

    private void registerGroup() {
    db.InsertGroup(name.getText().toString(),faculty.getText().toString(),flow.getText().toString());
        Toast toast = Toast.makeText(getContext(),
                "Group Saved!", Toast.LENGTH_SHORT);
        toast.show();
    }

}