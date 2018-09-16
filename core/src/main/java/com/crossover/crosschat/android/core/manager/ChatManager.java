package com.crossover.crosschat.android.core.manager;

import com.crossover.crosschat.android.core.model.ContentEntity;
import com.crossover.crosschat.android.core.model.MessageItem;
import com.crossover.crosschat.android.core.util.ContentAnalyser;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;
import io.reactivex.functions.Predicate;

/**
 * Created by Mahmoud Abdurrahman (mahmoud.abdurrahman@crossover.com) on 2/16/18.
 */
public class ChatManager {

    private static ChatManager instance;

    private ChatManager() {
    }

    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }

        return instance;
    }

    public Observable<MessageItem> analyseChatMessage(final String messageText) {
        return ContentAnalyser.getInstance().rxExtractEntitiesWithIndices(messageText)
                .flatMap(new Function<List<ContentEntity>, Observable<MessageItem>>() {
                    @Override
                    public Observable<MessageItem> apply(List<ContentEntity> contentEntities) {
                        Observable<ContentEntity> entitiesObservable =
                                Observable.fromIterable(contentEntities);

                        Observable<List<String>> mentionsObservable = entitiesObservable
                                .filter(new Predicate<ContentEntity>() {
                                    @Override
                                    public boolean test(ContentEntity contentEntity) {
                                        return contentEntity.getType()
                                                .equals(ContentEntity.Type.MENTION);
                                    }
                                })
                                .map(new Function<ContentEntity, String>() {
                                    @Override
                                    public String apply(ContentEntity contentEntity) {
                                        return contentEntity.getValue();
                                    }
                                })
                                .toList()
                                .toObservable();

                        Observable<List<String>> emoticonsObservable = entitiesObservable
                                .filter(new Predicate<ContentEntity>() {
                                    @Override
                                    public boolean test(ContentEntity contentEntity) {
                                        return contentEntity.getType()
                                                .equals(ContentEntity.Type.EMOTICON);
                                    }
                                })
                                .map(new Function<ContentEntity, String>() {
                                    @Override
                                    public String apply(ContentEntity contentEntity) {
                                        return contentEntity.getValue();
                                    }
                                })
                                .toList()
                                .toObservable();

                      Observable<List<String>> hashObservable = entitiesObservable
                          .filter(new Predicate<ContentEntity>() {
                            @Override
                            public boolean test(ContentEntity contentEntity) {
                              return contentEntity.getType()
                                  .equals(ContentEntity.Type.HASH);
                            }
                          })
                          .map(new Function<ContentEntity, String>() {
                            @Override
                            public String apply(ContentEntity contentEntity) {
                              return contentEntity.getValue();
                            }
                          })
                          .toList()
                          .toObservable();


                      Observable<List<String>> linksObservable = entitiesObservable
                                .filter(new Predicate<ContentEntity>() {
                                    @Override
                                    public boolean test(ContentEntity contentEntity) {
                                        return contentEntity.getType()
                                                .equals(ContentEntity.Type.URL);
                                    }
                                })
                                .map(new Function<ContentEntity, String>() {
                                    @Override
                                    public String apply(ContentEntity contentEntity) {
                                        return contentEntity.getValue();
                                    }
                                })
                                .toList()
                                .toObservable();

                        // compose these together
                        return Observable.zip(
                                mentionsObservable,
                                emoticonsObservable,
                                linksObservable,
                                hashObservable,
                                Observable.just(contentEntities),
                                new Function5<List<String>, List<String>, List<String>, List<String>, List<ContentEntity>,  MessageItem>() {
                                    @Override
                                    public MessageItem apply(List<String> mentions, List<String> emoticons,
                                        List<String> linkItems, List<String> hashItems, List<ContentEntity> contentEntities) {
                                        MessageItem messageItem = new MessageItem();

                                        messageItem.setRawMessage(messageText);
                                        messageItem.setMentions(mentions);
                                        messageItem.setEmoticons(emoticons);
                                        messageItem.setLinks(linkItems);
                                        messageItem.setHashtags(hashItems);
                                        messageItem.setAllEntities(contentEntities);

                                        return messageItem;
                                    }
                                });
                    }
                });
    }
}
