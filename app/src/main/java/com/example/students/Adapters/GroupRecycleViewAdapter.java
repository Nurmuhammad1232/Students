package com.example.students.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.students.DataBase.DbHandler;
import com.example.students.GroupStudents;
import com.example.students.R;
import com.example.students.models.GroupModel;

import java.util.ArrayList;

public class GroupRecycleViewAdapter extends RecyclerView.Adapter<GroupRecycleViewAdapter.RecyclerViewHolder>{

    private ArrayList<GroupModel> array;
    private Context mcontext;
    DbHandler db;

    public GroupRecycleViewAdapter(ArrayList<GroupModel> recyclerDataArrayList, Context mcontext) {
        this.array = recyclerDataArrayList;
        this.mcontext = mcontext;
        db = new DbHandler(mcontext);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        GroupModel recyclerData = array.get(position);
        holder.grName.setText(recyclerData.name);
        holder.grFaculty.setText(recyclerData.faculty);
    }

    @Override
    public int getItemCount() {return array.size();}

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView grName;
        private TextView grFaculty;
        private ImageButton deleteBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            grName = itemView.findViewById(R.id.name);
            grFaculty = itemView.findViewById(R.id.faculty);
            deleteBtn = itemView.findViewById(R.id.deletebtn);
            deleteBtn.setOnClickListener(view1 -> {
                int position = this.getAdapterPosition();
                db.DeleteGroup(array.get(position).name);
                deleteItem(position);
            });
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            GroupModel group = array.get(this.getAdapterPosition());
            Intent i = new Intent(mcontext.getApplicationContext(), GroupStudents.class);
            i.putExtra("name", group.name);
            i.putExtra("faculty", group.faculty);
            i.putExtra("flow", group.flow);
            mcontext.startActivity(i);
        }
    }

    private void deleteItem(final int position) {
        array.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,getItemCount());
    }
}
