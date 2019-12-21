package com.example.flaskandandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button bt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        bt2=findViewById(R.id.button2);
        final EditText ed1=findViewById(R.id.editText);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startit(ed1.getText().toString());
            }
        });


    }

    public int uploadit(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.43.120:5000/");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("POST");

                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream ());

                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("key1" , "value1");
                        obj.put("key2" , "value2");

                        wr.writeBytes(obj.toString());
                        Log.i("JSON Input", obj.toString());
                        wr.flush();
                        wr.close();
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    urlConnection.connect();

                    int responseCode = urlConnection.getResponseCode();

                    if(responseCode == HttpURLConnection.HTTP_OK){
                        String server_response = readStream(urlConnection.getInputStream());

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return 0;
    }

    public int startit(String s) {
        final String ss=s;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    URL url = new URL(ss);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() != 200) {
                        Log.i("Error is","Nothings it is");
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));

                    String output;
                    while ((output = br.readLine()) != null) {
                        textView.setText(output);
                    }

                    conn.disconnect();

                } catch (MalformedURLException e) {

                    e.printStackTrace();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }
        }).start();

        return 0;

    }
    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
