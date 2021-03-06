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
import rx.Single;

/**
 * A specialized {@link DeferredResult} that handles {@link Single} return type.
 *
 * @author Jakub Narloch
 * @author Robert Danci
 *
 * @see DeferredResult
 */
public class SingleDeferredResult<T> extends DeferredResultObservable<T> {

    public SingleDeferredResult(Single<T> single) {
        super(single.toObservable());
    }

    public SingleDeferredResult(Single<T> single, long timeout) {
        super(single.toObservable(), timeout);
    }

    public SingleDeferredResult(Single<T> single, Long timeout, Object timeoutResult) {
        super(single.toObservable(), timeout, timeoutResult);
    }
}
