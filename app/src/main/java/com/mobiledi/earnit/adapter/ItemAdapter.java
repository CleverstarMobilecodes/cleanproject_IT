package com.mobiledi.earnit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Item;

import java.util.List;

/**
 * Created by mobile-di on 28/10/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> mItems;
    private ItemListener mListener;

    public ItemAdapter(List<Item> items, ItemListener listener) {
        mItems = items;
        mListener = listener;
    }

    public void setListener(ItemListener listener) {
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public Item item;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.choice_type);
        }

        public void setData(Item item) {
            this.item = item;
            textView.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    public interface ItemListener {
        void onItemClick(Item item);
    }
}