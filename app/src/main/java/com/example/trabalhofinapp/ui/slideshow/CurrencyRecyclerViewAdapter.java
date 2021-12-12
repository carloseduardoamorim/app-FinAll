package com.example.trabalhofinapp.ui.slideshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalhofinapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyRecyclerViewAdapter extends RecyclerView.Adapter<CurrencyRecyclerViewAdapter.ViewHolder> {
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.######");

    public CurrencyRecyclerViewAdapter(ArrayList<CurrencyModel> currencyModelArrayList, Context context) {
        this.currencyModelArrayList = currencyModelArrayList;
        this.context = context;
    }

    public void filterList(ArrayList<CurrencyModel> filteredList){
        currencyModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item,parent, false);
        return new CurrencyRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRecyclerViewAdapter.ViewHolder holder, int position) {
        CurrencyModel currencyModel = currencyModelArrayList.get(position);
        holder.currencyNameTV.setText(currencyModel.getName());
        holder.currencySymbolTV.setText(currencyModel.getSymbol());
        holder.currencyRateTV.setText("$ " + df2.format(currencyModel.getPrice()));
    }

    @Override
    public int getItemCount() {
        return currencyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView currencyNameTV, currencySymbolTV, currencyRateTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyNameTV = itemView.findViewById(R.id.textViewCurrencyName);
            currencySymbolTV = itemView.findViewById(R.id.textViewSymbol);
            currencyRateTV = itemView.findViewById(R.id.textViewCurrencyRate);


        }
    }
}
