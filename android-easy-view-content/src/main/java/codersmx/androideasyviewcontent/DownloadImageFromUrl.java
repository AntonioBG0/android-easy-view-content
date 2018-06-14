package codersmx.androideasyviewcontent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;

public class DownloadImageFromUrl extends AsyncTask<String, Void, Bitmap> {

    private String LOG = DownloadImageFromUrl.class.getSimpleName();

    DownloadImageFromUrl() {
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