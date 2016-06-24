package com.example.topza.piggy;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class BalanceFragment extends Fragment {

    HistoryTable history;
    DBHelper dbHelper;
    TextView textCountMoney;
    TextView bottomText;
    CountDownTimer countDownTimer;
    ImageView coin1;
    ImageView coin5;
    ImageView coin10;
    TextView textCurrency;
    TextView textAnimation;
    Button clearButton;
    ImageView iconArrowUp;
    ViewPager coinViewPager;

    double countMoney = 0.00;
    int tokenCountMoney = 5;
    boolean startTicker = false;
    int coinSelected[] = {0, 0, 0};
    GestureDetector gestureDetector;
    String coinTextSelected[] = {"", "", ""};

    View.OnClickListener ClearCount = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            countMoney = 0.00;
            ((HomeActivity) getActivity()).sendBluetoothText(Double.toString(countMoney));
        }
    };

    public BalanceFragment() {
        super();
    }

    public static BalanceFragment newInstance(Bundle sliderSet) {
        BalanceFragment fragment = new BalanceFragment();
        fragment.setArguments(sliderSet);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Read from Arguments from function getArguments().
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
        textCurrency = (TextView) rootView.findViewById(R.id.TextCurrency);
        textAnimation = (TextView) rootView.findViewById(R.id.TextAnimation);
        bottomText = (TextView) rootView.findViewById(R.id.BottomTextFragment);
        clearButton = (Button) rootView.findViewById(R.id.ClearButton);
        clearButton.setOnClickListener(ClearCount);

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

        iconArrowUp = (ImageView) rootView.findViewById(R.id.ic_arrow_up);

        coinSelected[0] = R.drawable.coin1;
        coinSelected[1] = R.drawable.coin5;
        coinSelected[2] = R.drawable.coin10;

        coinTextSelected[0] = "1.0";
        coinTextSelected[1] = "5.0";
        coinTextSelected[2] = "10.0";

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                //Toast.makeText(getContext(), "Press me", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //Toast.makeText(getContext(), "Long Press me", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getY() > e2.getY()) {
                    FlingUpFunction();
                    return true;
                }
                //Toast.makeText(getContext(), "e1.getY = " + e1.getY() + "e2.getY = " + e2.getY(), Toast.LENGTH_SHORT).show();
                else return false;
            }
        });

        coinViewPager = (ViewPager) rootView.findViewById(R.id.CoinViewPager);
        coinViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView coin = new ImageView(container.getContext());
                coin.setImageResource(coinSelected[position]);
                coin.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });
                container.addView(coin);
                return coin;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        coinViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String i = coinTextSelected[position];
                textCountMoney.setText(i);

                String s = "Slide up to send THB " + i + " to Piggy";
                bottomText.setText(s);

                //Toast.makeText(getContext(), "This position is" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void FlingUpFunction() {
        String addMoney = coinTextSelected[coinViewPager.getCurrentItem()];
        countMoney = countMoney + Double.parseDouble(addMoney);

        String s = Double.toString(countMoney);
        Calendar rightNow = Calendar.getInstance();

        history = new HistoryTable();
        history.setHistoryApp("Piggy");
        history.setHistoryMoney(addMoney);
        history.setHistoryTime(rightNow.get(Calendar.HOUR_OF_DAY) + ":" + rightNow.get(Calendar.MINUTE));
        history.setHistoryIO("Output");

        dbHelper.addHistory(history);

        ((HomeActivity) getActivity()).sendBluetoothText(s);

        textAnimation.setText("THB " + addMoney + " to Piggy");
        Animation textFadeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        textAnimation.startAnimation(textFadeAnimation);

        if (addMoney == "5.0") {
            coinAnimation(coin5);
        } else coinAnimation(coin1);

        ((HomeActivity) getActivity()).setTextCredit(countMoney);

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

    private void coinAnimation(final ImageView coin) {
        final MediaPlayer coin_sound = MediaPlayer.create(getContext(), R.raw.coin_drop_sound);
        Animation coinMoveAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_animation);
        coin_sound.setVolume(80, 80);
        coinMoveAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                setAnimationVisibility("VISIBLE");
                textAnimation.setVisibility(View.INVISIBLE);
                coin.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                setAnimationVisibility("INVISIBLE");
                textAnimation.setVisibility(View.VISIBLE);
                coin_sound.start();
            }
        });
        coin.setVisibility(View.VISIBLE);
        coin.startAnimation(coinMoveAnimation);
    }

    private void setAnimationVisibility(String status) {
        int setting;

        switch (status) {
            case "INVISIBLE":
                setting = View.INVISIBLE;
                break;
            case "VISIBLE":
                setting = View.VISIBLE;
                break;
            default:
                setting = View.INVISIBLE;
                break;
        }

        bottomText.setVisibility(setting);
        coinViewPager.setVisibility(setting);
        textCurrency.setVisibility(setting);
        textCountMoney.setVisibility(setting);
        iconArrowUp.setVisibility(setting);
    }

    public double getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(double cm) {
        countMoney = cm;
    }

}