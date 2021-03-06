package com.example.top10downloader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class FeedAdapter<T extends FeedEntry> extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<T> applications;

    public FeedAdapter(@NonNull Context context, int resource, List<T> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;

    }


    @Override
    public int getCount() {
        return applications.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//       TextView tvName = convertView.findViewById(R.id.tvName);
//        TextView tvArtist =convertView.findViewById(R.id.tvArtist);
//        TextView tvSummary = convertView.findViewById(R.id.tvSummary);
        T CurrentApp = applications.get(position);

        viewHolder.tvName.setText(CurrentApp.getName());
        viewHolder.tvArtist.setText(CurrentApp.getArtist());
   //     viewHolder.tvSummary.setText(CurrentApp.getSummary());
        return convertView;
    }

    private class ViewHolder {

        final TextView tvName;
        final TextView tvArtist;
      //  final TextView tvSummary;

        public ViewHolder(View v) {
            this.tvName = v.findViewById(R.id.tvName);
            this.tvArtist = v.findViewById(R.id.tvArtist);
          //  this.tvSummary = v.findViewById(R.id.tvSummary);
            ;
        }


    }
}
