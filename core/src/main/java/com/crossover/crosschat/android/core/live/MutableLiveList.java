package com.crossover.crosschat.android.core.live;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 7/3/18.
 */
public class MutableLiveList<T> extends LiveData<List<T>> {
    private List<T> backingList = new ArrayList<>();

    public MutableLiveList() {
        setValue(backingList);
    }

    public void postItem(T item) {
        backingList.add(item);
        postValue(backingList);
    }

    public void postItem(int position, T item) {
        backingList.add(position, item);
        postValue(backingList);
    }

    public void postAll(Collection<? extends T> items) {
        backingList.addAll(items);
        postValue(backingList);
    }

    public void postAll(int position, Collection<? extends T> items) {
        backingList.addAll(position, items);
        postValue(backingList);
    }

    public void clear() {
        backingList.clear();
        postValue(backingList);
    }

}
