package com.example.topza.piggy;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.security.PublicKey;
import java.util.HashMap;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class BalanceFragment extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {

    SliderLayout coinSlider;
    TextView textCountMoney;
    TextView bottomText;
    CountDownTimer countDownTimer;
    BluetoothSPP bt;
    double countMoney = 5.00;
    int tokenCountMoney = 5;
    boolean startTicker = false;

    public BalanceFragment() {
        super();
    }

    public static BalanceFragment newInstance(double money) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putDouble("countMoney",money);
        fragment.setArguments(args);
        return fragment;
    }

    public void setBluetooth(BluetoothSPP setBluetooth){
        this.bt = setBluetooth;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Read from Arguments
        if(savedInstanceState != null)
            countMoney = savedInstanceState.getDouble("countMoney");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.balance_fragment, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        textCountMoney = (TextView) rootView.findViewById(R.id.TextCountMoneyFragment);
        bottomText = (TextView) rootView.findViewById(R.id.BottomTextFragment) ;
        coinSlider = (SliderLayout) rootView.findViewById(R.id.SliderFragment);

        countDownTimer = new CountDownTimer(25000,5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(tokenCountMoney < 5) tokenCountMoney++;
            }

            @Override
            public void onFinish() {
                startTicker = false;
            }
        };

        initSlider();
    }

    private void initSlider() {

        HashMap<String,Integer> file_map = new HashMap<String,Integer>();
        file_map.put("10.00",R.drawable.coin10);
        file_map.put("5.00",R.drawable.coin5);
        file_map.put("1.00",R.drawable.coin1);


        for(String name : file_map.keySet()){
            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
            defaultSliderView
                    .image(file_map.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle()
                    .putString("extra",name);

            coinSlider.addSlider(defaultSliderView);
        }

        coinSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        coinSlider.addOnPageChangeListener(this);
        coinSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        coinSlider.stopAutoCycle();
        coinSlider.setCurrentPosition(1);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        BaseSliderView current = coinSlider.getCurrentSlider();
        String s = "Click to send THB " + current.getBundle().getString("extra") + " to Piggy";
        bottomText.setText(s);

        //Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        if(tokenCountMoney > 0 && startTicker == false ){
            countMoney = countMoney + Double.parseDouble(slider.getBundle().getString("extra"));
            String s = Double.toString(countMoney);
            textCountMoney.setText(s);
            bt.send(s,true);
            //tokenCountMoney--;
        }else{
            Toast.makeText(getContext(), "You can't add Money", Toast.LENGTH_SHORT).show();
            if(startTicker == false){
                countDownTimer.start();
                startTicker = true;
            }
        }
    }
}
