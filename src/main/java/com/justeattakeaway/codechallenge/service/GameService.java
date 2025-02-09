package com.justeattakeaway.codechallenge.service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameService {

  private static final String TOPIC = "game-moves";
  private static final String GROUP_ID = "game-group";
  private static final String AUTOMATIC_MODE = "automatic";
  private final AtomicInteger lastReceivedNumber = new AtomicInteger(-1);
  private KafkaTemplate<String, String> kafkaTemplate;

  @Value("${game.mode}")
  private String gameMode;

  public GameService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void startGame() {
    int number = new Random().nextInt(100) + 2; // just for starting from 2
    System.out.println("Starting game with number: " + number);
    sendMove(number);
  }

  public void sendMove(int number) {
    kafkaTemplate.send(TOPIC, String.valueOf(number));
  }

  @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
  public void receiveMove(String message) {
    int number = Integer.parseInt(message);
    System.out.println("Received number: " + number);

    if (number == 1) {
      System.out.println("Game Over!");
      return;
    }

    lastReceivedNumber.set(number);

    if (AUTOMATIC_MODE.equalsIgnoreCase(gameMode)){
      processMove(number);
    } else{
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
    if (number % 3 == 0) {
      return 0;
    }
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
