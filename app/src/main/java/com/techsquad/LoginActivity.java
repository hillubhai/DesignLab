package com.techsquad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText phone_edit_text;
    private TextInputEditText password_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        AppCompatButton login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()) checkPhoneInDB();
            }
        });
    }

    private boolean verify() {
        if (TextUtils.isEmpty(phone_edit_text.getText())) {
            Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone_edit_text.getText().toString().length() != 10) {
            Toast.makeText(this, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password_edit_text.getText() == null || password_edit_text.getText().length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void checkPhoneInDB() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL.PHONE_CHECK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject userObj = new JSONObject(response);
                    if (userObj.getInt("exists") == 1) {
                        login();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
                        builder.setTitle("User not found")
                                .setMessage("User does not exist. Register?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                    }
                } catch (JSONException | NullPointerException e) {
                    Toast.makeText(LoginActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null)
                    return;
                String statusCode = String.valueOf(error.networkResponse.statusCode), body;
                body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                if (statusCode.equals("500"))
                    Toast.makeText(LoginActivity.this, "Server could not be reached\nError code " + statusCode, Toast.LENGTH_SHORT).show();
                Log.e("PhoneError", body);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", phone_edit_text.getText().toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void login() {
        final CookieManager manager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Login", response);
                try {
                    JSONObject userObj = new JSONObject(response);
                    List<HttpCookie> cookieList = manager.getCookieStore().getCookies();
                    Log.e("cookie", cookieList.toString());
                    if (userObj.getInt("success") == 1) {
                        Toast.makeText(LoginActivity.this, "Logging in", Toast.LENGTH_SHORT).show();
                        User user = new User(userObj.getString("user_fname"), userObj.getString("user_lname"),
                                userObj.getString("user_email"), userObj.getString("user_mobile"), userObj.getInt("user_id"));
                        LoginCookies cookies = new LoginCookies(cookieList);
                        SharedPrefManager.getInstance(LoginActivity.this).userLogin(user, cookies);
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else
                        Toast.makeText(LoginActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
                } catch (JSONException | NullPointerException e) {
                    Toast.makeText(LoginActivity.this, "Error logging in", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null)
                    return;
                String statusCode = String.valueOf(error.networkResponse.statusCode), body;
                try {
                    body = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject jsonObject = new JSONObject(body);
                    if (jsonObject.getInt("success") == 0 && (statusCode.equals("400") || statusCode.equals("401")))
                        Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                    if (statusCode.equals("500"))
                        Toast.makeText(LoginActivity.this, "Cannot reach server", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Log.e("Exception", e.toString());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", phone_edit_text.getText().toString());
                params.put("pwd", password_edit_text.getText().toString());
                params.put("remember", "true");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(4000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
