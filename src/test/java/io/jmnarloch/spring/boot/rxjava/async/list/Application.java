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
package io.jmnarloch.spring.boot.rxjava.async.list;

import io.jmnarloch.spring.boot.rxjava.async.BaseConfiguration;
import io.jmnarloch.spring.boot.rxjava.async.ListDeferredResult;
import io.jmnarloch.spring.boot.rxjava.dto.EventDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.functions.Func1;

import java.util.Date;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Configuration
@RestController
@Import(BaseConfiguration.class)
public class Application {

    @RequestMapping(method = RequestMethod.GET, value = "/single")
    public ListDeferredResult<String> single() {
        return new ListDeferredResult<>(Observable.just("single value"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/multiple")
    public ListDeferredResult<String> multiple() {
        return new ListDeferredResult<>(Observable.just("multiple", "values"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/event", produces = APPLICATION_JSON_UTF8_VALUE)
    public ListDeferredResult<EventDto> event() {
        return new ListDeferredResult<>(
                Observable.just(
                        new EventDto("Spring.io", new Date()),
                        new EventDto("JavaOne", new Date())
                )
        );
    }

    @RequestMapping(method = RequestMethod.GET, value = "/throw")
    public ListDeferredResult<Object> error() {
        return new ListDeferredResult<>(Observable.error(new RuntimeException("Unexpected")));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/timeoutWithBody")
    public ListDeferredResult<String> timeout() {
        return new ListDeferredResult<>(Observable.timer(3, SECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return "single value";
            }
        }), SECONDS.toMillis(1), "Timeout");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/timeoutWithoutBody")
    public ListDeferredResult<String> timeoutNoBody() {
        return new ListDeferredResult<>(Observable.timer(3, SECONDS).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return "single value";
            }
        }), SECONDS.toMillis(1));
    }
}
