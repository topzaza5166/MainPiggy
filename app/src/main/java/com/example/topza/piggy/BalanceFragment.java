package com.example.topza.piggy;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.security.PublicKey;
import java.util.Calendar;
import java.util.HashMap;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class BalanceFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    HistoryTable history;
    DBHelper dbHelper;
    SliderLayout coinSlider;
    TextView textCountMoney;
    TextView bottomText;
    CountDownTimer countDownTimer;
    ImageView coin1;
    ImageView coin5;
    ImageView coin10;

    double countMoney = 0.00;
    int tokenCountMoney = 5;
    boolean startTicker = false;

    public BalanceFragment() {
        super();
    }

//    public void setBluetooth(BluetoothSPP bluetooth) {
//        this.bt = bluetooth;
//    }

    public static BalanceFragment newInstance(double money) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putDouble("countMoney", money);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Read from Arguments
        if (savedInstanceState != null)
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
        bottomText = (TextView) rootView.findViewById(R.id.BottomTextFragment);
        coinSlider = (SliderLayout) rootView.findViewById(R.id.SliderFragment);

        countDownTimer = new CountDownTimer(25000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (tokenCountMoney < 5) tokenCountMoney++;
            }

            @Override
            public void onFinish() {
                startTicker = false;
            }
        };

        dbHelper = new DBHelper(getContext());

        coin1 = (ImageView) rootView.findViewById(R.id.coin1Animation);
        coin5 = (ImageView) rootView.findViewById(R.id.coin5Animation);
        coin10 = (ImageView) rootView.findViewById(R.id.coin10Animation);

        initSlider();
    }

    private void initSlider() {

        HashMap<String, Integer> file_map = new HashMap<String, Integer>();
        file_map.put("10.00", R.drawable.coin10);
        file_map.put("5.00", R.drawable.coin5);
        file_map.put("1.00", R.drawable.coin1);


        for (String name : file_map.keySet()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(getContext());
            defaultSliderView
                    .image(file_map.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            defaultSliderView.bundle(new Bundle());
            defaultSliderView.getBundle()
                    .putString("extra", name);

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
        outState.putDouble("CountMoney", countMoney);
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
            countMoney = savedInstanceState.getDouble("CountMoney");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        BaseSliderView current = coinSlider.getCurrentSlider();

        String i = current.getBundle().getString("extra");
        textCountMoney.setText(i);

        String s = "Click to send THB " + i + " to Piggy";
        bottomText.setText(s);

        //Toast.makeText(getContext(), position + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        String addMoney = slider.getBundle().getString("extra");
        countMoney = countMoney + Double.parseDouble(addMoney);

        String s = Double.toString(countMoney);
        Calendar rightNow = Calendar.getInstance();

        history = new HistoryTable();
        history.setHistoryApp("Piggy");
        history.setHistoryMoney(addMoney);
        history.setHistoryTime(rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE));
        history.setHistoryIO("Output");

        dbHelper.addHistory(history);

        ((HomeActivity)getActivity()).sendBluetoothText(s);

        if (addMoney == "5.00")
            coinAnimation(coin5);

        if (addMoney == "1.00")

            coinAnimation(coin1);
        if (addMoney == "10.00")
            coinAnimation(coin10);

        Toast.makeText(getContext(), "Add " + addMoney + " To Piggy Your Money are " + countMoney, Toast.LENGTH_SHORT).show();


    }

    private void coinAnimation(final ImageView coin){
        coinSlider.setVisibility(View.INVISIBLE);
        final MediaPlayer coin_sound = MediaPlayer.create(getContext(), R.raw.coin_drop_sound);
        Animation coinMoveAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_animation);
        coin_sound.setVolume(80, 80);
        coinMoveAnimation.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationEnd(Animation animation) {
                coinSlider.setVisibility(View.VISIBLE);
                coin.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {
                coin_sound.start();
            }
        });
        coin.setVisibility(View.VISIBLE);
        coin.startAnimation(coinMoveAnimation);
    }

    public double getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(double cm) {
        countMoney = cm;
    }

}

