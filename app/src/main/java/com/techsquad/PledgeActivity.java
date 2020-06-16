package com.techsquad;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PledgeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pledge);
        Toolbar toolbar = findViewById(R.id.toolbar);
        final AppCompatSpinner medicine_name = findViewById(R.id.medicine_name);
        final AppCompatSpinner manufacturer_name = findViewById(R.id.manufacturer_name);
        AppCompatImageButton selectDoE = findViewById(R.id.selectDoE);
        final TextInputEditText date_of_expiry = findViewById(R.id.date_of_expiry);
        final TextInputEditText quantity = findViewById(R.id.quantity);
        quantity.setText("0");
        AppCompatImageButton selectDateTime = findViewById(R.id.selectDateTime);
        final TextInputEditText date_of_donation = findViewById(R.id.date_of_donation);
        AppCompatButton add_qty = findViewById(R.id.add_qty);
        AppCompatButton sub_qty = findViewById(R.id.sub_qty);
        Button donate = findViewById(R.id.donate);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(String.valueOf(quantity.getText())) + 1;
                quantity.setText(String.valueOf(q));
            }
        });
        sub_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(String.valueOf(quantity.getText())) - 1;
                quantity.setText(String.valueOf(Math.max(q, 0)));
            }
        });
        medicine_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                manufacturer_name.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        manufacturer_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                medicine_name.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectDoE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(PledgeActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        date_of_expiry.setText(date);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_WEEK)).show();
            }
        });
        selectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(PledgeActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        date_of_donation.setText(date);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_WEEK)).show();
            }
        });

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL.DONATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.optInt("success", -1) == 1) {
                        startActivity(new Intent(PledgeActivity.this, ConfirmationActivity.class));
                    }
                } catch (JSONException e) {
                    Log.e("DON_ERR", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null)
                    return;
                String statusCode = String.valueOf(error.networkResponse.statusCode), body;
                //Toast.makeText(NeedsActivity.this, "Server error. Error code: " + statusCode, Toast.LENGTH_SHORT).show();
                body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                Log.e("ERROR", statusCode + " " + body);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                LoginCookies loginCookies = SharedPrefManager.getInstance(PledgeActivity.this).getLogin();
                params.put("X-CSRF-Token", loginCookies.getCsrfAccessCookie());
                params.put("Cookie", LoginCookies.JWT_ACCESS_COOKIE_NAME + "=" + loginCookies.getAccessCookie() +
                        "; " + LoginCookies.JWT_REFRESH_COOKIE_NAME + "=" + loginCookies.getRefreshCookie() +
                        "; " + LoginCookies.JWT_ACCESS_CSRF_COOKIE_NAME + "=" + loginCookies.getCsrfAccessCookie() +
                        "; " + LoginCookies.JWT_REFRESH_CSRF_COOKIE_NAME + "=" + loginCookies.getCsrfRefreshCookie());
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("need", String.valueOf(medicine_name.getSelectedItemPosition()));
                params.put("schedule", String.valueOf(date_of_donation.getText()));
                params.put("quantity", String.valueOf(quantity.getText()));
                params.put("expiry", String.valueOf(date_of_expiry.getText()));
                params.put("lat", "-99");
                params.put("lng", "-99");
                params.put("address", "Kolkata");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Volley.newRequestQueue(PledgeActivity.this).add(stringRequest);
            }
        });
    }
}