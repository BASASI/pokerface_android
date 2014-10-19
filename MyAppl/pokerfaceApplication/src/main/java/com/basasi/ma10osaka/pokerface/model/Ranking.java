package com.basasi.ma10osaka.pokerface.model;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.adapter.RankingDataHolder;
import com.basasi.ma10osaka.pokerface.adapter.RankingListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Ranking {
    private static final String TAG = Ranking.class.getSimpleName();
    private final Ranking self = this;

    private String requestUrl;

    private Context mContext;
    private ListView mListView;

    private List<RankingDataHolder> mRankingList = new ArrayList<RankingDataHolder>();

    public Ranking(Context context, ListView listView){
        mContext = context;
        mListView = listView;
    }

    public void requestRanking(){
        requestUrl = mContext.getString(R.string.ranking_api);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestUrl, null, mRankingListener, mEListener);
        queue.add(request);
        queue.start();
    }

    private Response.Listener<JSONObject> mRankingListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try{
                JSONArray ranking = jsonObject.getJSONArray("ranking");
                for(int i=1;i<=ranking.length();i++){
                    JSONObject rank = ranking.getJSONObject(i - 1);
                    Log.d(TAG, rank.toString());
                    mRankingList.add(new RankingDataHolder(Integer.toString(i), rank.getString("nickname"), Integer.toString(rank.getInt("score"))));
                }
                mListView.setAdapter(new RankingListViewAdapter(mContext,mRankingList));
            }catch (JSONException e){
                Log.d(TAG,e.toString());
            }

        }
    };

    private Response.ErrorListener mEListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.d(TAG, volleyError.toString());
        }
    };
}