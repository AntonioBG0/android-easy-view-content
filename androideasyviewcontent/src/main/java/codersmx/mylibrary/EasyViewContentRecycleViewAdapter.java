package codersmx.mylibrary;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Antonio BG - CodersMX on 23/05/16.
 * Project: EasyViewContent
 */
public class EasyViewContentRecycleViewAdapter extends RecyclerView.Adapter<EasyViewContentRecycleViewAdapter.ViewHolder> {


    private static final String LOG = EasyViewContentRecycleViewAdapter.class.getSimpleName();
    private Context context;
    private JSONArray data;
    private int resource;
    private ArrayList<String> keyNames;
    private ArrayList<Integer> viewIds;
    private ClickListener clickListener;
    private LongClickListener longClickListener;
    private int selectedPosition = 0, lastSelectedPosition;
    private Fragment fragment;
    private boolean customImgLoader;

    /**
     *
     * @param context context.
     * @param data json object.
     * @param resource layout id.
     * @param viewIds views id's within layer.
     * @param keyNames json key names.
     * @param fragment fragment (opcional).
     * @param customImgLoader configurator (optional).
     * @param clickListener row click listener (optional).
     * @param longClickListener row long click listener (optional).
     * @param selectedPosition default selected position.
     */
    EasyViewContentRecycleViewAdapter(Context context, JSONArray data, int resource, ArrayList<Integer> viewIds, ArrayList<String> keyNames, Fragment fragment, boolean customImgLoader, ClickListener clickListener, LongClickListener longClickListener, int selectedPosition) {
        this.data = data;
        this.keyNames = keyNames;
        this.viewIds = viewIds;
        this.resource = resource;
        this.context = context;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.selectedPosition = selectedPosition;
        this.fragment = fragment;
        this.customImgLoader = customImgLoader;
    }


    /**
     * Method which create views objects.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
         ArrayList<TextView> textViewsArrayList;
         ArrayList<ImageView> imageViewsArrayList;

        /**
         *
         * @param view list row parent view.
         */
         ViewHolder(View view) {
            super(view);

            //Create an arraylist to store views.
            textViewsArrayList = new ArrayList<>();
            imageViewsArrayList = new ArrayList<>();
            // Create objects
            for (Object oiViewIds : viewIds) {
                View field = view.findViewById(Integer.parseInt(oiViewIds.toString()));
                if (field instanceof ImageView) {
                    imageViewsArrayList.add((ImageView) field);
                } else if (field instanceof TextView) {
                    textViewsArrayList.add((TextView)field);
                }
            }
        }
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Iterator iJsonNames = keyNames.iterator();
        int data_position = holder.getAdapterPosition();
        try {
            // Get te actual object inside json.
            JSONObject c = data.getJSONObject(data_position);

            for(Object oiTextViews : holder.textViewsArrayList) {
                Object oiJsonNames = iJsonNames.next();
                // Set textview text
                ((TextView)oiTextViews).setText(c.getString(oiJsonNames.toString()));
                // Really I don't remmeber what's the functionality oh this hahahaha.
                if(((TextView)oiTextViews).getTag() != null) {
                    if (oiJsonNames.equals(((TextView)oiTextViews).getTag().toString())) {
                        ((TextView)oiTextViews).setText(c.getString(oiJsonNames.toString()).toUpperCase());
                    }
                }
            }

            for(Object oiImageViews : holder.imageViewsArrayList) {
                Object oiJsonNames = iJsonNames.next();

                String image_name = c.getString(oiJsonNames.toString()).replace(" ", "_");
                if(customImgLoader){
                    configureImageLoader(((ImageView)oiImageViews), image_name);
                }else {
                    try {
                        ((ImageView) oiImageViews).setImageBitmap(new DownloadImageFromUrl().execute(image_name).get());
                    }catch (Exception e){
                        Log.e(LOG, e.getLocalizedMessage());
                    }
                }

            }

            // Set View click action
            if(selectedPosition == holder.getAdapterPosition()){
                // Here I am just highlighting the background
                holder.itemView.setSelected(true);
                lastSelectedPosition = selectedPosition;
            }else{
                holder.itemView.setSelected(false);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {

                        if(lastSelectedPosition != holder.getAdapterPosition()) {
                            notifyItemChanged(lastSelectedPosition);
                        }else {
                            notifyItemChanged(holder.getAdapterPosition());
                        }
                        selectedPosition = holder.getAdapterPosition();
                        notifyItemChanged(holder.getAdapterPosition());
                        clickListener.onClickView(holder.getAdapterPosition());
                    }
                }
            });

            // Long click listener
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    if (longClickListener != null){
                        if(lastSelectedPosition != holder.getAdapterPosition()) {
                            notifyItemChanged(lastSelectedPosition);
                        }else {
                            notifyItemChanged(holder.getAdapterPosition());
                        }
                        selectedPosition = holder.getAdapterPosition();
                        notifyItemChanged(holder.getAdapterPosition());
                        longClickListener.onLongClickView(holder.getAdapterPosition());
                    }
                    return false;
                }
            });
        }catch (JSONException e){
            Log.e(LOG,e.getLocalizedMessage());
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length();
    }

    @Override
    public EasyViewContentRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(resource, parent, false));
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


    public interface ClickListener {
        void onClickView(int position);
    }

    public interface LongClickListener {
        void onLongClickView(int Position);
    }



}


