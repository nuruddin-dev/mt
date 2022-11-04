package com.graphicless.modeltest.adapter;

import android.content.Context;
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
import com.graphicless.modeltest.model.InnerDataModel;
import com.graphicless.modeltest.model.MiddleDataModel;

import java.util.List;

public class StudyCategoryAdapterMiddle extends RecyclerView.Adapter<StudyCategoryAdapterMiddle.MiddleDataViewHolder> {

    Context context;
    int color;
    String outerData;
    private final List<MiddleDataModel> middleDataModels;
    private List<InnerDataModel> innerDataModels;

    public StudyCategoryAdapterMiddle(Context context, int color, List<MiddleDataModel> middleDataModels, String outerData) {
        this.context = context;
        this.color = color;
        this.middleDataModels = middleDataModels;
        this.outerData = outerData;
    }


    @NonNull
    @Override
    public StudyCategoryAdapterMiddle.MiddleDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_study_category_outer, parent, false);
        return new StudyCategoryAdapterMiddle.MiddleDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudyCategoryAdapterMiddle.MiddleDataViewHolder holder, int position) {

        MiddleDataModel middleDataModel = middleDataModels.get(position);
        innerDataModels = middleDataModel.getInnerDataModels();
        holder.textView.setText(middleDataModel.getMiddleData());

        holder.cardView.setCardBackgroundColor(color);

        boolean isExpandable = middleDataModel.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.GONE : View.VISIBLE);
        holder.expandableLayout.setBackgroundColor(color);
        holder.recyclerView.setBackgroundColor(holder.recyclerView.getContext().getResources().getColor(R.color.action_bar));

        if (isExpandable)
            holder.arrow.setImageResource(R.drawable.arrow_down);
        else
            holder.arrow.setImageResource(R.drawable.arrow_up);

        StudyCategoryAdapterInner studyCategoryAdapterInner = new StudyCategoryAdapterInner(context, innerDataModels, outerData, middleDataModel.getMiddleData());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
//        holder.nestedRecyclerView.setHasFixedSize(false);
        holder.recyclerView.setAdapter(studyCategoryAdapterInner);

        holder.linearLayout.setOnClickListener(v -> {
            middleDataModel.setExpandable(!middleDataModel.isExpandable());
            notifyItemChanged(holder.getAdapterPosition());
//            if(context instanceof HomeActivity){
//                String subCategory = holder.textView.getText().toString().toLowerCase().replace(" ", "-");
//                ((HomeActivity) context).editor.putString("subCategory", subCategory).commit();
//            }
        });

    }

    @Override
    public int getItemCount() {
        return middleDataModels.size();
    }

    public static class MiddleDataViewHolder extends RecyclerView.ViewHolder{

        private final LinearLayout linearLayout;
        private final RelativeLayout expandableLayout;
        private final TextView textView;
        private final ImageView arrow;
        private final RecyclerView recyclerView;
        private final CardView cardView;

        public MiddleDataViewHolder(@NonNull View itemView) {
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
