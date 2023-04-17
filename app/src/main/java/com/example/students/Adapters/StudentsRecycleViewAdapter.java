package com.example.students.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.students.DataBase.DbHandler;
import com.example.students.R;
import com.example.students.models.GroupModel;
import com.example.students.models.StudentModel;
import com.example.students.ui.SingleStudentFragment;

import java.util.ArrayList;

public class StudentsRecycleViewAdapter extends RecyclerView.Adapter<StudentsRecycleViewAdapter.RecyclerViewHolder>{

    private ArrayList<StudentModel> array;
    private GroupModel group;
    private Context mcontext;
    DbHandler db;

    androidx.fragment.app.FragmentManager manager;
    public StudentsRecycleViewAdapter(ArrayList<StudentModel> recyclerDataArrayList,GroupModel group, Context mcontext, androidx.fragment.app.FragmentManager manager) {
        this.array = recyclerDataArrayList;
        this.group = group;
        this.mcontext = mcontext;
        this.manager = manager;
        db = new DbHandler(mcontext);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        StudentModel recyclerData = array.get(position);
        holder.grName.setText(recyclerData.fname+" "+recyclerData.lname);
        holder.grFaculty.setText(recyclerData.age+" years old");


        Bitmap decodedByte = BitmapFactory.decodeByteArray(recyclerData.img, 0, recyclerData.img.length);
        holder.personimg.setImageBitmap(decodedByte);
    }

    @Override
    public int getItemCount() {return array.size();}

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView grName;
        private TextView grFaculty;
        private ImageButton deleteBtn;
        private ImageView personimg;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            grName = itemView.findViewById(R.id.name);
            grFaculty = itemView.findViewById(R.id.faculty);
            deleteBtn = itemView.findViewById(R.id.deletebtn);
            personimg = itemView.findViewById(R.id.personimg);

            deleteBtn.setOnClickListener(view1 -> {
                int position = this.getAdapterPosition();
                db.DeleteStudent(array.get(position).id);
                deleteItem(position);
            });

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            StudentModel student = array.get(this.getAdapterPosition());
            DialogFragment newFragment = SingleStudentFragment.newInstance(student.id);
            newFragment.setShowsDialog(true);
            newFragment.show(manager,"Test");
        }
    }
    private void deleteItem(final int position) {
        array.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,getItemCount());
    }
}
