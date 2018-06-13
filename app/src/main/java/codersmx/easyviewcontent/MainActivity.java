package codersmx.easyviewcontent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import codersmx.androideasyviewcontent.EasyViewContent;
import codersmx.androideasyviewcontent.EasyViewContentActivity;
import codersmx.androideasyviewcontent.EasyViewContentRecycleViewAdapter;


public class MainActivity extends EasyViewContentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        // viewId
        ids.add(R.id.view_1);
        ids.add(R.id.image_1);

        // json keyNames
        values.add("textview_value");
        values.add("imageview_value");

        // Recycleview config.
        ArrayList<Integer> ids_recycleview = new ArrayList<>();
        ArrayList<String> values_recycleview = new ArrayList<>();

        // viewId recycleview
        ids_recycleview.add(R.id.row_textview);
        ids_recycleview.add(R.id.row_imageview);

        // json keyNames recycleview
        values_recycleview.add("textview_value");
        values_recycleview.add("imageview_value");

        try {

            // locally created json object.
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("textview_value", "EasyViewContent");
            jsonObject.put("imageview_value", "https://avatars0.githubusercontent.com/u/26068897?s=200&v=4");

            EasyViewContent easyViewContent = new EasyViewContent(this, getWindow().getDecorView().getRootView());
            easyViewContent.easyContentByJSON(ids, values, jsonObject);

            // locally created json array.
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            jsonArray.put(jsonObject);

            // Instantiate adapter for recycle view.
            RecyclerView.Adapter adapter = new EasyViewContentRecycleViewAdapter(
                    this,
                    jsonArray,
                    R.layout.list_row,
                    ids_recycleview,
                    values_recycleview
            );

            recyclerView.setAdapter(adapter);


        }catch (JSONException e){
            Log.e(MainActivity.class.getSimpleName(), e.getLocalizedMessage());
        }

    }

    @Override
    public void setUpCustomImageLoader(ImageView imageView, String url){
        try {
            // Example of custom image loader (like android universal image loader from nostra13)
            imageView.setImageBitmap(new exampleImageLoader().execute(url).get());
        }catch (Exception e){
            Log.e(MainActivity.class.getSimpleName(), e.getLocalizedMessage());
        }
    }


    public static class exampleImageLoader extends AsyncTask<String, Void, Bitmap> {
        private String LOG = exampleImageLoader.class.getSimpleName();

        exampleImageLoader() {
        }

        protected Bitmap doInBackground(String... args) {
            String url = args[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(LOG, e.getLocalizedMessage());
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
        }
    }


}
