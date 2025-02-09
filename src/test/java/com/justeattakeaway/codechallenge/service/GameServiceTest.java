package com.justeattakeaway.codechallenge.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @InjectMocks
  private GameService gameService;

  @BeforeEach
  void setUp() {
    gameService = new GameService(kafkaTemplate);
  }

  @Test
  void testStartGame() {
    gameService.startGame();
    verify(kafkaTemplate, atLeastOnce()).send(eq("game-moves"), anyString());
  }

  @Test
  void testSendMove() {
    gameService.sendMove(15);
    verify(kafkaTemplate).send("game-moves", "15");
  }

  @Test
  void testReceiveMove_AutomaticMode() {
    gameService.receiveMove("15");
  }

  @Test
  void testReceiveMove_ManualMode() {
    gameService.receiveMove("16");
  }

  @Test
  void testProcessMove() {
    gameService.receiveMove("10");
  }

  @Test
  void testMakeManualMove() {
    gameService.sendMove(10);
    gameService.makeManualMove(1);
    verify(kafkaTemplate, atLeastOnce()).send(eq("game-moves"), anyString());
  }
}
