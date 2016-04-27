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

import io.jmnarloch.spring.boot.rxjava.async.SingleDeferredResult;
import io.jmnarloch.spring.boot.rxjava.dto.EventDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

/**
 * Tests the {@link SingleDeferredResult} class.
 *
 * @author Jakub Narloch
 * @author Robert Danci
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class SingleDeferredResultTest {

    @Value("${local.server.port}")
    private int port = 0;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldRetrieveSingleValue() {
        ResponseEntity<String> response = restTemplate.getForEntity(path("/single"), String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("single value", response.getBody());
    }

    @Test
    public void shouldRetrieveSingleValueWithStatusCode() {
        ResponseEntity<String> response = restTemplate.getForEntity(path("/singleWithResponse"), String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("single value", response.getBody());
    }

    @Test
    public void shouldRetrieveJsonSerializedPojoValue() {
        ResponseEntity<EventDto> response = restTemplate.getForEntity(path("/event"), EventDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Spring.io", response.getBody().getName());
    }

    @Test
    public void shouldRetrieveErrorResponse() {
        ResponseEntity<Object> response = restTemplate.getForEntity(path("/throw"), Object.class);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void shouldTimeOut() {
        ResponseEntity<String> response = restTemplate.getForEntity(path("/timeoutWithBody"), String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Timeout", response.getBody());
    }

    @Test
    public void shouldTimeOutWithoutBody() {
        ResponseEntity<String> response = restTemplate.getForEntity(path("/timeoutWithoutBody"), String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    private String path(String context) {
        return String.format("http://localhost:%d%s", port, context);
    }
}