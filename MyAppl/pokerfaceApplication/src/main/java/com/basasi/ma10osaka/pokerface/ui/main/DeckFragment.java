package com.basasi.ma10osaka.pokerface.ui.main;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.model.Card;

import butterknife.ButterKnife;

public class DeckFragment extends ListFragment {
    private static final String TAG = DeckFragment.class.getSimpleName();
    private final DeckFragment self = this;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);

        ListView listView = getListView();
        listView.setScrollBarStyle(ListView.SCROLLBARS_OUTSIDE_OVERLAY);
        listView.setDivider(null);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deck,container,false);

        Card card = new Card();

        //DeckListAdapter adapter = new DeckListAdapter(getActivity(), );

        ButterKnife.inject(this, view);

        card.requestDeck(this);



        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}