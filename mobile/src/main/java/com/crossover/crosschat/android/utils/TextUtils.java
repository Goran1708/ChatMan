package com.crossover.crosschat.android.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.crossover.crosschat.android.R;
import com.crossover.crosschat.android.core.model.ContentEntity;
import com.crossover.crosschat.android.core.model.MessageItem;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 7/3/18.
 */
public class TextUtils {

    public static Spanned getFormattedMessageItem(Context context, MessageItem messageItem) {
        SpannableString formattedMessage = new SpannableString(messageItem.getRawMessage());
        for (ContentEntity entity : messageItem.getAllEntities()) {
            formattedMessage.setSpan(new StyleSpan(Typeface.BOLD), entity.getStart(),
                    entity.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            switch (entity.getType()) {
                case MENTION:
                    formattedMessage.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.mention_span_color)
                    ), entity.getStart(), entity.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case EMOTICON:
                    formattedMessage.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.emoticon_span_color)
                    ), entity.getStart(), entity.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                case URL:
                    formattedMessage.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(context, R.color.url_span_color)
                    ), entity.getStart(), entity.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return formattedMessage;
    }
}
