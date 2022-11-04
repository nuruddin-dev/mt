package com.graphicless.modeltest.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.graphicless.modeltest.HomeActivity;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.fragment.ModelTestFragment;
import com.graphicless.modeltest.fragment.ModelTestListFragment;
import com.graphicless.modeltest.fragment.QuestionCategoryFragment;
import com.graphicless.modeltest.model.InnerDataModel;

import java.util.List;
import java.util.Locale;

public class QuestionCategoryAdapterInner extends RecyclerView.Adapter<QuestionCategoryAdapterInner.InnerViewHolder> {


    String outerData, middleData;
    private final List<InnerDataModel> innerDataModels;
    QuestionCategoryFragment qcf;

    public QuestionCategoryAdapterInner(List<InnerDataModel> innerDataModels, String outerData, String middleData, QuestionCategoryFragment qcf) {
        this.innerDataModels = innerDataModels;
        this.outerData = outerData;
        this.middleData = middleData;
        this.qcf = qcf;
    }

    @NonNull
    @Override
    public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question_category_inner, parent, false);
        return new InnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerViewHolder holder, int position) {
        InnerDataModel innerDataModel = innerDataModels.get(position);
        holder.data.setText(innerDataModel.getInnerData());

        holder.data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = outerData.toLowerCase().replace(" ", "-");
                String sub_subject = middleData.toLowerCase().replace(" ", "-");
                String chapter = holder.data.getText().toString().toLowerCase(Locale.ROOT).replace(" ", "-");
                HomeActivity.questionCategoryUrl = subject + "/" + sub_subject + "/" + chapter + "/";

                    if (holder.data.getContext() instanceof HomeActivity) {
                        ((HomeActivity) holder.data.getContext()).getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.pop_enter_from_left, R.anim.pop_exit_to_right)
                                .add(R.id.fragment_holder, new ModelTestListFragment(), "MTLF")
                                .addToBackStack("MTLF")
                                .commit();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return innerDataModels.size();
    }

    public static class InnerViewHolder extends RecyclerView.ViewHolder {

        private final ImageView plus;
        private final TextView data;

        public InnerViewHolder(@NonNull View itemView) {
            super(itemView);

            plus = itemView.findViewById(R.id.plus);
            data = itemView.findViewById(R.id.data);

        }
    }
}
