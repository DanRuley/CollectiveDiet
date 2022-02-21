package com.example.thecollectivediet.Me_Fragment_Components.Food_Editing;

import android.annotation.SuppressLint;
import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.R;

import java.util.ArrayList;

public class RecyclerEditFoodAdapter1 extends RecyclerView.Adapter<RecyclerEditFoodAdapter1.VerticalRVViewHolder> {

    Context context;
    ArrayList<VerticalModel> arrayList;

    int mExpandedPosition = -1;

    public RecyclerEditFoodAdapter1(Context context, ArrayList<VerticalModel> arrayList){
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public VerticalRVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_vertical_item, parent, false);

        return new VerticalRVViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalRVViewHolder holder, @SuppressLint("RecyclerView") int position) {
        VerticalModel verticalModel = arrayList.get(position);
        String title = verticalModel.getTitle();
        ArrayList<HorizontalModel> singleItem = verticalModel.getArrayList();

        holder.mTitle.setText(title);
        HorizontalRecyclerViewAdapter horizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter(context, singleItem);

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        holder.recyclerView.setAdapter(horizontalRecyclerViewAdapter);

//        holder.mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, verticalModel.getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        });

        final boolean isExpanded = position == mExpandedPosition;
        holder.recyclerView.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.recyclerView.setActivated(isExpanded);
        holder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(holder.recyclerView);
                notifyDataSetChanged();;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VerticalRVViewHolder extends RecyclerView.ViewHolder{

        RecyclerView recyclerView;
        TextView mTitle;
        AppCompatButton mButton;
        public VerticalRVViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_breakfast);
            mTitle = itemView.findViewById(R.id.tv_title);
//            mButton = itemView.findViewById(R.id.btn_more);

        }
    }
}
