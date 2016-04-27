/**
 * Copyright (c) 2015-2016 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jmnarloch.spring.boot.rxjava.async;

import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

import java.io.IOException;

import static org.springframework.util.Assert.notNull;

/**
 * A specialized {@link SseEmitter} that handles {@link Observable} types. The emitter subscribes to the
 * passed {@link Observable} instance and emits every produced value through {@link #send(Object, MediaType)}.
 *
 * @author Jakub Narloch
 * @author Robert Danci
 *
 * @see SseEmitter
 */
public class ObservableSseEmitter<T> extends SseEmitter implements Observer<T>, Runnable {

    private final MediaType mediaType;
    private final Subscription subscription;

    public ObservableSseEmitter(Observable<T> observable) {
        this(observable, null);
    }

    public ObservableSseEmitter(Observable<T> observable, long timeout) {
        this(observable, null, timeout);
    }

    public ObservableSseEmitter(Observable<T> observable, MediaType mediaType) {
        this(observable, mediaType, null);
    }

    public ObservableSseEmitter(Observable<T> observable, MediaType mediaType, Long timeout) {
        super(timeout);
        notNull(observable, "Observable cannot be null");
        this.subscription = observable.subscribe(this);
        this.mediaType = mediaType;
        this.onTimeout(this);
    }

    @Override
    public void onNext(T value) {
        try {
            send(value, mediaType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void onError(Throwable e) {
        completeWithError(e);
    }

    @Override
    public void onCompleted() {
        complete();
    }

    @Override
    public void run() {
        subscription.unsubscribe();
        onCompleted();
    }
}
