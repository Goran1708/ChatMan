package com.crossover.crosschat.android.core.ui.intro;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 7/4/18.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface IntroView extends MvpView {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNextScreen(String name);
}
