package com.testapp.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.testapp.R;
import com.testapp.model.Pojo;

import java.util.ArrayList;

/**
 * Created by cws on 19/10/16.
 */
public class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.DataObjectHolder> {

    static Context con;
    private ArrayList<Pojo> mDataset;

    public VerticalListAdapter(Context con, ArrayList<Pojo> myDataset) {
        mDataset = myDataset;
        this.con = con;
    }

    @Override
    public VerticalListAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_list_item, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(VerticalListAdapter.DataObjectHolder holder, int position) {
        holder.name.setText(mDataset.get(position).getName());
        holder.addres.setText(mDataset.get(position).getAddress());
        if (mDataset.get(position).getLocation().equals("1")) {
            holder.distance.setText(mDataset.get(position).getLocation() + " Km");
        } else {
            holder.distance.setText(mDataset.get(position).getLocation() + " Kms");
        }
        Glide.with(con)
                .load(mDataset.get(position).getImage()).placeholder(con.getResources().getDrawable(android.R.drawable.ic_menu_gallery))
                .fitCenter()
                .into(holder.img);
    }

    public void addItem(Pojo dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView name;
        TextView addres;
        TextView distance;
        ImageView img;
        LinearLayout linearLayout;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.title);
            addres = (TextView) itemView.findViewById(R.id.address);
            distance = (TextView) itemView.findViewById(R.id.distance);
            img = (ImageView) itemView.findViewById(R.id.myImage);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.Item);
            linearLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ((MyClickListener) con).onItemClick(getPosition(), v);
        }
    }


}
