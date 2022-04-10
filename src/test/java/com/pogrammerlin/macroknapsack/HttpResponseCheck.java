package com.pogrammerlin.macroknapsack;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
public class HttpResponseCheck {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void checkDefaultHelloWorldEndpoint() {
        HttpEntity<String> request = new HttpEntity<>(getBasicAuthHeaders());
        ResponseEntity<String> response = testRestTemplate.exchange("http://localhost:"+port+"/default/helloWorld?say=testString", HttpMethod.GET, request, String.class);
//                testRestTemplate.getForEntity("http://localhost:"+port+"/default/helloWorld?say=testString", String.class);
        assertEquals("Hello Word! My name is Test Name, and to you I say testString", response.getBody());
    }

    private HttpHeaders getBasicAuthHeaders() {
        String basicCreds = "testAdmin:testAdmin";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic "+new String(Base64.encodeBase64(basicCreds.getBytes())));
        return headers;
    }
}
