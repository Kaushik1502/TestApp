package com.testapp.com.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testapp.R;

/**
 * Created by cws on 19/10/16.
 */
public class HorizontalListAdapter extends RecyclerView.Adapter<HorizontalListAdapter.DataObjectHolder> {
    Activity activity;
    int[] names, images;

    public HorizontalListAdapter(Activity activity, int[] names, int[] images) {
        this.activity = activity;
        this.images = images;
        this.names = names;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_list_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {
        holder.textView.setText(names[position]);
        holder.imageView.setImageResource(images[position]);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "" + activity.getResources().getString(names[position]), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        RelativeLayout mainLayout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.mainLayout);
        }

    }


}
