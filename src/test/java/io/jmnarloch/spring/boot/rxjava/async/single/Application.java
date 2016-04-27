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
package io.jmnarloch.spring.boot.rxjava.async.single;

import io.jmnarloch.spring.boot.rxjava.async.BaseConfiguration;
import io.jmnarloch.spring.boot.rxjava.async.SingleDeferredResult;
import io.jmnarloch.spring.boot.rxjava.dto.EventDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rx.Single;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Configuration
@RestController
@Import(BaseConfiguration.class)
public class Application {

    @RequestMapping(method = RequestMethod.GET, value = "/single")
    public SingleDeferredResult<String> single() {
        return new SingleDeferredResult<>(Single.just("single value"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/singleWithResponse")
    public SingleDeferredResult<ResponseEntity<String>> singleWithResponse() {
        return new SingleDeferredResult<>(
                Single.just(new ResponseEntity<>("single value", HttpStatus.NOT_FOUND)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/event", produces = APPLICATION_JSON_UTF8_VALUE)
    public SingleDeferredResult<EventDto> event() {
        return new SingleDeferredResult<>(Single.just(new EventDto("Spring.io", new Date())));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/throw")
    public SingleDeferredResult<Object> error() {
        return new SingleDeferredResult<>(Single.error(new RuntimeException("Unexpected")));
    }

    @RequestMapping("/timeoutWithBody")
    public SingleDeferredResult<String> timeoutWithBody() {
        return new SingleDeferredResult<>(Single.just("hello").delay(3, SECONDS), TimeUnit.SECONDS.toMillis(1), "Timeout");
    }

    @RequestMapping("/timeoutWithoutBody")
    public SingleDeferredResult<String> timeoutWithoutBody() {
        return new SingleDeferredResult<>(Single.just("hello").delay(3, SECONDS), TimeUnit.SECONDS.toMillis(1));
    }


}
