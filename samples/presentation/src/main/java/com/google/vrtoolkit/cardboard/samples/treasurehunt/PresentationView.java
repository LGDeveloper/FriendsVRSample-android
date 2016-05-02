package com.google.vrtoolkit.cardboard.samples.treasurehunt;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.display.DisplayManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class PresentationView {
    private final String TITLE = "LGVR_3D_overlay";      // for setting the Title of Presentation

    private Context mContext;
    private View mPresentationLayout;
    private Presentation mPresentation;
    private Display mCurrentDisplay;

    private AlphaAnimation textFadeAnimation;

    public PresentationView(Context context) {
        if (context == null)
            return;
        mContext = context;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initWindow();
            }
        });
    }

    // Create and Initialize the views
    private void initWindow() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPresentationLayout = inflater.inflate(R.layout.presentation_layout, null, false);                // Set the presentation layout
        DisplayManager displayManager = (DisplayManager) mContext.getSystemService(Context.DISPLAY_SERVICE);
        Display[] currDisplays = displayManager.getDisplays(null);
        mCurrentDisplay = currDisplays.length >= 2 ? currDisplays[1] : currDisplays[0];     // currDisplays[0] : Phone for Test, currDisplays[1] : VR

        if (mCurrentDisplay != null) {
            mPresentation = new Presentation(mContext, mCurrentDisplay);
            mPresentation.setCancelable(true);
            mPresentation.setCanceledOnTouchOutside(true);
            if (mPresentation.getWindow() == null) {
                destroy();
                return;
            }

            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.setTitle(TITLE);             // Must set the Title for LG VR
            params.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            mPresentation.getWindow().setAttributes(params);
            mPresentation.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPresentation.setContentView(mPresentationLayout);

            textFadeAnimation = new AlphaAnimation(1.0f, 0.0f);
        }
    }

    public void show3DToast(final String message, final boolean isTimeout, final int duration) {
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ((mCurrentDisplay == null) || (mPresentation == null))
                    return;
                if (!mCurrentDisplay.isValid())
                    return;

                hide3DToast();

                final TextView textView = (TextView) mPresentationLayout.findViewById(R.id.toast_text);
                textView.setAlpha(1f);
                textView.setText(message);
                mPresentation.show();
                if (isTimeout) {
                    if (textFadeAnimation == null)
                        textFadeAnimation = new AlphaAnimation(1.0f, 0.0f);
                    textFadeAnimation.setDuration(duration);
                    textFadeAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            hide3DToast();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    textView.startAnimation(textFadeAnimation);
                }
            }
        });
    }

    public void hide3DToast() {
        if (mContext == null)
            return;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPresentation != null && mPresentation.isShowing()) {
                    mPresentation.hide();
                }
            }
        });
    }

    public void destroy() {
        if (mContext == null)
            return;
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPresentation != null) {
                    mPresentation.dismiss();
                    mPresentation = null;
                }
                if (mCurrentDisplay != null)
                    mCurrentDisplay = null;
            }
        });
    }

    public boolean isShowing() {
        if (mPresentation != null)
            return mPresentation.isShowing();
        return false;
    }
}
