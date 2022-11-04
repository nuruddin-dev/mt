package com.graphicless.modeltest.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graphicless.modeltest.ExamActivity;
import com.graphicless.modeltest.HomeActivity;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.fragment.ModelTestListFragmentAll;
import com.graphicless.modeltest.model.ModelTestListModel;

import java.util.List;

public class ModelTestListAdapter extends RecyclerView.Adapter<ModelTestListAdapter.ModelTestListViewHolder> {

    List<ModelTestListModel> modelTestListModels;
    int color;

    public ModelTestListAdapter(List<ModelTestListModel> modelTestListModels) {
        this.modelTestListModels = modelTestListModels;
    }

    public void update(List<ModelTestListModel> modelTestListModels, int color){
        this.modelTestListModels = modelTestListModels;
        this.color = color;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ModelTestListAdapter.ModelTestListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_list_item, parent, false);
        return new ModelTestListAdapter.ModelTestListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelTestListAdapter.ModelTestListViewHolder holder, int position) {

        Log.d("mfla", "onBindViewHolder: ");

        holder.title.setText(modelTestListModels.get(position).getName());
        holder.dataHolder.setBackgroundColor(color);
        String api = modelTestListModels.get(position).getAddress();

        holder.dataHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.dataHolder.getContext() instanceof HomeActivity){
                    Intent intent = new Intent(((HomeActivity) holder.dataHolder.getContext()), ExamActivity.class);
                    intent.putExtra("api", api);
                    holder.dataHolder.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelTestListModels.size();
    }

    public static class ModelTestListViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout dataHolder;
        TextView title, tv1, tv2, tv3, attempted, result;


        public ModelTestListViewHolder(@NonNull View itemView) {
            super(itemView);

            dataHolder = itemView.findViewById(R.id.data_holder);
            title = itemView.findViewById(R.id.title);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            attempted = itemView.findViewById(R.id.attempted);
            result = itemView.findViewById(R.id.result);
        }
    }
}
