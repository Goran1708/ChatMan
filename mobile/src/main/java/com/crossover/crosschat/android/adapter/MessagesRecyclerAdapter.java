package com.crossover.crosschat.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crossover.crosschat.android.R;
import com.crossover.crosschat.android.core.model.ChatMessageItem;
import com.crossover.crosschat.android.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/10/18.
 */
public class MessagesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_SELF = 100;
    private final int VIEW_TYPE_REPLY = 200;

    private Context context;
    private OnItemClickListener onItemClickListener;

    private List<ChatMessageItem> itemsList = new ArrayList<>();

    public MessagesRecyclerAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<ChatMessageItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<ChatMessageItem> items) {
        if (items == null)
            return;

        this.itemsList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemsList.get(position).isSelf()? VIEW_TYPE_SELF : VIEW_TYPE_REPLY;
    }

    @Override
    public long getItemId(int position) {
        return itemsList.get(position).getDate().getTime();
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutRes = viewType == VIEW_TYPE_SELF? R.layout.list_item_chat_message_right : R.layout.list_item_chat_message_left;
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(layoutRes, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.update(position, itemsList.get(position));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.container_content)
        LinearLayout contentContainer;

        @BindView(R.id.container_message)
        LinearLayout messageContainer;

        @BindView(R.id.label_date)
        TextView date;

        @BindView(R.id.label_message)
        TextView message;

        int position;
        ChatMessageItem item;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void update(int position, ChatMessageItem item) {
            this.position = position;
            this.item = item;

            if (!item.isFormatted()) {
                message.setText(item.getRawMessage());
            } else {
                if (!item.isHtml()) {
                    message.setText(TextUtils.getFormattedMessageItem(context, item.getMessageItem()));
                } else {
                    message.setText(Html.fromHtml(item.getRawMessage()));
                }
            }
            date.setText(DateUtils.getRelativeTimeSpanString(item.getDate().getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL));
        }

        @Override
        public void onClick(View v) {
            if (item == null || onItemClickListener == null) {
                return;
            }

            onItemClickListener.onItemClick(position, item);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, ChatMessageItem item);
    }

}
