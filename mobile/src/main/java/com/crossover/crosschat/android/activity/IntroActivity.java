package com.crossover.crosschat.android.activity;

import android.content.Intent;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.crossover.crosschat.android.R;
import com.crossover.crosschat.android.core.ui.intro.IntroPresenter;
import com.crossover.crosschat.android.core.ui.intro.IntroView;
import com.crossover.crosschat.android.utils.ValidationUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class IntroActivity extends BasicActivity implements IntroView {

    @InjectPresenter
    IntroPresenter presenter;

    @BindView(R.id.edit_name)
    protected EditText nameEditText;

    @Override
    protected int getContentResource() {
        return R.layout.activity_intro;
    }

    private boolean validateInput() {
        if (!ValidationUtils.isValidString(nameEditText, getString(R.string.error_name_required))) {
            nameEditText.requestFocus();
            return false;
        }
        return true;
    }

    @OnEditorAction(R.id.edit_name)
    protected boolean onEditorAction(int actionId) {
        onStartBtn();

        return true;
    }

    @OnClick(R.id.btn_start)
    protected void onStartBtn() {
        if (validateInput()) {
            presenter.onStartButtonClicked(nameEditText.getText().toString());
        }
    }

    @Override
    public void showNextScreen(String name) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_USERNAME, name);
        intent.putExtra(ChatActivity.EXTRA_FOCUS_MESSAGE_EDIT, true);
        startActivity(intent);
    }

}
