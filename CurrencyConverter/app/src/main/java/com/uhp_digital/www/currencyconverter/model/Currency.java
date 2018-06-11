package com.uhp_digital.www.currencyconverter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bonidiction on 11.06.18..
 */

public class Currency {
    @SerializedName("unit_value")
    @Expose
    private Long unitValue;
    @SerializedName("currency_code")
    @Expose
    private String currencyCode;
    @SerializedName("median_rate")
    @Expose
    private String medianRate;
    @SerializedName("buying_rate")
    @Expose
    private String buyingRate;
    @SerializedName("selling_rate")
    @Expose
    private String sellingRate;

    public Currency() {
    }

    public Currency(Long unitValue, String currencyCode, String medianRate, String buyingRate, String sellingRate) {
        super();
        this.unitValue = unitValue;
        this.currencyCode = currencyCode;
        this.medianRate = medianRate;
        this.buyingRate = buyingRate;
        this.sellingRate = sellingRate;
    }

    public Long getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Long unitValue) {
        this.unitValue = unitValue;
    }

    public Currency withUnitValue(Long unitValue) {
        this.unitValue = unitValue;
        return this;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Currency withCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public Double getMedianRate() {
        return Double.parseDouble(medianRate);
    }

    public void setMedianRate(String medianRate) {
        this.medianRate = medianRate;
    }

    public Currency withMedianRate(String medianRate) {
        this.medianRate = medianRate;
        return this;
    }

    public Double getBuyingRate() {
        return Double.parseDouble(buyingRate);
    }

    public void setBuyingRate(String buyingRate) {
        this.buyingRate = buyingRate;
    }

    public Currency withBuyingRate(String buyingRate) {
        this.buyingRate = buyingRate;
        return this;
    }

    public Double getSellingRate() {
        return Double.parseDouble(sellingRate);
    }

    public void setSellingRate(String sellingRate) {
        this.sellingRate = sellingRate;
    }

    public Currency withSellingRate(String sellingRate) {
        this.sellingRate = sellingRate;
        return this;
    }
}
