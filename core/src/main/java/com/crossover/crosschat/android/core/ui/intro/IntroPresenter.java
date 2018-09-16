package com.crossover.crosschat.android.core.ui.intro;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com.com) on 1/18/18.
 */
@InjectViewState
public class IntroPresenter extends MvpPresenter<IntroView> implements IntroActions {

    public IntroPresenter() {
    }

    @Override
    public void onStartButtonClicked(String name) {
        getViewState().showNextScreen(name);
    }
}
