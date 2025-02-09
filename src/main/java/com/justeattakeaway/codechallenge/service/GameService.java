package com.justeattakeaway.codechallenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GameService {
  private static final String TOPIC = "game-moves";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Value("${game.mode}")
  private String gameMode;

  private final AtomicInteger lastReceivedNumber = new AtomicInteger(-1);

  public void startGame() {
    int number = new Random().nextInt(100) + 2; // Generate random number >= 2
    System.out.println("Starting game with number: " + number);
    sendMove(number);
  }

  public void sendMove(int number) {
    kafkaTemplate.send(TOPIC, String.valueOf(number));
  }

  @KafkaListener(topics = TOPIC, groupId = "game-group")
  public void receiveMove(String message) {
    int number = Integer.parseInt(message);
    System.out.println("Received number: " + number);

    if (number == 1) {
      System.out.println("Game Over!");
      return;
    }

    lastReceivedNumber.set(number); // Store for manual mode

    if ("automatic".equalsIgnoreCase(gameMode)) {
      processMove(number);
    } else {
      System.out.println("Waiting for manual input. Call /game/move?adjustment={-1,0,1}.");
    }
  }

  private void processMove(int number) {
    int adjustment = getAdjustment(number);
    int newNumber = (number + adjustment) / 3;
    System.out.println("Adjusting: " + adjustment + ", New Number: " + newNumber);
    sendMove(newNumber);
  }

  private int getAdjustment(int number) {
    if (number % 3 == 0) return 0;
    return (number % 3 == 1) ? -1 : 1;
  }

  public boolean hasPendingMove() {
    return lastReceivedNumber.get() > 1;
  }

  public void makeManualMove(int adjustment) {
    int number = lastReceivedNumber.get();
    if (number <= 1) {
      System.out.println("No move available, game over.");
      return;
    }

    int newNumber = (number + adjustment) / 3;
    System.out.println("Manual move: Adjusting " + adjustment + ", New Number: " + newNumber);
    sendMove(newNumber);
  }
}
