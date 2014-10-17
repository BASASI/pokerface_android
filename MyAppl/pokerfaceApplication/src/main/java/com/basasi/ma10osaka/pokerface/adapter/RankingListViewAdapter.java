package com.basasi.ma10osaka.pokerface.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.basasi.ma10osaka.pokerface.R;

import java.util.List;

class NavigationViewHolder {
    TextView rankView;
    TextView nameView;
    TextView scoreView;
}

public class RankingListViewAdapter extends ArrayAdapter<RankingDataHolder> {
    private static final String TAG = RankingListViewAdapter.class.getSimpleName();
    private final RankingListViewAdapter self = this;

    private LayoutInflater mLayoutInflater;

    public RankingListViewAdapter(Context context, List<RankingDataHolder> objects){
        super(context, 0, objects);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        NavigationViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.rank_row, parent, false);
            holder = new NavigationViewHolder();
            holder.rankView = (TextView) convertView.findViewById(R.id.rank_num);
            holder.nameView = (TextView) convertView.findViewById(R.id.rank_name);
            holder.scoreView = (TextView) convertView.findViewById(R.id.rank_score);


            convertView.setTag(holder);
        } else {
            holder = (NavigationViewHolder) convertView.getTag();
        }

        RankingDataHolder data = getItem(position);
        holder.rankView.setText(data.mScore);
        holder.nameView.setText(data.mName);
        holder.scoreView.setText(data.mScore);

        return convertView;
    }
}