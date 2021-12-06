package com.example.thecollectivediet.Me_Fragment_Components.Food_Editing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.thecollectivediet.JSON_Marshall_Objects.EditFoodObject;
import com.example.thecollectivediet.R;

import java.util.List;

public class RecyclerEditFoodAdapter extends RecyclerView.Adapter<RecyclerEditFoodAdapter.EditViewHolder> {

    private List<EditFoodObject> list;
    private Context context;


    public RecyclerEditFoodAdapter(Context context, List<EditFoodObject> foodResults) {

        //todo probably need to deserialize list here instead of using foodResults **********************
        this.context = context;
        this.list = foodResults;
    }

    @NonNull
    @Override
    public RecyclerEditFoodAdapter.EditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.edit_food_recycler_element, parent, false);

        return new EditViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerEditFoodAdapter.EditViewHolder holder, int position) {

        holder.editFoodName.setText(list.get(position).getNameText());
        holder.editFoodServing.setText(list.get(position).getInfoText());
        holder.editDate.setText(list.get(position).getDateText());
    }

    @Override
    public int getItemCount() {
        //return foodResultList.size();
        return list.size();
    }

    public class EditViewHolder extends RecyclerView.ViewHolder {

        //ImageView editFoodPicture;
        TextView editFoodName;
        TextView editFoodServing;
        TextView editDate;

        public EditViewHolder(View view) {

            super(view);

            //editFoodPicture = itemView.findViewById(R.id.editRecImage);
            editFoodName = itemView.findViewById(R.id.editRecName);
            editFoodServing = itemView.findViewById(R.id.editRecServing);
            editDate = itemView.findViewById(R.id.editRecDate);
        }
    }
}
