package com.uhp_digital.www.currencyconverter;

import com.uhp_digital.www.currencyconverter.model.Currency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by bonidiction on 11.06.18..
 */

public interface Api {
    String BASE_URL = "http://hnbex.eu/api/v1/rates/";

    @GET("daily")
    Call<List<Currency>> getCurrencies();
}
