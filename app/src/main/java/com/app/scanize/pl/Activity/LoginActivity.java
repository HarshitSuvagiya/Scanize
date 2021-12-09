package com.app.scanize.pl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.scanize.pl.Model.SendOtpModel;
import com.app.scanize.pl.R;
import com.app.scanize.pl.Services.ApiInterface;
import com.app.scanize.pl.Services.RetrofitClient;
import com.app.scanize.pl.Utils.Constans;
import com.app.scanize.pl.Utils.PrefrenceHandler;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "data-->";
    EditText edt_number;
    TextView tv_submit;
    String MobileNo;
    ProgressDialog progressDialog;
    PrefrenceHandler prefrenceHandler;
    String[] country_code = {"+970", "+972"};
    Spinner sp_code;
    String Country_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        if (!Constans.isOnline(LoginActivity.this)) {
            Constans.NoInternetDialog(LoginActivity.this);
        } else {
            GetKey();
        }

        prefrenceHandler = new PrefrenceHandler(LoginActivity.this);
        edt_number = findViewById(R.id.edt_number);
        tv_submit = findViewById(R.id.tv_submit);
        sp_code = (Spinner) findViewById(R.id.sp_code);
        sp_code.setOnItemSelectedListener(this);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country_code);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_code.setAdapter(aa);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Constans.isOnline(LoginActivity.this)) {
                    Constans.NoInternetDialog(LoginActivity.this);
                } else {
                    if (edt_number.getText().length() != 0 && edt_number.getText().length() == 9) {
                        MobileNo = edt_number.getText().toString();
                        if (MobileNo.startsWith("59")) {
                            SendOTP("Jawwal", MobileNo, getConvertJson("0",MobileNo, "pal_jawwal_meme"));
                        } else {
                            SendOTP("ooredoo", MobileNo, getConvertJson("1",MobileNo, "pal_ooredoo_drawit"));
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter valid Mobile number", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        edt_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i == 8) {
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void SendOTP(String type, String num, String object) {
        progressDialog.show();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (object));

        final ApiInterface apiInterface = RetrofitClient.getClient().create(ApiInterface.class);
        apiInterface.sendAPI1Otp(body).enqueue(new Callback<SendOtpModel>() {
            @Override
            public void onResponse(Call<SendOtpModel> call, Response<SendOtpModel> response) {

                SendOtpModel adsDataModel = response.body();
                if (response.isSuccessful()) {
                    if (adsDataModel.getResponse().equals("SUCCESS")) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+adsDataModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, OTPActivity.class).putExtra("num", num)
                                .putExtra("type", type));
                        finish();

                    } else {
                        progressDialog.dismiss();
                    }

                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<SendOtpModel> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public String getConvertJson(String type, String number, String appName) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appname", appName);
            jsonObject.put("apiname", "sendotp");
            jsonObject.put("msisdn", number);
            if (type.equals("1")) {
                jsonObject.put("tid", prefrenceHandler.getString(Constans.KEY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("TAG-->", "getConvertJson: " + jsonObject.toString());
        return jsonObject.toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Country_code = country_code[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void GetKey() {
        progressDialog.show();
        final ApiInterface apiInterface = RetrofitClient.getClient2().create(ApiInterface.class);
        apiInterface.getKey(Constans.getDateTime() + Constans.getRandomNumberString()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                String adsDataModel = response.body();
                Log.i(TAG, "onResponse: " + call.request().toString());
                Log.i(TAG, "onResponse: " + adsDataModel);
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    prefrenceHandler.setString(Constans.KEY, adsDataModel);
                } else {
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                call.cancel();
                progressDialog.dismiss();
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

}