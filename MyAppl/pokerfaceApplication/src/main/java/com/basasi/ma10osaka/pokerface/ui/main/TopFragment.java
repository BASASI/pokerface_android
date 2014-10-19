package com.basasi.ma10osaka.pokerface.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.model.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TopFragment extends Fragment {
    private static final String TAG = TopFragment.class.getSimpleName();
    private final TopFragment self = this;

    @InjectView(R.id.btn_start)
    Button mStartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top,container,false);
        ButterKnife.inject(this,view);

        return view;
    }

    @OnClick(R.id.btn_start)
    public void startLogin(){
        User user = new User(getActivity());
        user.setDeviceId();
        user.login(this);
    }

    public void transactionMainFragment(){
        getFragmentManager().beginTransaction().replace(R.id.container,new MainFragment()).commit();
    }
}