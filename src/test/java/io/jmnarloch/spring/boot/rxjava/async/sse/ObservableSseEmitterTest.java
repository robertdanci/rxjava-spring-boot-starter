/**
 * Copyright (c) 2015-2016 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jmnarloch.spring.boot.rxjava.async.sse;

import io.jmnarloch.spring.boot.rxjava.async.ObservableSseEmitter;
import io.jmnarloch.spring.boot.rxjava.dto.EventDto;
import org.glassfish.jersey.media.sse.EventInput;
import org.glassfish.jersey.media.sse.InboundEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link ObservableSseEmitter} class.
 *
 * @author Jakub Narloch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
@DirtiesContext
public class ObservableSseEmitterTest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private Client client;

    @Test
    public void shouldRetrieveSse() {
        List<String> response = readEvents("/sse", String.class);

        assertEquals(1, response.size());
        assertEquals("single value", response.get(0));
    }

    @Test
    public void shouldRetrieveSseWithMultipleMessages() {
        List<String> results = readEvents("/messages", String.class);

        assertEquals(3, results.size());
        assertEquals(results.get(0), "message 1");
        assertEquals(results.get(1), "message 2");
        assertEquals(results.get(2), "message 3");
    }

    @Test
    public void shouldRetrieveJsonOverSseWithMultipleMessages() {
        List<EventDto> results = readEvents("/events", EventDto.class);
        assertEquals(2, results.size());
    }

    @Test
    public void shouldReturnEmptyOnTimeout() {
        List<String> result = readEvents("/timeout", String.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnEmptyOnError() {
        List<String> result = readEvents("/errors", String.class);
        assertTrue(result.isEmpty());
    }

    @Test
    public void shouldReturnPartialResponse() {
        List<String> result = readEvents("/partial", String.class);
        assertEquals(1, result.size());
        assertEquals("partial", result.get(0));
    }

    private <T> List<T> readEvents(String context, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        WebTarget webTarget = client.target(path(context));
        EventInput eventInput = webTarget.request().get(EventInput.class);
        while (!eventInput.isClosed()) {
            final InboundEvent inboundEvent = eventInput.read();
            if (inboundEvent == null) {
                break;
            }
            result.add(inboundEvent.readData(clazz));
        }
        return result;
    }

    private String path(String context) {
        return String.format("http://localhost:%d%s", port, context);
    }

}