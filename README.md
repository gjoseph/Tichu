# Tichu

This is an online implementation of [Tichu](https://www.fatamorgana.ch/tichu/tichu.asp),
based on the card game by [Fata Morgana Spiele](https://www.fatamorgana.ch/fatamorgana/default.asp), Switzerland.

For information about the original game, see

- [Fata Morgana's website](https://www.fatamorgana.ch/fatamorgana/default.asp)
- [BGG entry about Tichu](https://boardgamegeek.com/boardgame/215/tichu)

## Code [![Main Build](https://github.com/gjoseph/Tichu/actions/workflows/main.yml/badge.svg)](https://github.com/gjoseph/Tichu/actions/workflows/main.yml?query=branch%3Amaster)

### Build

```
./mvnw clean verify
```

### Run

Currently I just run `net.incongru.tichu.tomcat.WebSocketServer` from IntelliJ, so... TODO

### Notes

This project currently consists of different modules:

- `model`: models the game and rules,
- `actions`: wraps the above in an action framework, which can be used by:
  - `simulator`: a module that provides a text file-based simulation of games, and serves as a test harness for the model
  - `websocket-endpoint`: offers a websocket entrypoint to actions
- `embedded-tomcat`: currently used as a quick way of spinning up the websocket service locally
- `archunit`: [ArchUnit](https://www.archunit.org/) tests to try and keep the codebase under control
- `clients`:
  - `term-client`: play from a terminal! Currently used as a test bed for the websocket implementation
  - `web`: will probably be a React-based bundle

## Next up & future things

Rather than maintain a laundry list of all excellent ideas I have for the future, they're on a Trello board: https://trello.com/b/ehVw0CHp/tichu
