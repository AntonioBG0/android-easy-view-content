package codersmx.mylibrary;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by CodersMX on 23/05/16.
 * Project: EasyViewContent
 */
public  class EasyViewContent {


    private static final String LOG = EasyViewContent.class.getSimpleName();
    private Context context;
    private View view;
    private Fragment fragment;
    private boolean customImgLoader = false;


    /**
     * If the instance will be image configurable.
     *
     * @param context context
     * @param parentView parent view.
     * @param fragment fragment.
     * @param customImgLoader image configuration tag.
     *
     */
    public EasyViewContent(Context context, View parentView, Fragment fragment, boolean customImgLoader){
        this.context = context;
        this.view = parentView;
        this.fragment = fragment;
        this.customImgLoader = customImgLoader;
    }

    /**
     * Default constructor.
     *
     * @param context context
     * @param parentView parent view.
     *
     */
    public EasyViewContent(Context context, View parentView){
        this.context = context;
        this.view = parentView;
    }


    /**
     * Public method for fill the by by JSON data.
     *
     * @param viewId Id's of the view which gonna be filled.
     * @param keyNames name of the value key.
     * @param data json object format data.
     *
     *
     */
    public void easyContentByJSON(final ArrayList<Integer> viewId, final ArrayList keyNames, final JSONObject data){
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    fillViewMethods(viewId, keyNames, data);
                }catch (NullPointerException e){
                    Log.e(LOG, e.getLocalizedMessage());
                }
            }
        });
    }

    /**
     * Private method for fill the by by JSON data.
     *
     * @param viewId Id's of the view which gonna be filled.
     * @param keyNames name of the value key.
     * @param data json object format data.
     *
     *
     */
    private void fillViewMethods(final ArrayList<Integer> viewId, final ArrayList keyNames, final JSONObject data){
        try {
            Iterator keyn = keyNames.iterator();
            for (Object oidi : viewId) {
                Object oikey = keyn.next();
                View field = view.findViewById(Integer.parseInt(oidi.toString()));
                // Check view instance type.
                if(field instanceof CheckBox){
                    if(Integer.parseInt(data.getString(oikey.toString())) == 1 || data.getString(oikey.toString()).equals("true") || data.getString(oikey.toString()).equals("yes")) {
                        ((CheckBox)field).setChecked(true);
                    }
                    if (Integer.parseInt(data.getString(oikey.toString())) == 0 || data.getString(oikey.toString()).equals("false") || data.getString(oikey.toString()).equals("no")) {
                        ((CheckBox)field).setChecked(false);
                    }
                } else if(field instanceof EditText){
                    ((EditText)field).setText(data.getString(oikey.toString()).replace("\n", ""));
                } else if(field instanceof ProgressBar){
                    ObjectAnimator progressAnimator = ObjectAnimator.ofInt(field, "progress", 0, Integer.parseInt(data.getString(oikey.toString()).replace("%", "")));
                    progressAnimator.setDuration(1000);
                    progressAnimator.setInterpolator(new LinearInterpolator());
                    progressAnimator.start();
                } else if (field instanceof TextView) {
                    ((TextView) field).setText(data.getString(oikey.toString()).replace("\n", ""));
                } else if (field instanceof ImageView) {
                    if(customImgLoader){
                        configureImageLoader(((ImageView)field), data.getString(oikey.toString()));
                    }else {
                        ((ImageView)field).setImageBitmap(new DownloadImageFromUrl().execute(data.getString(oikey.toString())).get());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG, e.getLocalizedMessage());
        }
    }

    /**
     * Public method for fill the by by JSON data.
     *
     * @param imageView image view.
     * @param url value for image view.
     *
     *
     */
    private void configureImageLoader(ImageView imageView, String url){
        if(fragment instanceof EasyViewContentFragment) {
            ((EasyViewContentFragment) fragment).setUpCustomImageLoader(imageView, url);
        }else if(context instanceof EasyViewContentActivity){
            ((EasyViewContentActivity)context).setUpCustomImageLoader(imageView, url);
        }
    }


    /**
     * Return a recycle view adapter
     *
     * @param data JSONArray.
     * @param resource List row layout id.
     * @param fragment This is required for custom image loader.
     * @param viewId List of views id in layout.
     * @param keyNames List of json key names.
     * @param customImgLoader Boolean to check if will configure the loader.
     * @param clickListener Configure row click listener.
     * @param longClickListener Configure row long click.
     * @param selectedPosition Defualt selected position.
     * @return RecyclerView.Adapter
     */
    public RecyclerView.Adapter recycleView(final JSONArray data, int resource, final Fragment fragment,
                                            final ArrayList<Integer> viewId,
                                            final ArrayList<String> keyNames,
                                            boolean customImgLoader,
                                            EasyViewContentRecycleViewAdapter.ClickListener clickListener,
                                            EasyViewContentRecycleViewAdapter.LongClickListener longClickListener,
                                            int selectedPosition){
        return new EasyViewContentRecycleViewAdapter(context, data, resource, viewId, keyNames, fragment, customImgLoader, clickListener, longClickListener, selectedPosition);
    }


}
