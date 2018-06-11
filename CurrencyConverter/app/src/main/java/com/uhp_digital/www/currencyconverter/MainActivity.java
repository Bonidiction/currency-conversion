package com.uhp_digital.www.currencyconverter;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.uhp_digital.www.currencyconverter.model.Currency;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<String> currencies = new ArrayList<>();
        final List<Currency> allData = new ArrayList<>();
        final Spinner spinnerFrom = (Spinner) findViewById(R.id.spinnerFROM);
        final Spinner spinnerTo = (Spinner) findViewById(R.id.spinnerTO);
        final TextView resultText = (TextView) findViewById(R.id.textView);
        final EditText inputText = (EditText) findViewById(R.id.editText);
        Button convertButton = (Button) findViewById(R.id.button);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //Get data
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        Api api = retrofit.create(Api.class);

        Call<List<Currency>> call = api.getCurrencies();

        call.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                allData.addAll(response.body());
                Log.println(Log.DEBUG, allData.toString(), "Parsed JSON");

                //Populate currencies list
                for (Currency currency : allData) {
                    currencies.add(currency.getCurrencyCode());
                }
                currencies.add("HRK");
            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //Init spinners
        initSpinner(spinnerFrom, currencies);
        initSpinner(spinnerTo, currencies);

        spinnerFrom.setOnItemSelectedListener(this);
        spinnerTo.setOnItemSelectedListener(this);

        //Configure button
        convertButton.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 Double startingAmount = Double.parseDouble(inputText.getText().toString());
                                                 String startingCurrency = spinnerFrom.getSelectedItem().toString();
                                                 String desiredCurrency = spinnerTo.getSelectedItem().toString();
                                                 resultText.setText(convert(startingCurrency, desiredCurrency, startingAmount, allData));
                                             }
                                         }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Converts currencies.
     * @param from Starting currency
     * @param to Desired currency
     * @param amount Amount to convert
     * @param data Data from http://hnbex.eu/api/v1/rates/daily/
     * @return Amount in desired currency
     */
    public String convert(String from, String to, Double amount, List<Currency> data) {
        Currency currency = null;

        for (Currency one : data) {
            if (one.getCurrencyCode().equals(from)) {
                currency = one;
                break;
            }
        }

        if (currency == null) {
            String errMessage = "Can't find requested currency data: " + from;
            Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
        }

        Double amountHrk = convertToHrk(amount, currency);

        for (Currency one : data) {
            if (one.getCurrencyCode().equals(to)) {
                currency = one;
                break;
            }
        }

        if (currency == null) {
            String errMessage = "Can't find requested currency data: " + to;
            Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
        }
        Double amountFinal = convertFromHrk(amountHrk, currency);

        return String.valueOf(amountFinal);
    }

    /**
     * Converts to Croation Kuna (base currency)
     * @param amount Amount of currency (3 EUR e.g.)
     * @param currencyData Currency from which conversion happens
     * @return Amount in Croatian Kunas
     */
    public Double convertToHrk(Double amount, Currency currencyData) {
        if (currencyData.getCurrencyCode().equals("HRK")) {
            return amount;
        }

        return amount / (currencyData.getMedianRate() * currencyData.getUnitValue());
    }

    /**
     * Converts from Croatian Kuna to currency of choice.
     * @param amount Amount of Kunas
     * @param currencyData Currency to which convert
     * @return Amount in given currency
     */
    public Double convertFromHrk(Double amount, Currency currencyData) {
        if (currencyData.getCurrencyCode().equals("HRK")) {
            return amount;
        }

        return amount * currencyData.getMedianRate() * currencyData.getUnitValue();
    }

    /**
     * Initialize spinner.
     * @param spinner Spinner to initialize
     * @param data Data to populate spinner with (currencies list e.g.)
     */
    public void initSpinner(Spinner spinner, List<String> data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, data);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spinner.setEnabled(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        adapterView.setSelection(0);
    }
}
