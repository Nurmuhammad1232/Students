package com.example.students.ui;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.students.DataBase.DbHandler;
import com.example.students.R;
import com.example.students.models.GroupModel;
import com.example.students.models.StudentModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleStudentFragment extends DialogFragment {

    private StudentModel student;
    private GroupModel group;
    DbHandler db;
    Button update;
    ImageButton personimg;
    ImageButton close;
    EditText fname;
    EditText lname;
    EditText age;
    RadioGroup gender;
    TextView group_name;
    TextView faculty;
    TextView flow;
    private final int PICK_IMAGE_REQUEST = 100;

    public SingleStudentFragment() {
        // Required empty public constructor
    }

    public static SingleStudentFragment newInstance(Integer id) {
        SingleStudentFragment fragment = new SingleStudentFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme);
        assert getArguments() != null;
        int id = getArguments().getInt("id");
        db = new DbHandler(getContext());
        student = db.GetStudent(String.valueOf(id));
        group = db.GetGroup(student.groupname);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_single_student,container, false);
        personimg = view.findViewById(R.id.personimg);
        fname = view.findViewById(R.id.fname);
        lname = view.findViewById(R.id.lname);
        age = view.findViewById(R.id.age);
        gender = view.findViewById(R.id.gender);
        group_name = view.findViewById(R.id.group_name);
        faculty = view.findViewById(R.id.faculty);
        flow = view.findViewById(R.id.flow);
        close = view.findViewById(R.id.closebtn);
        update = view.findViewById(R.id.updatebtn);

        close.setOnClickListener(view1 -> {
            dismiss();
        });
        update.setOnClickListener(view1 -> {
            updateStudent(view);
            dismiss();
        });
        personimg.setOnClickListener(view1 -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
        });

        if (Objects.equals(student.gender, "Male")){
            gender.check(R.id.radioMale);
        }else{
            gender.check(R.id.radioFemale);
        }
        fname.setText(student.fname);
        lname.setText(student.lname);
        age.setText(student.age);

        Bitmap decodedByte = BitmapFactory.decodeByteArray(student.img, 0, student.img.length);
        personimg.setImageBitmap(decodedByte);

        group_name.setText(group.name);
        faculty.setText(group.faculty);
        flow.setText(group.flow);

        return view;
    }

    private void updateStudent(View view) {
    student.fname = fname.getText().toString();
    student.lname =  lname.getText().toString();
    RadioButton genderRadio = view.findViewById(gender.getCheckedRadioButtonId());
    student.gender =  genderRadio.getText().toString();
    student.age = age.getText().toString();
    db.UpdateStudent(student);
    Toast toast = Toast.makeText(getContext(),
            "Student Updated!", Toast.LENGTH_SHORT);
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
                personimg.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            student.img = byteArrayOutputStream.toByteArray();
        }
    }
}