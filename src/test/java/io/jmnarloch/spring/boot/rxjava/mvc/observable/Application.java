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
package io.jmnarloch.spring.boot.rxjava.mvc.observable;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;

@Configuration
@EnableAutoConfiguration
@RestController
class Application {

    @RequestMapping(method = RequestMethod.GET, value = "/single")
    public Observable<String> single() {
        return Observable.just("single value");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/multiple")
    public Observable<String> multiple() {
        return Observable.just("multiple", "values");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/throw")
    public Observable<Object> error() {
        return Observable.error(new RuntimeException("Unexpected"));
    }
}
