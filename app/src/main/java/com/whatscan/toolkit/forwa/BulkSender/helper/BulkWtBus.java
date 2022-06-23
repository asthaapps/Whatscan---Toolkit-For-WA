package com.whatscan.toolkit.forwa.BulkSender.helper;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public final class BulkWtBus {
    @NotNull
    private final PublishSubject<Object> publishSubject;

    public BulkWtBus() {
        PublishSubject<Object> create = PublishSubject.create();
        publishSubject = create;
    }

    @NotNull
    public final PublishSubject<Object> getBus() {
        return publishSubject;
    }

    public final void send(@NotNull Object obj) {
        publishSubject.onNext(obj);
    }

    @NotNull
    public final Observable<Object> toObserve() {
        return publishSubject;
    }
}