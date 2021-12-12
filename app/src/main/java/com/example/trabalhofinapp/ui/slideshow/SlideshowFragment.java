package com.example.trabalhofinapp.ui.slideshow;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhofinapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {
    private EditText editTextSearch;
    private RecyclerView recyclerViewCurrencies;
    private ProgressBar progressBarLoading;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private CurrencyRecyclerViewAdapter currencyRecyclerViewAdapter;
    private final String URL = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private final String API_HEADER = "X-CMC_PRO_API_KEY";
    private final String API_KEY = "1f5ae9a3-acaa-4186-8fff-9ac4e56c0056";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        editTextSearch = root.findViewById(R.id.editTextSearch);
        recyclerViewCurrencies = root.findViewById(R.id.recyclerViewCurrencies);
        progressBarLoading = root.findViewById(R.id.progressBarLoading);

        currencyModelArrayList = new ArrayList<>();
        currencyRecyclerViewAdapter = new CurrencyRecyclerViewAdapter(currencyModelArrayList, getContext());

        recyclerViewCurrencies.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewCurrencies.setAdapter(currencyRecyclerViewAdapter);

        getCurrencyData();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterCurrencies(editable.toString());
            }
        });

        return root;
    }



    private void filterCurrencies(String currency){
        ArrayList<CurrencyModel> filteredList = new ArrayList<>();
        for (CurrencyModel item : currencyModelArrayList){
            if (item.getName().toLowerCase().contains(currency.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()){
            Toast.makeText(getContext(), "Ativo não encontrado.", Toast.LENGTH_SHORT).show();
        } else {
            currencyRecyclerViewAdapter.filterList(filteredList);
        }
    }

    private void getCurrencyData(){
        progressBarLoading.setVisibility(View.VISIBLE);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBarLoading.setVisibility(View.GONE);
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String name = dataObj.getString("name");
                        String symbol = dataObj.getString("symbol");
                        JSONObject quote = dataObj.getJSONObject("quote");
                        JSONObject usd = quote.getJSONObject("USD");
                        double price = usd.getDouble("price");
                        currencyModelArrayList.add(new CurrencyModel(name, symbol, price));
                    }
                    currencyRecyclerViewAdapter.notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Falha ao extrair o json.", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarLoading.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Falha na requisição de ativos.", Toast.LENGTH_SHORT).show();
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<>();
                headers.put(API_HEADER, API_KEY);
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}