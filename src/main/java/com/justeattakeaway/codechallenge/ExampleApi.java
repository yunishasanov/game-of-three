package com.justeattakeaway.codechallenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class ExampleApi {

    @Autowired
    ExampleKafka exampleKafka;

    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @GetMapping("kafka")
    public ResponseEntity<String> testKafka() {
        exampleKafka.produceMessage();
        return new ResponseEntity<>("Kafka test done", HttpStatus.OK);
    }

}
