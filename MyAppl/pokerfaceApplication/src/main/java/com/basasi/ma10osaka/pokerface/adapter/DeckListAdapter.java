package com.basasi.ma10osaka.pokerface.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.model.BitmapCache;
import com.basasi.ma10osaka.pokerface.model.Card;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DeckListAdapter extends BaseAdapter {
    private static final String TAG = DeckListAdapter.class.getSimpleName();
    private final DeckListAdapter self = this;

    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    private Context context;
    private List<Card> list;
    private Card mCard;
    private Resources resources;

    static class ResultViewHolder{
        @InjectView(R.id.card_atk)
        TextView atk;
        @InjectView(R.id.card_def)
        TextView def;
        @InjectView(R.id.card_image)
        ImageView image;

        public ResultViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }

    public DeckListAdapter(Context context, List<Card> cardList) {
        super();
        this.context = context;
        list = new ArrayList<Card>();
        list = cardList;
        resources = context.getResources();

        mQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    @Override
    public int getCount() throws NullPointerException {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        ResultViewHolder holder;
        mCard = (Card)getItem(position);

        if(view != null){
            holder = (ResultViewHolder) view.getTag();
        }else{
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.deck_row, parent, false);
            holder = new ResultViewHolder(view);
            view.setTag(holder);
        }


        holder.atk.setText("攻撃力：" + mCard.getAtk());
        holder.def.setText("防御力：" + mCard.getDef());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.image, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        mImageLoader.get(mCard.getImageUrl(), listener);

        return view;

    }




}