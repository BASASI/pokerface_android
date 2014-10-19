package com.basasi.ma10osaka.pokerface.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.model.Card;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ResultFragment extends Fragment {
    private static final String TAG = ResultFragment.class.getSimpleName();
    private final ResultFragment self = this;

    @InjectView(R.id.result_text)
    TextView mResultText;
    @InjectView(R.id.result_score)
    TextView mResultScore;
    @InjectView(R.id.btn_back_ranking)
    Button mBackRankingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result,container,false);
        ButterKnife.inject(this, view);

        if(Card.point > 0){
            mResultText.setText("勝利！");
            mResultScore.setText("スコア" + Integer.toString(Card.point));
        }else{
            mResultText.setText("敗北…");
            mResultScore.setText("残念…，もっと強い顔を期待しているよ！");
        }

        return view;
    }

    @OnClick(R.id.btn_back_ranking)
    public void transactionMainFragment(){
        getFragmentManager().beginTransaction().replace(R.id.container,new MainFragment()).commit();
    }
}