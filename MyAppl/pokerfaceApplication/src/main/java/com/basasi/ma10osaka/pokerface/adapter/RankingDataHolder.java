package com.basasi.ma10osaka.pokerface.adapter;

public class RankingDataHolder {
    private static final String TAG = RankingDataHolder.class.getSimpleName();
    private final RankingDataHolder self = this;

    String mRank;
    String mName;
    String mScore;

    public RankingDataHolder(String rank, String name, String score){
        mRank = rank;
        mName = name;
        mScore = score;
    }
}