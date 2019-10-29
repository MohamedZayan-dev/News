package com.example.android.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.List;

public class NewsAdapterActivity extends ArrayAdapter<NewsInfoActivity> {
    public NewsAdapterActivity(Context context, List<NewsInfoActivity> news) {
        super(context, 0, news);
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.info_container, parent, false);
        }

        NewsInfoActivity currentNews = getItem(position);

        TextView section = (TextView) listView.findViewById(R.id.sectionName);
        section.setText(currentNews.getmSection());

        TextView title = (TextView) listView.findViewById(R.id.Title);
        title.setText(currentNews.getmTitle());

        TextView date = (TextView) listView.findViewById(R.id.date);
        date.setText(currentNews.getmDate());

        return listView;
    }
}
