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
package io.jmnarloch.spring.boot.rxjava.async.sse;

import io.jmnarloch.spring.boot.rxjava.async.BaseConfiguration;
import io.jmnarloch.spring.boot.rxjava.async.ObservableSseEmitter;
import io.jmnarloch.spring.boot.rxjava.dto.EventDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

import java.util.Date;
import java.util.GregorianCalendar;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@Configuration
@RestController
@Import(BaseConfiguration.class)
public class Application {

    @RequestMapping(method = RequestMethod.GET, value = "/sse")
    public ObservableSseEmitter<String> single() {
        return new ObservableSseEmitter<>(Observable.just("single value"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/timeout")
    public ObservableSseEmitter<String> timeout() {
        return new ObservableSseEmitter<>(Observable.just("single value").delay(2, SECONDS), SECONDS.toMillis(1));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/errors")
    public ObservableSseEmitter<String> errors() {
        return new ObservableSseEmitter<>(Observable.<String>error(new RuntimeException("bad, bad")));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/partial")
    public ObservableSseEmitter<String> partial() {
        return new ObservableSseEmitter<>(Observable.merge(
                Observable.just("partial"),
                Observable.<String>error(new IllegalArgumentException("bad, bad"))));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/messages")
    public ObservableSseEmitter<String> messages() {
        return new ObservableSseEmitter<>(Observable.just("message 1", "message 2", "message 3"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/events")
    public ObservableSseEmitter<EventDto> event() {
        return new ObservableSseEmitter<>(
                Observable.merge(
                        Observable.just(new EventDto("Spring.io", getDate(2016, 5, 11))),
                        Observable.just(new EventDto("JavaOne", getDate(2016, 9, 22)))
                                .delay(2, SECONDS)
                )
                , APPLICATION_JSON_UTF8);
    }

    private static Date getDate(int year, int month, int day) {
        return new GregorianCalendar(year, month, day).getTime();
    }
}
