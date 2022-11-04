package com.graphicless.modeltest.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.graphicless.modeltest.HomeActivity;
import com.graphicless.modeltest.R;
import com.graphicless.modeltest.fragment.QuestionCategoryFragment;
import com.graphicless.modeltest.model.SingleItemModel;

import java.util.List;
import java.util.Objects;

public class GridviewAdapter extends BaseAdapter {

    private final List<SingleItemModel> items;
    Context context;
    public String style;
    Activity activity;

    public GridviewAdapter(List<SingleItemModel> items, Context context, String style, Activity activity) {
        this.items = items;
        this.context = context;
        this.style = style;
        notifyDataSetChanged();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewHorizontal = null, viewVertical = null;


        if(view == null){
            viewHorizontal = layoutInflater.inflate(R.layout.layout_single_item, viewGroup, false);
            viewVertical = layoutInflater.inflate(R.layout.layout_single_item_verticle, viewGroup, false);
        }else{
            if (Objects.equals(style, "horizontal"))
                viewHorizontal = view;
            else
                viewVertical = view;
        }


        if (Objects.equals(style, "horizontal")) {
            assert viewHorizontal != null;
            holder = new ViewHolder(viewHorizontal);
        }
        else {
            assert viewVertical != null;
            holder = new ViewHolder(viewVertical);
        }

//        Glide.with(context)
//                .asBitmap()
//                .load(items.get(i).getItemImageLink())
//                .into(holder.itemImage);

        holder.itemName.setText(items.get(i).getItemName());

        holder.itemImage.setImageResource(items.get(i).getItemImage());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.itemName.getText() == "By Topic"){
                    String from = "ModelTestFragment";
                    Bundle bundle = new Bundle();
                    bundle.putString("from", from);
                    QuestionCategoryFragment qcf = new QuestionCategoryFragment();
                    qcf.setArguments(bundle);

                    if(activity instanceof HomeActivity){
                        ((HomeActivity) activity).getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.pop_enter_from_left, R.anim.pop_exit_to_right)
                                .add(R.id.fragment_holder, qcf, "QCF_MTF")
                                .addToBackStack("QCF_MTF")
                                .commit();
                    }
                }
            }
        });

        if (Objects.equals(style, "horizontal"))
            return viewHorizontal;
        else
            return viewVertical;
    }

    public static class ViewHolder {

        RelativeLayout item;
        ImageView itemImage;
        TextView itemName;

        public ViewHolder(View view){
            item = view.findViewById(R.id.item);
            itemImage = view.findViewById(R.id.iv_item);
            itemName = view.findViewById(R.id.tv_item);
        }
    }

}
