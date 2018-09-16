package com.crossover.crosschat.android.core.ui.chat;

import com.crossover.crosschat.android.core.model.ChatMessageItem;

import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 7/4/18.
 */
public interface ChatActions {

    void onWelcomeMessageRequested();

    void onLayoutRefreshRequested();

    void onChatMessageSubmitted(String messageText);

    void onRestoreInstanceStateRequested(List<ChatMessageItem> chatMessageItems);
}
