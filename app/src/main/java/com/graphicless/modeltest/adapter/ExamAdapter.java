package com.graphicless.modeltest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.model.QuestionModel;

import java.util.ArrayList;
import java.util.Objects;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {

    private static final String TAG = "ExamAdapter";
    public static float result = 0f;


    private ArrayList<QuestionModel> model;
    private final Context context;

    public ExamAdapter(Context context, ArrayList<QuestionModel> model) {
        this.model = model;
        this.context = context;
    }

    public void updateData(ArrayList<QuestionModel> model) {
        this.model = model;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_single_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Log.d(TAG, "mgs: " + model.get(position).getImageUrl());
        if(!Objects.equals(model.get(position).getImageUrl(), null)){
            holder.question_image.setPadding(20,20,20,20);
            Glide.with(context)
                    .asBitmap()
                    .load(model.get(position).getImageUrl())
                    .into(holder.question_image);
        }
        String question_number_temp = model.get(position).getQuestion_number() + ") ";
        holder.question_number.setText(question_number_temp);
        holder.question.setText(model.get(position).getQuestion());
        holder.option_one.setText(model.get(position).getOption_one());
        holder.option_two.setText(model.get(position).getOption_two());
        holder.option_three.setText(model.get(position).getOption_three());
        holder.option_four.setText(model.get(position).getOption_four());

        holder.option_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.option_one.toggle();
                holder.option_one.setClickable(false);
                holder.option_two.setClickable(false);
                holder.option_three.setClickable(false);
                holder.option_four.setClickable(false);

                holder.option_one.setTextColor(Color.rgb(244, 81, 30));
                holder.option_two.setTextColor(Color.GRAY);
                holder.option_three.setTextColor(Color.GRAY);
                holder.option_four.setTextColor(Color.GRAY);
                if(Objects.equals(model.get(holder.getAdapterPosition()).getCorrect_answer(), "option_one"))
                    result += 1f;
                else
                    result -= .25f;
            }
        });
        holder.option_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.option_two.toggle();
                holder.option_one.setClickable(false);
                holder.option_two.setClickable(false);
                holder.option_three.setClickable(false);
                holder.option_four.setClickable(false);

                holder.option_two.setTextColor(Color.rgb(244, 81, 30));
                holder.option_one.setTextColor(Color.GRAY);
                holder.option_three.setTextColor(Color.GRAY);
                holder.option_four.setTextColor(Color.GRAY);
                if(Objects.equals(model.get(holder.getAdapterPosition()).getCorrect_answer(), "option_two"))
                    result += 1f;
                else
                    result -= .25f;
            }
        });
        holder.option_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.option_three.toggle();
                holder.option_one.setClickable(false);
                holder.option_two.setClickable(false);
                holder.option_three.setClickable(false);
                holder.option_four.setClickable(false);

                holder.option_three.setTextColor(Color.rgb(244, 81, 30));
                holder.option_two.setTextColor(Color.GRAY);
                holder.option_one.setTextColor(Color.GRAY);
                holder.option_four.setTextColor(Color.GRAY);
                if(Objects.equals(model.get(holder.getAdapterPosition()).getCorrect_answer(), "option_three"))
                    result += 1f;
                else
                    result -= .25f;
            }
        });
        holder.option_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.option_four.toggle();
                holder.option_one.setClickable(false);
                holder.option_two.setClickable(false);
                holder.option_three.setClickable(false);
                holder.option_four.setClickable(false);

                holder.option_four.setTextColor(Color.rgb(244, 81, 30));
                holder.option_two.setTextColor(Color.GRAY);
                holder.option_three.setTextColor(Color.GRAY);
                holder.option_one.setTextColor(Color.GRAY);
                if(Objects.equals(model.get(holder.getAdapterPosition()).getCorrect_answer(), "option_four"))
                    result += 1f;
                else
                    result -= .25f;
            }
        });

    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
//        CircleImageView imageView;
        TextView question_number, question;
        ImageView question_image;
        RadioGroup options;
        RadioButton option_one, option_two, option_three, option_four;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question_image = itemView.findViewById(R.id.question_image);
            question_number = itemView.findViewById(R.id.question_number);
            question = itemView.findViewById(R.id.question);
            options = itemView.findViewById(R.id.options);
            option_one = itemView.findViewById(R.id.option_one);
            option_two = itemView.findViewById(R.id.option_two);
            option_three = itemView.findViewById(R.id.option_three);
            option_four = itemView.findViewById(R.id.option_four);
        }
    }
}
