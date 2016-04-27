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

import java.util.List;

/**
 * A specialized {@link DeferredResult} that handles {@link Observable} type.
 *
 * @author Jakub Narloch
 * @author Robert Danci
 * @see DeferredResult
 */
public class ListDeferredResult<T> extends DeferredResultObservable<List<T>> {

    public ListDeferredResult(Observable<T> observable) {
        super(observable.toList());
    }

    public ListDeferredResult(Observable<T> observable, long timeout) {
        super(observable.toList(), timeout);
    }

    public ListDeferredResult(Observable<T> observable, Long timeout, Object timeoutResult) {
        super(observable.toList(), timeout, timeoutResult);
    }
}
