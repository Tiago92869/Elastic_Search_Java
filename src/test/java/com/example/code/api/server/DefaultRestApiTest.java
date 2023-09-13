package com.example.code.api.server;

import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DefaultRestApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testWelcome() {
        webTestClient.get().uri("/welcome").exchange().expectStatus().is2xxSuccessful().expectBody(String.class)
                .value(IsEqualIgnoringCase.equalToIgnoringCase("welcome to spring boot"));
    }

    @Test
    void testTime(){
        webTestClient.get().uri("/time").exchange().expectBody(String.class).value(v -> isCorrectTime(v));
    }

    private Object isCorrectTime(String v){
        var responseLocalTime = LocalTime.parse(v);
        var now = LocalTime.now();
        var nowMinus30Seconds = now.minusSeconds(30);

        assertTrue(responseLocalTime.isAfter(nowMinus30Seconds) && responseLocalTime.isBefore(now));

        return responseLocalTime;
    }
}
