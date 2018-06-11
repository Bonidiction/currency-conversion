package com.uhp_digital.www.currencyconverter;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by bonidiction on 11.06.18..
 */

public class SpinnerListener implements AdapterView.OnItemSelectedListener {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapterView.setSelection(adapterView.getSelectedItemPosition());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        adapterView.setSelection(0);
    }
}
