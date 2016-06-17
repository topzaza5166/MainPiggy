package com.example.topza.piggy;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;
import com.inthecheesefactory.thecheeselibrary.view.state.BundleSavedState;

import org.w3c.dom.Text;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class CustomViewGroupTemplate extends BaseCustomViewGroup {
    ImageView customListItemImage;
    TextView customListTextMoney;
    TextView customListTextTime;
    TextView customListTextApp;

    public CustomViewGroupTemplate(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public CustomViewGroupTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public CustomViewGroupTemplate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public CustomViewGroupTemplate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.list_item, this);
    }

    private void initInstances() {
        // findViewById here
        customListItemImage = (ImageView) findViewById(R.id.CustomListItemImage);
        customListTextMoney = (TextView) findViewById(R.id.CustomListTextMoney);
        customListTextApp = (TextView) findViewById(R.id.CustomListTextApp);
        customListTextTime = (TextView) findViewById(R.id.CustomListTextTime);
    }


    public void setCustomListItemImage(int ImageResource) {
        customListItemImage.setImageResource(ImageResource);
    }

    public void setCustomListTextMoney(String TextMoney) {
        customListTextMoney.setText(TextMoney);
    }

    public void setCustomListTextApp(String TextApp) {
        customListTextApp.setText(TextApp);
    }

    public void setCustomListTextTime(String TextTime) {
        customListTextTime.setText(TextTime);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
