package com.example.trabalhofinapp.ui.gallery;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalhofinapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private TextView convertFromDropdownTextView, convertToDropdownTextView, conversionRateText;
    private EditText amountToConvert;
    private ArrayList<String> arrayList;
    private Dialog fromDialog;
    private Dialog toDialog;
    private Button convertButton;
    private String convertFromValue, convertToValue, conversionValue;
    private String[] country = {"JPY", "CNY", "SDG", "RON", "MKD", "MXN", "CAD",
            "ZAR", "AUD", "NOK", "ILS", "ISK", "SYP", "LYD", "UYU", "YER", "CSD",
            "EEK", "THB", "IDR", "LBP", "AED", "BOB", "QAR", "BHD", "HNL", "HRK",
            "COP", "ALL", "DKK", "MYR", "SEK", "RSD", "BGN", "DOP", "KRW", "LVL",
            "VEF", "CZK", "TND", "KWD", "VND", "JOD", "NZD", "PAB", "CLP", "PEN",
            "GBP", "DZD", "CHF", "RUB", "UAH", "ARS", "SAR", "EGP", "INR", "PYG",
            "TWD", "TRY", "BAM", "OMR", "SGD", "MAD", "BYR", "NIO", "HKD", "LTL",
            "SKK", "GTQ", "BRL", "EUR", "HUF", "IQD", "CRC", "PHP", "SVC", "PLN",
            "USD"};
    private final String API_KEY = "e37707ee3ccf29ea989f";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        convertFromDropdownTextView = root.findViewById(R.id.convert_from_dropdown_menu);//getView().findViewById(R.id.convert_from_dropdown_menu);
        convertToDropdownTextView = root.findViewById(R.id.convert_to_dropdown_menu);
        convertButton = root.findViewById(R.id.conversionButton);
        conversionRateText = root.findViewById(R.id.conversionRateText);
        amountToConvert = root.findViewById(R.id.amountToConvertEditText);

        arrayList = new ArrayList<>();
        for (String i : country){
            arrayList.add(i);
        }

        convertFromDropdownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromDialog = new Dialog(getContext());
                fromDialog.setContentView(R.layout.from_spinner);
                fromDialog.getWindow().setLayout(650, 800);
                fromDialog.show();

                EditText editText = fromDialog.findViewById(R.id.edit_text);
                ListView listView = fromDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        convertFromDropdownTextView.setText(adapter.getItem(i));
                        fromDialog.dismiss();
                        convertFromValue = adapter.getItem(i);
                    }
                });
            }
        });

        convertToDropdownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toDialog = new Dialog(getContext());
                toDialog.setContentView(R.layout.to_spinner);
                toDialog.getWindow().setLayout(650, 800);
                toDialog.show();

                EditText editText = toDialog.findViewById(R.id.edit_text);
                ListView listView = toDialog.findViewById(R.id.list_view);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, arrayList);
                listView.setAdapter(adapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        adapter.getFilter().filter(charSequence);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        convertToDropdownTextView.setText(adapter.getItem(i));
                        toDialog.dismiss();
                        convertToValue = adapter.getItem(i);
                    }
                });
            }
        });
        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Double amountToConvert = Double.valueOf(GalleryFragment.this.amountToConvert.getText().toString());
                    getConversion(convertFromValue, convertToValue, amountToConvert);

                } catch (Exception e){

                }
            }
        });

        return root;
    }

    private String getConversion(String convertFromValue, String convertToValue, Double amountToConvert) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://free.currconv.com/api/v7/convert?q=" + convertFromValue + "_" + convertToValue + "&compact=ultra&apiKey=" + API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("Response API", response);
                    Double conversionRateValue = round(((Double) jsonObject.get(convertFromValue + "_" + convertToValue)), 4);
                    conversionValue = "" + round((conversionRateValue * amountToConvert), 3);
                    conversionRateText.setText(conversionValue);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Erro na requisição de câmbio", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
        return null;

    }

    private static double round(double value, int casas) {
        if (casas<0)
            throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(casas, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}