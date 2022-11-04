package com.graphicless.modeltest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.graphicless.modeltest.R;
import com.graphicless.modeltest.model.MiddleDataModel;
import com.graphicless.modeltest.model.OuterDataModel;

import java.util.List;
import java.util.Random;

public class StudyCategoryAdapterOuter extends RecyclerView.Adapter<StudyCategoryAdapterOuter.OuterDataViewHolder> {

   Context context;
   int color;
   private List<OuterDataModel> outerDataModels;
   private List<MiddleDataModel> middleDataModels;

   public StudyCategoryAdapterOuter(Context context, List<OuterDataModel> outerDataModels) {
      this.context = context;
      this.outerDataModels = outerDataModels;
   }

   public void updateData(int color, List<OuterDataModel> outerDataModels) {
      this.color = color;
      this.outerDataModels = outerDataModels;
      notifyDataSetChanged();
   }


   @NonNull
   @Override
   public StudyCategoryAdapterOuter.OuterDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_study_category_outer, parent , false);
      return new  StudyCategoryAdapterOuter.OuterDataViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull StudyCategoryAdapterOuter.OuterDataViewHolder holder, int position) {

      OuterDataModel outerDataModel = outerDataModels.get(position);
      middleDataModels = outerDataModel.getMiddleDataModels();
      holder.textView.setText(outerDataModel.getOuterData());

      holder.cardView.setBackgroundColor(color);

      boolean isExpandable = outerDataModel.isExpandable();
      holder.expandableLayout.setVisibility(isExpandable ? View.GONE : View.VISIBLE);
      holder.recyclerView.setBackgroundColor(color);

      if (isExpandable)
         holder.arrow.setImageResource(R.drawable.arrow_down);
      else
         holder.arrow.setImageResource(R.drawable.arrow_up);

//      Random random = new Random();
//      int color = Color.argb(255, random.nextInt(250-230)+230, random.nextInt(240-220)+220, random.nextInt(190-170)+170);
      int color = holder.recyclerView.getContext().getResources().getColor(R.color.action_bar);
      StudyCategoryAdapterMiddle middleAdapter = new StudyCategoryAdapterMiddle(context, color, middleDataModels, outerDataModel.getOuterData());
      holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
//        holder.nestedRecyclerView.setHasFixedSize(false);
      holder.recyclerView.setAdapter(middleAdapter);

      holder.linearLayout.setOnClickListener(v -> {
         outerDataModel.setExpandable(!outerDataModel.isExpandable());
         notifyItemChanged(holder.getAdapterPosition());
//         if(context instanceof HomeActivity){
//            String category = holder.textView.getText().toString().toLowerCase().replace(" ", "-");
//            ((HomeActivity) context).editor.putString("category", category).commit();
//         }
      });
   }

   @Override
   public int getItemCount() {
      return outerDataModels.size();
   }

   public static class OuterDataViewHolder extends RecyclerView.ViewHolder{

      private final LinearLayout linearLayout;
      private final RelativeLayout expandableLayout;
      private final TextView textView;
      private final ImageView arrow;
      private final RecyclerView recyclerView;
      private final CardView cardView;


      public OuterDataViewHolder(@NonNull View itemView) {
         super(itemView);

         linearLayout = itemView.findViewById(R.id.layout_linear);
         expandableLayout = itemView.findViewById(R.id.layout_expandable);
         textView = itemView.findViewById(R.id.tv_data);
         arrow = itemView.findViewById(R.id.iv_arrow);
         recyclerView = itemView.findViewById(R.id.recycler_view);
         cardView = itemView.findViewById(R.id.cv_data);
      }

   }
}
