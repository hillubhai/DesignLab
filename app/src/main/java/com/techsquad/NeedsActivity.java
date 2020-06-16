package com.techsquad;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NeedsActivity extends AppCompatActivity {

    private ArrayList<Medicine> currentData;
    private MedicineAdapter adapter;
    private TextView nobooking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_needs);
        adapter = new MedicineAdapter();
        nobooking = findViewById(R.id.noBooking);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        currentData = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.canScrollVertically();
        recyclerView.setLayoutManager(linearLayoutManager);
        //getData();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL.NEEDS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replaceAll("[\\\\]", "");
                response = response.replace("\"{", "{");
                response = response.replace("}\"", "}");
                Log.e("UpcomingFormatted", response);
                currentData = new ArrayList<>();
                try {
                    JSONArray userObj = new JSONObject(response).getJSONArray("needs_list");//new JSONArray(response);
                    nobooking.setVisibility(View.GONE);
                    for (int i = 0; i < userObj.length(); i++) {
                        JSONObject obj = userObj.getJSONObject(i);
                        int id = obj.optInt("med_id", -1);
                        int qty = obj.optInt("need_qty", 0);
                        String name = obj.optString("med_name", "");
                        Medicine dr = new Medicine(id, qty, name);
                        currentData.add(dr);
                    }
                    adapter.setMedicineList(currentData);
                } catch (JSONException e) {
                    currentData = new ArrayList<>();
                    //slimAdapter.updateData(currentData);
                    Log.e("UpcomingFormatParsError", e.toString());
                    nobooking.setVisibility(View.VISIBLE);
                }
                //swipeContainer.setRefreshing(false);
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
                LoginCookies loginCookies = SharedPrefManager.getInstance(NeedsActivity.this).getLogin();
                params.put("X-CSRF-Token", loginCookies.getCsrfAccessCookie());
                params.put("Cookie", LoginCookies.JWT_ACCESS_COOKIE_NAME + "=" + loginCookies.getAccessCookie() +
                        "; " + LoginCookies.JWT_REFRESH_COOKIE_NAME + "=" + loginCookies.getRefreshCookie() +
                        "; " + LoginCookies.JWT_ACCESS_CSRF_COOKIE_NAME + "=" + loginCookies.getCsrfAccessCookie() +
                        "; " + LoginCookies.JWT_REFRESH_CSRF_COOKIE_NAME + "=" + loginCookies.getCsrfRefreshCookie());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(stringRequest);
    }
}