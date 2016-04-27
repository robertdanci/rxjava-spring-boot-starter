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

import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static org.springframework.util.Assert.notNull;

/**
 * A subscriber that sets the single value produced by the {@link Observable} on the {@link DeferredResult}.
 *
 * @author Jakub Narloch
 * @author Robert Danci
 * @see DeferredResult
 */
class DeferredResultObservable<T> extends DeferredResult<T> implements Runnable, Observer<T> {

    private final Subscription subscription;

    public DeferredResultObservable(Observable<T> observable) {
        this(observable, null);
    }

    public DeferredResultObservable(Observable<T> observable, Long timeout) {
        this(observable, timeout, null);
    }

    public DeferredResultObservable(Observable<T> observable, Long timeout, Object timeoutResult) {
        super(timeout, timeoutResult);
        notNull(observable, "Observable cannot be null");
        this.subscription = observable.subscribe(this);
        this.onTimeout(this);
    }

    @Override
    public void onNext(T value) {
        setResult(value);
    }

    @Override
    public void onError(Throwable e) {
        setErrorResult(e);
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void run() {
        subscription.unsubscribe();
    }
}
