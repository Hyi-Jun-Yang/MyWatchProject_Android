package com.test.projectwatch3_android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
  ArrayList<String> list;
  Bitmap[] imgArray;
    private EditText word;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcess();
            }
        }

        ).start();

        btn = (Button)findViewById(R.id.button);
        word = (EditText) findViewById(R.id.editText);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        watchListProcess();
                    }
                }.start();

            }
        });


    }

private Handler mHandler = new Handler();

public void doProcess(){
    list = new ArrayList<>();

    String urlAddress="http://192.168.14.9:8088/controller/and_select.do";
    HttpURLConnection conn = null;
    final String[] link;

    try {
        URL url = new URL(urlAddress);
        conn = (HttpURLConnection)url.openConnection();
        if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
            InputStream is =conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while((line= br.readLine())!=null){
                sb.append(line);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("clist"));
            imgArray = new Bitmap[jsonArray.length()];
            link = new String[jsonArray.length()];
            for(int i =0;i<jsonArray.length();i++) {

                JSONObject obj = (JSONObject) jsonArray.get(i);
                list.add(obj.getString("con_name") + ":" +
                         obj.getString("con_writer") + ":" +
                         obj.getString("scount") + ":" +
                         obj.getString("con_regedate")
                );

                link[i] = URLEncoder.encode(obj.getString("con_link"), "UTF-8");

                String file="http://192.168.14.9:5000/"+ URLEncoder.encode(obj.getString("con_link"), "UTF-8")+".png";
                Log.i("Main Log",file);

               url = new URL(file);
               is = url.openStream();

               Bitmap bm = BitmapFactory.decodeStream(is);
               imgArray[i]=Bitmap.createScaledBitmap(bm, 150, 100, true);

            }


            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ListView lv = (ListView)findViewById(R.id.listView);
                    lv.setAdapter(new ListAdapt(getApplicationContext(),list,imgArray));
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(),WatchPage.class);
                            intent.putExtra("link",link[position]);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    });


                }
            });

        }
    }catch (Exception ex){
      Log.i("Main Log",ex.toString());
    }

}

    public void watchListProcess(){
        Log.i("Search Log",word.getText().toString());
        HttpClient client = new DefaultHttpClient();
        String urlAddress="http://192.168.14.9:8088/controller/and_watchlist.do";
        HttpPost post = new HttpPost(urlAddress);
        ArrayList<NameValuePair> nameValues = new ArrayList<NameValuePair>();
        try {
            nameValues.add(new BasicNameValuePair("word", URLEncoder.encode(word.getText().toString(), "UTF-8")));
            post.setEntity(
                    new UrlEncodedFormEntity(nameValues, "UTF-8")
            );
        }catch(UnsupportedEncodingException ex){
            Log.e("Search Log", ex.toString());
        }
        list = new ArrayList<>();
        final String[] link;
        try {

            HttpResponse response = client.execute(post);
            InputStream is = response.getEntity().getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String Line = "";
            while((Line = br.readLine())!=null){
                sb.append(Line);
            }
            Log.i("Search Log", sb.toString());
            JSONObject jsonObject = new JSONObject(sb.toString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("wlist"));
            if(jsonArray.length()==0){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                final String res= "검색결과가 없습니다.";
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                getApplicationContext(),
                                res,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }else {

                imgArray = new Bitmap[jsonArray.length()];
                link = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    list.add(obj.getString("con_name") + ":" +
                                    obj.getString("con_writer") + ":" +
                                    obj.getString("scount") + ":" +
                                    obj.getString("con_regedate")
                    );

                    link[i] = URLEncoder.encode(obj.getString("con_link"), "UTF-8");

                    String file = "http://192.168.14.9:5000/" + URLEncoder.encode(obj.getString("con_link"), "UTF-8") + ".png";
                    Log.i("Main Log", file);

                    URL url = new URL(file);
                    is = url.openStream();

                    Bitmap bm = BitmapFactory.decodeStream(is);
                    imgArray[i] = Bitmap.createScaledBitmap(bm, 150, 100, true);

                }


                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ListView lv = (ListView) findViewById(R.id.listView);
                        lv.setAdapter(new ListAdapt(getApplicationContext(), list, imgArray));
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), WatchPage.class);
                                intent.putExtra("link", link[position]);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        });


                    }
                });
            }

        }catch (Exception ex){
            Log.i("Main Log",ex.toString());
        }

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
}
