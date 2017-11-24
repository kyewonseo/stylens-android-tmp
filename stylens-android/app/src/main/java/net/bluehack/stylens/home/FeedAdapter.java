package net.bluehack.stylens.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.bluehack.stylens_android.R;

import java.util.ArrayList;

public class FeedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> thumbIds = new ArrayList<>();

    public FeedAdapter(Context context) {
        this.context = context;
    }

    public FeedAdapter(Context context, ArrayList<String> thumbIds) {
        this.context = context;
        this.thumbIds = thumbIds;
    }

    public void add(String thumbId) {
        this.thumbIds.add(thumbId);
    }

    public void clean() {
        if (thumbIds != null) {
            thumbIds.clear();
        }
    }


    public int getCount() {
        if (thumbIds == null) {
            return 0;
        }
        return thumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        ImageView imageView;
        if (view == null) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_feed_item, parent, false);
            holder = new ViewHolder();
            holder.iv_item = (ImageView) view.findViewById(R.id.iv_item);
            view.setTag(holder);

            // if it's not recycled, initialize some attributes
//            imageView = new ImageView(context);
//            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(4, 4, 4, 4);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Picasso.with(context).load(thumbIds.get(position)).into(holder.iv_item);
        return view;
    }


    static class ViewHolder {
        ImageView iv_item;
    }
}
