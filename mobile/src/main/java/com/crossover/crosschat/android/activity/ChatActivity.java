package com.crossover.crosschat.android.activity;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.crossover.crosschat.android.R;
import com.crossover.crosschat.android.adapter.MessagesRecyclerAdapter;
import com.crossover.crosschat.android.core.model.ChatMessageItem;
import com.crossover.crosschat.android.core.ui.chat.ChatPresenter;
import com.crossover.crosschat.android.core.ui.chat.ChatView;
import com.crossover.crosschat.android.utils.InputUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import icepick.State;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/10/18.
 */
public class ChatActivity extends BasicActivity implements ChatView {

    public static final String TAG = ChatActivity.class.getSimpleName();

    public static final String EXTRA_USERNAME = "EXTRA_USERNAME";
    public static final String EXTRA_FOCUS_MESSAGE_EDIT = "EXTRA_FOCUS_MESSAGE_EDIT";
    public static final String SAVED_INSTANCE_CHAT_MESSAGES = "SAVED_INSTANCE_CHAT_MESSAGES";

    private static final long UI_UPDATE_PERIOD = 60 * 1000;

    @InjectPresenter
    ChatPresenter presenter;

    @BindView(R.id.recycler)
    protected RecyclerView recyclerView;

    @BindView(R.id.edit_message)
    protected EditText messageEditText;

    @State
    protected boolean focusMessageEdit;

    private MessagesRecyclerAdapter messagesRecyclerAdapter;

    private LinearLayoutManager linearLayoutManager;

    private Handler updateHandler;

    private Runnable updateRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getBaseContext());
        linearLayoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);

        messagesRecyclerAdapter = new MessagesRecyclerAdapter(this, null);
        messagesRecyclerAdapter.setHasStableIds(true);

        recyclerView.setAdapter(messagesRecyclerAdapter);

        if (focusMessageEdit) {
            focusMessageEdit = false;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    messageEditText.requestFocus();
                    InputUtils.showKeyboard(ChatActivity.this, messageEditText);
                }
            }, 500);
        }

        // Delay a little, Simulating a popping up Welcome Message
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.onWelcomeMessageRequested();
            }
        }, 1000);

        updateHandler = new Handler();
        updateRunnable = new UpdateLayoutRunnable();

        initObservers();

        // Ensure we have clear chat room on screen opening
        presenter.getChatMessageItems().clear();
    }

    @Override
    protected int getContentResource() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onLaunch() {
        super.onLaunch();

        if (!getIntent().hasExtra(EXTRA_USERNAME))
            finish();

        focusMessageEdit = getIntent().getBooleanExtra(EXTRA_FOCUS_MESSAGE_EDIT, false);

        presenter.getUsername().postValue(getIntent().getStringExtra(EXTRA_USERNAME));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Start the initial UI runnable
        updateHandler.post(updateRunnable);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_INSTANCE_CHAT_MESSAGES,
            new ArrayList<>(presenter.getChatMessageItems().getValue()));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        List<ChatMessageItem> chatMessageItems =
            savedInstanceState.getParcelableArrayList(SAVED_INSTANCE_CHAT_MESSAGES);
        presenter.onRestoreInstanceStateRequested(chatMessageItems);
    }

    @Override
    protected void onPause() {
        super.onPause();

        updateHandler.removeCallbacks(updateRunnable);
    }

    private void initObservers() {
        presenter.getChatMessageItems().observe(this, new Observer<List<ChatMessageItem>>() {
            @Override
            public void onChanged(@Nullable List<ChatMessageItem> chatMessageItems) {
                messagesRecyclerAdapter.setItemsList(chatMessageItems);
            }
        });
    }

    @Override
    public void refreshLayout() {
        if (linearLayoutManager == null || messagesRecyclerAdapter == null) return;

        int startPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int endPosition = linearLayoutManager.findLastVisibleItemPosition();

        messagesRecyclerAdapter.notifyItemRangeChanged(startPosition,
                endPosition - startPosition + 1);
    }

    @OnClick(R.id.btn_submit)
    protected void onSubmitCommentClick() {
        if (messageEditText.getText().toString().length() == 0
                || messageEditText.getText().toString().trim().length() == 0) {
            messageEditText.requestFocus();
            return;
        }

        presenter.onChatMessageSubmitted(messageEditText.getText().toString());

        messageEditText.setText("");
        messageEditText.clearFocus();
        InputUtils.hideKeyboard(this);
    }

    @Override
    public void onBackPressed() {
        if (messageEditText.hasFocus()) {
            messageEditText.clearFocus();
            InputUtils.hideKeyboard(this);
            return;
        }

        super.onBackPressed();
    }

    private class UpdateLayoutRunnable implements Runnable {

        @Override
        public void run() {
            presenter.onLayoutRefreshRequested();

            // Repeat this same runnable code block again after UI_UPDATE_PERIOD
            updateHandler.postDelayed(updateRunnable, UI_UPDATE_PERIOD);
        }

    }

}
