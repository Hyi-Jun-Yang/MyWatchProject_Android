package com.test.projectwatch3_android;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;


public class WatchPage extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.videowatch);
        String link = getIntent().getStringExtra("link");
        String urlAddress = "http://192.168.14.9:5000/" + link;
        Log.i("Main Log", urlAddress);
        VideoView vi = (VideoView) findViewById(R.id.videoView);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(vi);
        Uri video = Uri.parse(urlAddress);
        vi.setMediaController(mc);
        vi.setVideoURI(video);
        vi.requestFocus();
        vi.start();

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
