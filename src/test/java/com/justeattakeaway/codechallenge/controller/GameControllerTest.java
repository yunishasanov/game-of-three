package com.justeattakeaway.codechallenge.controller;

import com.justeattakeaway.codechallenge.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

  @Mock
  private GameService gameService;

  @InjectMocks
  private GameController gameController;


  @Test
  void testStartGame() {
    String response = gameController.startGame();
    verify(gameService, times(1)).startGame();
    assertEquals("Game started!", response);
  }

  @Test
  void testManualMove_NoMoveAvailable() {
    when(gameService.hasPendingMove()).thenReturn(false);
    String response = gameController.manualMove(1);
    assertEquals("No move available. Please wait for the next turn.", response);
  }

  @Test
  void testManualMove_InvalidAdjustment() {
    when(gameService.hasPendingMove()).thenReturn(true);
    String response = gameController.manualMove(5);
    assertEquals("Invalid move. Choose between -1, 0, or 1.", response);
  }

  @Test
  void testManualMove_ValidMove() {
    when(gameService.hasPendingMove()).thenReturn(true);
    String response = gameController.manualMove(1);
    verify(gameService, times(1)).makeManualMove(1);
    assertEquals("Move submitted!", response);
  }
}