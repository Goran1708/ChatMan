package com.crossover.crosschat.android.core.ui.chat;

import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.crossover.crosschat.android.core.CoreApplication;
import com.crossover.crosschat.android.core.R;
import com.crossover.crosschat.android.core.live.MutableLiveList;
import com.crossover.crosschat.android.core.manager.ChatManager;
import com.crossover.crosschat.android.core.model.ChatMessageItem;
import com.crossover.crosschat.android.core.model.MessageItem;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 1/18/17.
 */
@InjectViewState
public class ChatPresenter extends MvpPresenter<ChatView> implements ChatActions {

    public static final String TAG = ChatPresenter.class.getSimpleName();

    private final MutableLiveList<ChatMessageItem> chatMessageItems = new MutableLiveList<>();
    private final MutableLiveData<String> username = new MutableLiveData<>();

    private final CompositeDisposable chatManagerDisposables = new CompositeDisposable();

    public ChatPresenter() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Dispose all pending requests
        chatManagerDisposables.dispose();
    }

    @Override
    public void onWelcomeMessageRequested() {
        if (chatMessageItems.getValue() != null && !chatMessageItems.getValue().isEmpty()) return;

        ChatMessageItem welcomeChatMessage = new ChatMessageItem();
        welcomeChatMessage.setSelf(false);
        welcomeChatMessage.setDate(new Date());
        welcomeChatMessage.setRawMessage(
                CoreApplication.getInstance().getString(R.string.label_welcome_message,
                        username.getValue())
        );
        welcomeChatMessage.setHtml(true);

        addChatMessage(welcomeChatMessage);
    }

    @Override
    public void onRestoreInstanceStateRequested(List<ChatMessageItem> chatMessageItems) {
        this.chatMessageItems.postAll(chatMessageItems);
    }

    @Override
    public void onLayoutRefreshRequested() {
        getViewState().refreshLayout();
    }

    @Override
    public void onChatMessageSubmitted(String messageText) {
        addSelfMessage(messageText);
        analyseChatMessage(messageText);
    }

    private void addSelfMessage(String messageText) {
        ChatMessageItem selfMessage = new ChatMessageItem();
        selfMessage.setMessageItem(null);
        selfMessage.setSelf(true);
        selfMessage.setDate(new Date());
        selfMessage.setRawMessage(messageText);

        addChatMessage(selfMessage);
    }

    private void addReplyMessage(MessageItem reply) {
        ChatMessageItem replyMessage = new ChatMessageItem();
        replyMessage.setMessageItem(reply);
        replyMessage.setSelf(false);
        replyMessage.setDate(new Date());

        if (reply == null) {
            replyMessage.setRawMessage(CoreApplication.getInstance().getString(R.string.msg_failed_processing_message));
        } else {
            replyMessage.setRawMessage(reply.toString());
        }

        addChatMessage(replyMessage);
        addReplyFormattedMessage(reply);
    }

    private void addFailureReplyMessage() {
        addReplyMessage(null);
    }

    private void addReplyFormattedMessage(MessageItem reply) {
        if (reply == null || reply.getAllEntities().isEmpty()) return;

        ChatMessageItem replyMessage = new ChatMessageItem();
        replyMessage.setMessageItem(reply);
        replyMessage.setSelf(false);
        replyMessage.setDate(new Date());
        replyMessage.setFormatted(true);

        addChatMessage(replyMessage);
    }

    private void addChatMessage(ChatMessageItem chatMessage) {
        if (chatMessage == null) return;

        chatMessageItems.postItem(0, chatMessage);
    }

    private void analyseChatMessage(String messageText) {
        chatManagerDisposables.add(
                ChatManager.getInstance().analyseChatMessage(messageText)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new AnalyseMessageOnNextConsumer(),
                                new AnalyseMessageOnErrorConsumer())
        );
    }

    public MutableLiveList<ChatMessageItem> getChatMessageItems() {
        return chatMessageItems;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    private class AnalyseMessageOnNextConsumer implements Consumer<MessageItem> {
        @Override
        public void accept(MessageItem messageItem) {
            addReplyMessage(messageItem);
        }
    }

    private class AnalyseMessageOnErrorConsumer implements Consumer<Throwable> {
        @Override
        public void accept(Throwable throwable) {
            addFailureReplyMessage();

            Log.e(TAG, throwable.getMessage(), throwable);
        }
    }
}
