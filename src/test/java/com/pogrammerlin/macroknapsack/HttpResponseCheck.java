package com.pogrammerlin.macroknapsack;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpResponseCheck {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void checkDefaultHelloWorldEndpoint() {
        ResponseEntity<String> response = testRestTemplate.getForEntity("http://localhost:"+port+"/default/helloWorld?say=testString", String.class);
        assertEquals("Hello Word! My name is Test Name, and to you I say testString", response.getBody());
    }
}
