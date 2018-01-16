package com.mobiledi.earnit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobiledi.earnit.R;
import com.mobiledi.earnit.model.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mobile-di on 31/10/17.
 */

public class CountryAdapter  extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private ArrayList<Country> countries;
    private CountryListner cListener;

    public CountryAdapter(ArrayList<Country> countries, CountryListner listener) {
        this.countries = countries;
        cListener = listener;
    }

    public void setListener(CountryListner listener) {
        cListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(countries.get(position));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public Country country;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.choice_type);
        }

        public void setData(Country country) {
            this.country = country;
            textView.setText(country.getCountryName()+ " - "+ country.getCountryCode());
        }

        @Override
        public void onClick(View v) {
            if (cListener != null) {
                cListener.onItemClick(country);
            }
        }
    }

    public interface CountryListner {
        void onItemClick(Country country);
    }
}