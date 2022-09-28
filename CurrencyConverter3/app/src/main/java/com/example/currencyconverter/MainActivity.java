package com.example.currencyconverter;

import static com.example.currencyconverter.CurrencyConverter.getCurrencyConverterObjectFromJSONString;
import static com.example.currencyconverter.Utils.showInfoDialog;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private CurrencyConverter mCurrencyConverter;
    private boolean mClearAmountAfterCalculation;
    EditText amount, factor, from_ct, to_ct;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Converter", mCurrencyConverter.getJSONStringFromThis());
        outState.putBoolean("ClearCalculation", mClearAmountAfterCalculation);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrencyConverter = getCurrencyConverterObjectFromJSONString(savedInstanceState.getString("Converter"));
        mClearAmountAfterCalculation = savedInstanceState.getBoolean("ClearCalculation");
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setUpFab();
        setUpViews();
        mCurrencyConverter = new CurrencyConverter();
    }

    private void setUpViews() {
        amount = findViewById(R.id.edit_amount);
        factor = findViewById(R.id.edit_factor);
        from_ct = findViewById(R.id.edit_from_ct);
        to_ct = findViewById(R.id.edit_to_ct);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpFab() {
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> handleFab(view));
    }

    private void handleFab(View view) {
        String str_amount = amount.getText().toString();
        String str_factor = factor.getText().toString();

        str_amount = str_amount.length() > 0 ? str_amount : "1";
        str_factor = str_factor.length() > 0 ? str_factor : ".01";

        double dbl_amount = Double.parseDouble(str_amount);
        double dbl_factor = Double.parseDouble(str_factor);

        mCurrencyConverter.setCurrencyFromAmount(dbl_amount);
        mCurrencyConverter.setConversionPercentage(dbl_factor);

        double amount = mCurrencyConverter.getConvertedCurrencyAmount();
        Snackbar.make(view, "converted amounts: " + amount, Snackbar.LENGTH_LONG).show();

        clearFields();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_clear).setChecked(mClearAmountAfterCalculation);
        return super.onPrepareOptionsMenu(menu);
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
        if (id == R.id.action_info) {
            showInfoDialog(MainActivity.this, R.string.info, R.string.info_dialog);
            return true;
        }
        if (id == R.id.action_about) {
            showInfoDialog(MainActivity.this, R.string.about, R.string.about_dialog);
            return true;
        }
        if (id == R.id.action_clear) {
            toggleMenuItem(item);
            mClearAmountAfterCalculation = item.isChecked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleMenuItem(MenuItem item) {
        item.setChecked(!item.isChecked());
    }

    private void clearFields() {
        if (mClearAmountAfterCalculation) {
                amount.setText("");
                factor.setText("");
                from_ct.setText("");
                to_ct.setText("");
        }
    }

}