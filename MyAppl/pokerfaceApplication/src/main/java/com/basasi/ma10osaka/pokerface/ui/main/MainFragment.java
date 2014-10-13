package com.basasi.ma10osaka.pokerface.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.basasi.ma10osaka.pokerface.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    private final MainFragment self = this;

    @InjectView(R.id.btn_intent_camera)
    Button intentCamButton;
    @InjectView(R.id.btn_deck)
    Button deckButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        ButterKnife.inject(this, view);

        return view;
    }

    @OnClick(R.id.btn_intent_camera)
    public void onClickIntentCamButton(){
        ((MainActivity)getActivity()).intentToCamera();
    }

    @OnClick(R.id.btn_deck)
    public void transactionDeckFragment() {
        getFragmentManager().beginTransaction().replace(R.id.container,new DeckFragment()).addToBackStack(null).commit();
    }
}