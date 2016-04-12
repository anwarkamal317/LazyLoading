package androod.com.lazyloading;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ch on 4/12/2016.
 */
public class CustomAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<DataModel> feedItems;
    private Context context;
    viewholder vh;
    DataModel item;
    public CustomAdapter(Activity activity, List<DataModel> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }
    @Override
    public int getCount() {
        return feedItems.size();
    }
    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            vh=new viewholder();
            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.listviewitem, null);

            vh.Name = (TextView) convertView.findViewById(R.id.name);
            vh.imgView = (ImageView) convertView.findViewById(R.id.imageView);
            vh.laymain = (LinearLayout) convertView.findViewById(R.id.laymain);

            convertView.setTag(vh);
        }
        else
        {
            vh=(viewholder) convertView.getTag();
        }




        vh.Name.setText(feedItems.get(position).Name.toUpperCase());
        Picasso.with(context).load(feedItems.get(position).ImagePath).into(vh.imgView);

        vh.laymain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(activity, MainActivity.class);
                activity.startActivity(i);
            }
        });


        return convertView;
    }
    static class viewholder
    {
        TextView Name;
        ImageView imgView;
        LinearLayout laymain;
    }
}