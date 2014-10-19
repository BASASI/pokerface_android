package com.basasi.ma10osaka.pokerface.model;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.adapter.DeckListAdapter;
import com.basasi.ma10osaka.pokerface.ui.main.DeckFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private static final String TAG = Card.class.getSimpleName();
    private final Card self = this;

    private String mAtk;
    private String mDef;
    private String mImageUrl;
    private String mNickname;
    private DeckFragment mFragment;

    public static List<Card> cardList = new ArrayList<Card>();

    public static Card my;
    public static Card supporter;
    public static Card enemy1;
    public static Card enemy2;
    public static int point;


    public Card() {

    }

    public Card(String atk, String def, String imageUrl, String nickname){
        mAtk = atk;
        mDef = def;
        mImageUrl = imageUrl;
        mNickname = nickname;
    }

    public String getNickname() {
        return mNickname;
    }

    public void setNickname(String nickname) {
        mNickname = nickname;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getDef() {
        return mDef;
    }

    public void setDef(String def) {
        mDef = def;
    }

    public String getAtk() {
        return mAtk;
    }

    public void setAtk(String atk) {
        mAtk = atk;
    }



    public void requestDeck(DeckFragment fragment){
        mFragment = fragment;
        RequestQueue queue = Volley.newRequestQueue(mFragment.getActivity());
        JsonArrayRequest request = new JsonArrayRequest(mFragment.getActivity().getString(R.string.deck_api)+ "?user_id=" +Integer.toString(User.getUserId()),mListener,mEListener);
        queue.add(request);
        queue.start();
    }

    private Response.Listener<JSONArray> mListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray jsonArray) {
            Log.d(TAG, jsonArray.toString());
            for(int i=0; i<jsonArray.length();i++){
                try{
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Card card = new Card(obj.getString("offense"),obj.getString("defense"),obj.getString("image_url"),User.getNickName());
                    cardList.add(card);

                }catch(JSONException e){
                    Log.d(TAG,e.toString());
                }

            }

            mFragment.setListAdapter(new DeckListAdapter(mFragment.getActivity(), cardList));


        }
    };

    private Response.ErrorListener mEListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

            Log.d(TAG,volleyError.toString());
        }
    };


}