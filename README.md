# scoober-code-challenge-boilerplate

See [here](./example.md)

# Resources
- [Example](./example.md) contains instructions about the repository content and how to use it
- [LICENSE](./LICENSE) contains a reference copy of the Apache 2.0 license that applies all Just Eat Takeaway.com projects.
- [CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md) describes the Code of Conduct that applies to all contributors to our projects.


# Game of Three - Coding Challenge Solution

## Overview
The "Game of Three" is a two-player game where players exchange numbers following specific rules. Each player runs as an independent service and communicates via Kafka. The game supports both **automatic** and **manual** modes, allowing flexible interaction.

## Solution Architecture
- **Spring Boot** for backend implementation.
- **Kafka** for player communication.
- **Docker Compose** for service orchestration.
- **REST API** for manual game interaction.
- **Configurable input mode** (automatic/manual).

## How the Game Works
1. A player starts the game by generating a random number and sending it to the second player.
2. The receiving player adjusts the number by adding {-1, 0, 1} to make it divisible by 3.
3. The number is then divided by 3 (rule of the game) and sent back.
4. The process repeats until the number reaches **1**, ending the game.

## Implemented
### Player Communication via Kafka
- Kafka is used to ensure **asynchronous** and **loosely coupled** player interaction.
- If a player is **offline**, it can receive the message once it comes online.

### Configurable Game Mode
- Players can operate in **automatic** or **manual** mode.
- In **automatic** mode, the player makes moves without user input.
- In **manual** mode, the player waits for an API call to submit a move.

### REST API for Manual Mode
- `GET /game/start` → Starts the game.
- `GET /game/move?adjustment={-1,0,1}` → Manual move submission.

### Scalable & Platform Independent
- The solution is containerized with **Docker Compose**, making it easy to deploy across different environments.

## Running the Game
### Step-1 Start Kafka and Game Services
Run the following command to start Kafka and both players:
```sh
docker-compose up --build
```

### Step-2 Build the project using Maven
```sh
mvn clean install
```

### Step-3 Run the application
```sh
mvn spring-boot:run
```

### Step-4 Start the Game
Player 1 starts the game manually:
```sh
curl "http://localhost:8080/game/start"
```

### Step-5 Manual Player Moves (If Enabled)
If a player is in **manual** mode, make a move with:
```sh
curl "http://localhost:8080/game/move?adjustment=-1"
```
(Change `-1` to `0` or `1` as needed.)

## Future Improvements
Since I had **limited time**, I could not fully implement **Domain-Driven Design (DDD)** principles and an **Anemic Domain Model** fix. Below are the concepts we would apply if time permitted.

### Domain-Driven Design (DDD) Improvements
#### Current Issue:
- My implementation is more service-oriented, with business logic spread across service classes.
- DDD promotes an **explicit domain model** with entities and aggregates.

#### DDD Solution:
- Introduce **Game**, **Player** and **Move** entities with clear domain logic.
- Implement **Services** as separate business logic from infrastructure concerns.

### Fixing the Anemic Domain Model
#### Current Issue:
- Business logic is handled in my service class instead of domain objects.
- This leads to a procedural approach rather than an object-oriented design. ( Usually I'm not doing so :) )

#### Proposed Solution:
- Move game logic into the **Game** entity instead of handling it in the **GameService**.
- Introduce a **Move** (or Step for example) class encapsulating adjustments and validation.

## Conclusion
This solution successfully implements a **distributed, event-driven** "Game of Three" using Kafka and Spring Boot. 
I also designed the system with **configurable input modes** to support both **automatic and manual gameplay**.

While I didn't have time to fully apply **DDD** and fix the **anemic domain model**, here I provided an approach that can be followed in future iterations.

