package com.basasi.ma10osaka.pokerface.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.model.BitmapCache;
import com.basasi.ma10osaka.pokerface.model.Card;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GetCardFragment extends Fragment {
    private static final String TAG = GetCardFragment.class.getSimpleName();
    private final GetCardFragment self = this;

    @InjectView(R.id.get_card_image)
    ImageView mYourCardImage;
    @InjectView(R.id.get_card_atk)
    TextView mYourCardAtk;
    @InjectView(R.id.get_card_def)
    TextView mYourCardDef;
    @InjectView(R.id.support_image)
    ImageView mSupportImage;
    @InjectView(R.id.support_atk)
    TextView mSupportAtk;
    @InjectView(R.id.support_def)
    TextView mSupportDef;
    @InjectView(R.id.btn_start_battle)
    Button mStartBattleButton;

    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_card,container,false);
        ButterKnife.inject(this, view);

        mQueue = Volley.newRequestQueue(getActivity());
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());

        mYourCardAtk.setText("攻撃力：" + Card.my.getAtk());
        mYourCardDef.setText("防御力：" + Card.my.getDef());
        mSupportAtk.setText("攻撃力：" + Card.supporter.getAtk());
        mSupportDef.setText("防御力：" + Card.supporter.getDef());


        ImageLoader.ImageListener myImageListener = ImageLoader.getImageListener(mYourCardImage, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        mImageLoader.get(Card.my.getImageUrl(), myImageListener);
        ImageLoader.ImageListener supportImageListener = ImageLoader.getImageListener(mSupportImage, android.R.drawable.ic_menu_rotate, android.R.drawable.ic_delete);
        mImageLoader.get(Card.supporter.getImageUrl(), supportImageListener);

        return view;
    }

    @OnClick(R.id.btn_start_battle)
    public void transactionResultFragment(){
        getFragmentManager().beginTransaction().replace(R.id.container,new ResultFragment()).commit();
    }
}