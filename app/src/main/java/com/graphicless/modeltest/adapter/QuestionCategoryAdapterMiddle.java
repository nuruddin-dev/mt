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
import com.graphicless.modeltest.model.InnerDataModel;
import com.graphicless.modeltest.model.MiddleDataModel;

import java.util.List;

public class QuestionCategoryAdapterMiddle extends RecyclerView.Adapter<QuestionCategoryAdapterMiddle.MiddleViewHolder> {


    String outerData;
    private final List<MiddleDataModel> middleDataModels;
    private List<InnerDataModel> innerDataModels;
    QuestionCategoryFragment qcf;

    public QuestionCategoryAdapterMiddle(List<MiddleDataModel> middleDataModels, String outerData, QuestionCategoryFragment qcf) {
        this.middleDataModels = middleDataModels;
        this.outerData = outerData;
        this.qcf = qcf;
    }

    @NonNull
    @Override
    public MiddleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question_category_middle, parent, false);
        return new MiddleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MiddleViewHolder holder, int position) {

        MiddleDataModel middleDataModel = middleDataModels.get(position);
        innerDataModels = middleDataModel.getInnerDataModels();
        holder.data.setText(middleDataModel.getMiddleData());

        boolean isExpandable = middleDataModel.isExpandable();
        holder.expandable.setVisibility(isExpandable ? View.GONE : View.VISIBLE);

        if (isExpandable)
            holder.plus.setImageResource(R.drawable.icon_plus);
        else
            holder.plus.setImageResource(R.drawable.icon_minus);

        QuestionCategoryAdapterInner questionCategoryAdapterInner = new QuestionCategoryAdapterInner(innerDataModels, outerData, middleDataModel.getMiddleData(), qcf);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.recyclerView.setHasFixedSize(false);
        holder.recyclerView.setAdapter(questionCategoryAdapterInner);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.outside.setBackgroundResource(R.drawable.border);
                middleDataModel.setExpandable(!middleDataModel.isExpandable());
                notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return middleDataModels.size();
    }

    public static class MiddleViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout outside;
        ImageView plus;
        TextView data;
        RelativeLayout expandable;
        RecyclerView recyclerView;

        public MiddleViewHolder(@NonNull View itemView) {
            super(itemView);

            outside = itemView.findViewById(R.id.layout_outside);
            plus = itemView.findViewById(R.id.plus);
            data = itemView.findViewById(R.id.data);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            expandable = itemView.findViewById(R.id.layout_expandable);

        }
    }
}
