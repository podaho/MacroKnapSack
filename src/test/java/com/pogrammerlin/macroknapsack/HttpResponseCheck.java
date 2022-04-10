package com.pogrammerlin.macroknapsack;

import com.pogrammerlin.macroknapsack.client.FatSecret.dto.FatSecretSearchResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
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
        ResponseEntity<String> response = testRestTemplate.getForEntity("http://localhost:"+port+"/default/helloWorld?say=testString", String.class);
        assertEquals("Hello Word! My name is Test Name, and to you I say testString", response.getBody());
    }
}
