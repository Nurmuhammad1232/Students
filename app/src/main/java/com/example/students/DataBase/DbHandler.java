package com.example.students.DataBase;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.text.Editable;

import com.example.students.models.GroupModel;
import com.example.students.models.StudentModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class DbHandler extends SQLiteOpenHelper {
    public DbHandler(Context context) {
        super(context, "userstore.db", null, 1);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS \"group\" (\n" +
                "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\t    TEXT,\n" +
                "\t\"faculty\"\tTEXT,\n" +
                "\t\"flow\"\t    TEXT\n" +
                ")"));

        sqLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS  \"student\" (\n" +
                "\t\"id\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"fname\"\t    TEXT,\n" +
                "\t\"lname\"\t    TEXT,\n" +
                "\t\"gender\"\t    TEXT,\n" +
                "\t\"age\"\t    TEXT,\n" +
                "\t\"img\"\t    BLOB,\n" +
                "\t\"group_name\"\t    TEXT\n" +
                ")"));
    }

    @SuppressLint("Recycle")
    public ArrayList<GroupModel> GetAllGroups() {
        SQLiteDatabase DbHandler = this.getReadableDatabase();
        Cursor cursor = DbHandler.rawQuery("SELECT * FROM \"group\";", null);
        ArrayList<GroupModel> groups = new ArrayList<>();
        while (cursor.moveToNext()) {
            GroupModel group = new GroupModel();
            group.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            group.faculty = cursor.getString(cursor.getColumnIndexOrThrow("faculty"));
            group.flow = cursor.getString(cursor.getColumnIndexOrThrow("flow"));
            group.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            groups.add(group);
        }
        DbHandler.close();
        return groups;
    }

    @SuppressLint("Recycle")
    public GroupModel GetGroup(String groupname) {
        SQLiteDatabase DbHandler = this.getReadableDatabase();
        Cursor cursor = DbHandler.rawQuery("SELECT * FROM \"group\" where name=?;", new String[]{groupname});
        GroupModel group = new GroupModel();
        while (cursor.moveToNext()) {
            group.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            group.faculty = cursor.getString(cursor.getColumnIndexOrThrow("faculty"));
            group.flow = cursor.getString(cursor.getColumnIndexOrThrow("flow"));
            group.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        DbHandler.close();
        return group;
    }

    @SuppressLint("Recycle")
    public ArrayList<String> GetAllGroupsName() {
        SQLiteDatabase DbHandler = this.getReadableDatabase();
        Cursor cursor = DbHandler.rawQuery("SELECT name FROM \"group\";", null);
        ArrayList<String> groups = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            groups.add(name);
        }
        DbHandler.close();
        return groups;
    }

    @SuppressLint("Recycle")
    public ArrayList<StudentModel> GetStudentsofGroup(String groupname) {
        SQLiteDatabase DbHandler = this.getReadableDatabase();
        Cursor cursor = DbHandler.rawQuery("SELECT * FROM \"student\" where group_name=?;", new String[]{groupname});
        ArrayList<StudentModel> students = new ArrayList<>();
        while (cursor.moveToNext()) {
            StudentModel student = new StudentModel();
            student.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            student.fname = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
            student.lname = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
            student.age = cursor.getString(cursor.getColumnIndexOrThrow("age"));
            student.img = cursor.getBlob(cursor.getColumnIndexOrThrow("img"));
            student.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            students.add(student);
        }
        DbHandler.close();
        return students;
    }

    @SuppressLint("Recycle")
    public StudentModel GetStudent(String id) {
        SQLiteDatabase DbHandler = this.getReadableDatabase();
        Cursor cursor = DbHandler.rawQuery("SELECT * FROM \"student\" where id=?;", new String[]{id});
        StudentModel student = new StudentModel();
        while (cursor.moveToNext()) {
            student.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            student.fname = cursor.getString(cursor.getColumnIndexOrThrow("fname"));
            student.lname = cursor.getString(cursor.getColumnIndexOrThrow("lname"));
            student.age = cursor.getString(cursor.getColumnIndexOrThrow("age"));
            student.img = cursor.getBlob(cursor.getColumnIndexOrThrow("img"));
            student.gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            student.groupname = cursor.getString(cursor.getColumnIndexOrThrow("group_name"));
        }
        DbHandler.close();
        return student;
    }

    public void InsertStudent(String fname, String lname, String gender, String age, String group_name, byte[] img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fname", fname);
        values.put("lname", lname);
        values.put("gender", gender);
        values.put("age", age);
        values.put("group_name", group_name);
        values.put("img", img);
        db.insert("student", null, values);
        db.close();
    }

    public void UpdateStudent(StudentModel student) {
        System.out.println(student.gender);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fname", student.fname);
        values.put("lname", student.lname);
        values.put("gender", student.gender);
        values.put("age", student.age);
        values.put("img", student.img);
        values.put("group_name", student.groupname);
        db.update("student", values, "id=?", new String[]{String.valueOf(student.id)});
        db.close();
    }

    public void InsertGroup(String name, String faculty, String flow) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("faculty", faculty);
        values.put("flow", flow);
        db.insert("\"group\"", null, values);
        db.close();
    }

    public void DeleteStudentsbyGroup(String groupname) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("\"student\"", "group_name=?", new String[]{groupname});
        db.close();
    }

    public void DeleteGroup(String groupname) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("\"group\"", "name=?", new String[]{groupname});
        DeleteStudentsbyGroup(groupname);
        db.close();
    }

    public void DeleteStudent(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("\"student\"", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
