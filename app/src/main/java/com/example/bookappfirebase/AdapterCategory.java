package com.example.bookappfirebase;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory>{

    private Context context;
    private ArrayList<ModelCategory> categoryArrayList;


    public AdapterCategory(Context context, ArrayList<ModelCategory> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
    ModelCategory modelCategory = categoryArrayList.get(position);
    String id = modelCategory.getId();
    String category = modelCategory.getCategory();
    String uid = modelCategory.getUid();
    long timestamp = modelCategory.getTimestamp();
    holder.catTv.setText(category);
    holder.delBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(context, ""+category, Toast.LENGTH_SHORT).show();
        }
    });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class HolderCategory extends RecyclerView.ViewHolder{
        TextView catTv;
        ImageButton delBtn;
        public HolderCategory(@NonNull View itemView) {
            super(itemView);
            catTv = catTv.findViewById(R.id.categoryTv);
            delBtn = delBtn.findViewById(R.id.deleteBtn);
        }
    }
}
