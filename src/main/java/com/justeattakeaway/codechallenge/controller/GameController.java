package com.justeattakeaway.codechallenge.controller;

import com.justeattakeaway.codechallenge.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
class GameController {

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  private GameService gameService;

  @GetMapping("/start")
  public String startGame() {
    gameService.startGame();
    return "Game started!";
  }

  @GetMapping("/move")
  public String manualMove(@RequestParam int adjustment) {
    if (!gameService.hasPendingMove()) {
      return "No move available. Please wait for the next turn.";
    }

    if (adjustment < -1 || adjustment > 1) {
      return "Invalid move. Choose between -1, 0, or 1.";
    }

    gameService.makeManualMove(adjustment);
    return "Move submitted!";
  }
}
