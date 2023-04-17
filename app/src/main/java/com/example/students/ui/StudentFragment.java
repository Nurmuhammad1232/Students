package com.example.students.ui;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.students.DataBase.DbHandler;
import com.example.students.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentFragment extends Fragment {

    ImageButton openFileBtn;
    private final int PICK_IMAGE_REQUEST = 100;
    Spinner groupsSpinner;
    DbHandler db;
    EditText fname;
    EditText lname;
    EditText age;
    RadioGroup gender;
    View view;
    Button register;
    byte[] imgbase64;

    public StudentFragment() {
        // Required empty public constructor
    }

    public static StudentFragment newInstance() {
        StudentFragment fragment = new StudentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DbHandler(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_student, container, false);
        openFileBtn = view.findViewById(R.id.openFile);

        register = view.findViewById(R.id.register);

        groupsSpinner = view.findViewById(R.id.groups);
        fname = view.findViewById(R.id.fname);
        lname = view.findViewById(R.id.lname);
        age = view.findViewById(R.id.age);
        gender = view.findViewById(R.id.gender);
        ArrayList<String> groups = db.GetAllGroupsName();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, groups);
        groupsSpinner.setAdapter(adp);
        register.setOnClickListener(view1 -> {
            registerStudent();
        });

        openFileBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
        });
        return view;
    }

    private void registerStudent() {
        RadioButton genderRadio = view.findViewById(gender.getCheckedRadioButtonId());
        db.InsertStudent(fname.getText().toString(), lname.getText().toString(), genderRadio.getText().toString(), age.getText().toString(), groupsSpinner.getSelectedItem().toString(), imgbase64);
        Toast toast = Toast.makeText(getContext(),
                "Student Saved!", Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri filePath = data.getData();
            ContentResolver contentResolver = requireActivity().getContentResolver();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath);
                openFileBtn.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            imgbase64 = byteArrayOutputStream.toByteArray();
//            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
//            imgbase64 = encoded;
        }
    }

}