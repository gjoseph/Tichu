import React from "react";
import { Game } from "../components/Game";
import { actions } from "@storybook/addon-actions";
import { Card, cardFromName } from "tichu-client-ts-lib";
import { GameState } from "../model/GameState";

export default {
  title: "Game",
  component: Game,
};
const events = actions("sendMessage");

export const sample_Game = () => {
  const hand = [cardFromName("*P"), cardFromName("GJ"), cardFromName("K6")];
  return (
    <Game
      gameState={
        new GameState("abc", hand, [
          { id: "abc", cardsInHand: 3, cardsCollected: 10 },
          { id: "def", cardsInHand: 10, cardsCollected: 0 },
          { id: "ghi", cardsInHand: 1, cardsCollected: 20 },
        ])
      }
      {...events}
    />
  );
};

export const simulate_room = () => {
  return <SimuRoom />;
};

const SimuRoom: React.FC = () => {
  const init = new GameState(
    "abc",
    [],
    [
      { id: "abc", cardsInHand: 3, cardsCollected: 10 },
      { id: "def", cardsInHand: 10, cardsCollected: 0 },
      { id: "ghi", cardsInHand: 1, cardsCollected: 20 },
    ]
  );
  const [gameState, setGameState] = React.useState(init);
  const handleClick = () =>
    setGameState(
      (oldState) =>
        new GameState(
          oldState.currentPlayer,
          [cardFromName("*P"), cardFromName("GJ"), cardFromName("K6")],
          []
        )
    );
  return (
    <>
      <Game gameState={gameState} {...events} />
      <button onClick={handleClick}>SIMULATE RECEIVE HAND</button>
    </>
  );
};
