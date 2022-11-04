package com.graphicless.modeltest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.graphicless.modeltest.R;
import com.graphicless.modeltest.fragment.QuestionCategoryFragment;
import com.graphicless.modeltest.model.MiddleDataModel;
import com.graphicless.modeltest.model.OuterDataModel;

import java.util.List;

public class QuestionCategoryAdapterOuter extends RecyclerView.Adapter<QuestionCategoryAdapterOuter.OuterViewHolder>{

    private List<OuterDataModel> outerDataModels;
    private List<MiddleDataModel> middleDataModels;
    QuestionCategoryFragment qcf;

    public QuestionCategoryAdapterOuter(List<OuterDataModel> outerDataModels, QuestionCategoryFragment qcf) {
        this.outerDataModels = outerDataModels;
        this.qcf = qcf;
    }
    public void updateData(List<OuterDataModel> outerDataModels) {
        this.outerDataModels = outerDataModels;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OuterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question_category_outer, parent, false);
        return new OuterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterViewHolder holder, int position) {

        OuterDataModel outerDataModel = outerDataModels.get(position);
        middleDataModels = outerDataModel.getMiddleDataModels();
        holder.data.setText(outerDataModel.getOuterData());

        boolean isExpandable = outerDataModel.isExpandable();
        holder.expandable.setVisibility(isExpandable ? View.GONE : View.VISIBLE);

        if (isExpandable)
            holder.plus.setImageResource(R.drawable.icon_plus);
        else
            holder.plus.setImageResource(R.drawable.icon_minus);

        QuestionCategoryAdapterMiddle questionCategoryAdapterMiddle = new QuestionCategoryAdapterMiddle(middleDataModels, outerDataModel.getOuterData(), qcf);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerView.setHasFixedSize(false);
        holder.recyclerView.setAdapter(questionCategoryAdapterMiddle);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.outside.setBackgroundResource(R.drawable.border);
                outerDataModel.setExpandable(!outerDataModel.isExpandable());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return outerDataModels.size();
    }


    public static class OuterViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout outside;
        ImageView plus;
        TextView data;
        RelativeLayout expandable;
        RecyclerView recyclerView;

        public OuterViewHolder(@NonNull View itemView) {
            super(itemView);

            outside = itemView.findViewById(R.id.layout_outside);
            plus = itemView.findViewById(R.id.plus);
            data = itemView.findViewById(R.id.data);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            expandable = itemView.findViewById(R.id.layout_expandable);

        }
    }
}
