package com.graphicless.modeltest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.graphicless.modeltest.HomeActivity;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.model.InnerDataModel;

import java.util.List;

public class StudyCategoryAdapterInner extends RecyclerView.Adapter<StudyCategoryAdapterInner.InnerDataViewHolder> {

   public static final String TAG = "inner";

   Context context;
   String outerData, middleData;
   private final List<InnerDataModel> innerDataModels;

   public StudyCategoryAdapterInner(Context context, List<InnerDataModel> innerDataModels, String outerData, String middleData) {
      this.context = context;
      this.innerDataModels = innerDataModels;
      this.outerData = outerData;
      this.middleData = middleData;
   }

   @NonNull
   @Override
   public StudyCategoryAdapterInner.InnerDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_study_category_inner, parent, false);
      return new StudyCategoryAdapterInner.InnerDataViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull StudyCategoryAdapterInner.InnerDataViewHolder holder, int position) {

      InnerDataModel innerDataModel = innerDataModels.get(position);
      holder.textView.setText(innerDataModel.getInnerData());

//      int color = Color.argb(255, 220, 220, 220);
//      holder.cardView.setCardBackgroundColor(color);

      holder.textView.setOnClickListener(view -> {
         if(context instanceof HomeActivity){
            String category = outerData.toLowerCase().replace(" ", "-");
            String subCategory = middleData.toLowerCase().replace(" ", "-");
            String unit = holder.textView.getText().toString().toLowerCase().replace(" ", "-");
            ((HomeActivity) context).editor.putString("category", category).commit();
            ((HomeActivity) context).editor.putString("subCategory", subCategory).commit();
            ((HomeActivity) context).editor.putString("unit", unit).commit();
            HomeActivity.studyCategoryUrl = category + "/" + subCategory + "/" + unit + "/";
            String message = "You've Selected:\n\n" + outerData + "\n" + middleData + "\n" + innerDataModel.getInnerData();
            ((HomeActivity)context).showConfirmationDialog(message);
         }
      });
   }

   @Override
   public int getItemCount() {
      return innerDataModels.size();
   }

   public static class InnerDataViewHolder extends RecyclerView.ViewHolder{

      private final TextView textView;
      private final CardView cardView;

      public InnerDataViewHolder(@NonNull View itemView) {
         super(itemView);

         textView = itemView.findViewById(R.id.tv_data);
         cardView = itemView.findViewById(R.id.cv_data);
      }
   }
}
