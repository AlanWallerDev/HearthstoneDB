package com.example.t00053669.hearthstonedb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.R.attr.uiOptions;
import static android.R.attr.x;
import static android.R.string.no;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.os.Build.VERSION_CODES.M;
import static com.example.t00053669.hearthstonedb.R.id.search;

public class MainActivity extends AppCompatActivity {
//http://stackoverflow.com/questions/19945411/android-java-how-can-i-parse-a-local-json-file-from-assets-folder-into-a-listvi

    ArrayList<HashMap<String, String>> cardList = null;
    JSONArray bArray = null;
    JSONArray tgtArray = null;
    JSONArray MSoGArray = null;
    JSONArray GvGArray = null;
    JSONArray kArray = null;
    JSONArray nArray = null;
    JSONArray wArray = null;
    JSONArray bmArray = null;
    JSONArray lArray = null;
    JSONArray cArray = null;
    JSONArray rArray = null;
    JSONArray uArray = null;
    JSONArray kftArray = null;
    JSONArray KnCArray = null;
    String url = null;
    ImageView imageView;
    JSONObject bObj;
    HashMap<String, String> m_li;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoCompleteTextView search = (AutoCompleteTextView) findViewById(R.id.search);
        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
         //       | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        //decorView.setSystemUiVisibility(uiOptions);



        try {

            JSONObject obj = new JSONObject(loadJson());

            bArray = obj.getJSONArray("Basic");
            tgtArray = obj.getJSONArray("The Grand Tournament");
            MSoGArray = obj.getJSONArray("Mean Streets of Gadgetzan");
            GvGArray = obj.getJSONArray("Goblins vs Gnomes");
            kArray = obj.getJSONArray("One Night in Karazhan");
            nArray = obj.getJSONArray("Naxxramas");
            wArray = obj.getJSONArray("Whispers of the Old Gods");
            bmArray = obj.getJSONArray("Blackrock Mountain");
            lArray = obj.getJSONArray("The League of Explorers");
            cArray = obj.getJSONArray("Classic");
            rArray = obj.getJSONArray("Hall of Fame");
            uArray = obj.getJSONArray("Journey to Un'Goro");
            kftArray = obj.getJSONArray("Knights of the Frozen Throne");
            KnCArray = obj.getJSONArray("Kobolds & Catacombs");

            cardList = new ArrayList<HashMap<String, String>>();

            //the following for loops populate cardList with all the games cards
            jsonReader(KnCArray);
            jsonReader(tgtArray);
            jsonReader(MSoGArray);
            jsonReader(GvGArray);
            jsonReader(kArray);
            jsonReader(nArray);
            jsonReader(wArray);
            jsonReader(bmArray);
            jsonReader(lArray);
            jsonReader(cArray);
            jsonReader(rArray);
            jsonReader(uArray);
            jsonReader(kftArray);
            jsonReader(bArray);

        }catch(JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> cards = new ArrayList<String>();
        HashMap<String, String> y = null;
        int x = 0;
        for(int i = 0; i<cardList.size(); i++){
            y = cardList.get(i);
            try {
                    cards.add(x, y.get("name"));
                    x++;
            }catch(NullPointerException e){
                e.printStackTrace();
            }
        }

        int arraysize = cards.size();

        String[] cardArray = new String[arraysize];

        for(int i = 0; i < cardArray.length; i++){
            cardArray[i] = cards.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cardArray);
        search.setAdapter(adapter);

        EditText searchText = (EditText) findViewById(R.id.search);

        searchText.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event){
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    if(event.getAction() == KeyEvent.ACTION_UP){

                        EditText search = (EditText) findViewById(R.id.search);
                        LinearLayout layout = (LinearLayout) findViewById(R.id.scrollView);
                        layout.removeAllViews();
                        ArrayList<String> urlArray = new ArrayList<String>();
                        HashMap<String, String> y = null;
                        for(int i = 0; i < cardList.size(); i++){
                            y = cardList.get(i);
                            if(((y.get("type").equalsIgnoreCase(search.getText().toString())) ||y.get("name").toUpperCase().contains(search.getText().toString().trim().toUpperCase())) && y.containsKey("img")){
                                String newUrl = y.get("img");
                                //Log.d("UrlString", newUrl);
                                urlArray.add(newUrl);
                            }

                        }

                        View view = v;
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        search.clearFocus();

                        //imageView = (ImageView) findViewById(R.id.imageView2);
                        if(urlArray.size() == 0) {
                            Toast.makeText(MainActivity.this, "Card Not Found", Toast.LENGTH_SHORT).show();
                        }else if(search.getText().toString().length() < 3){
                            Toast.makeText(MainActivity.this, "Input too short", Toast.LENGTH_SHORT).show();
                        }else {

                            for(int i = 0; i < urlArray.size(); i++){
                                WorkerThread task = new WorkerThread();
                                url = urlArray.get(i);
                                task.execute(new String[]{url});
                            }

                        }
                        return true;
                    }else{
                        return false;
                    }
                }else{
                    return false;
                }
            }

        });
    }



    protected void onClick(View view){
        EditText search = (EditText) findViewById(R.id.search);
        HashMap<String, String> y = null;
        for(int i = 0; i < cardList.size(); i++){
            y = cardList.get(i);
            if(y.get("name").equalsIgnoreCase(search.getText().toString()) && y.containsKey("img")){
                url = y.get("img");
                break;
            }else
                url = null;

        }

        //imageView = (ImageView) findViewById(R.id.imageView2);
        if(url == null){
            Toast.makeText(this, "Card Not Found", Toast.LENGTH_SHORT).show();
        }else {
            WorkerThread task = new WorkerThread();

            task.execute(new String[]{url});
        }
    }

    protected String loadJson(){
        String json = null;
        try {
            InputStream in = getResources().openRawResource(R.raw.data);

            int size = in.available();
            byte[] buffer = new byte[size];

            in.read(buffer);
            in.close();

            json = new String(buffer, "UTF-8");


        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

        return json;


    }

    public void jsonReader( JSONArray jArray){
        try {
            for (int i = 0; i < jArray.length(); i++) {
                bObj = (JSONObject) jArray.getJSONObject(i);
                if (bObj.has("collectible")) {
                    String cardIdVal = bObj.getString("cardId");
                    String cardNameVal = bObj.getString("name");
                    String cardTypeVal = bObj.getString("type");

                    m_li = new HashMap<String, String>();
                    m_li.put("type", cardTypeVal);
                    m_li.put("cardId", cardIdVal);
                    m_li.put("name", cardNameVal);
                    if (bObj.has("text")) {
                        String cardTextVal = bObj.getString("text");
                        m_li.put("text", cardTextVal);
                    }
                    if (bObj.has("img")) {
                        String cardImgVal = bObj.getString("img");
                        String goldImgVal = bObj.getString("imgGold");
                        m_li.put("imgGold", goldImgVal);
                        m_li.put("img", cardImgVal);
                    }

                    if (bObj.has("collectible")) {
                        boolean cardColVal = bObj.getBoolean("collectible");
                        String isCol = "" + cardColVal;
                        m_li.put("collectible", isCol);
                    } else {
                        m_li.put("collectible", "false");
                    }

                    cardList.add(m_li);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private class WorkerThread extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            for (String url : urls) {
                map = downloadImage(url);
            }
            return map;
        }

        protected void onPostExecute(Bitmap result) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.scrollView);
            ImageView image = new ImageView(MainActivity.this);
            image.setImageBitmap(result);


            layout.addView(image);
            final float scale = getResources().getDisplayMetrics().density;

            int dpheight = (int) (420 * scale);
            image.getLayoutParams().height = dpheight;

        }
    }

        private Bitmap downloadImage(String url){
            Bitmap bitmap = null;
            InputStream stream = null;
            BitmapFactory.Options bmOptions= new BitmapFactory.Options();
            bmOptions.inSampleSize = 1;

            try{
                stream = getHttpConnection(url);
                bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                stream.close();
            }catch(IOException e){
                e.printStackTrace();
            }catch(NullPointerException e){
                e.printStackTrace();
            }
            return bitmap;
        }

        private InputStream getHttpConnection(String urlString) throws IOException{
            InputStream stream = null;
            //Log.d("url: ", urlString);
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();

            try{
                HttpURLConnection httpConnection = (HttpURLConnection) connection;
                httpConnection.setRequestMethod("GET");
                httpConnection.connect();

                if(httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    stream = httpConnection.getInputStream();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return stream;
        }





}

