import React from "react";
import { Game } from "../components/Game";
import { actions } from "@storybook/addon-actions";
import { cardFromName } from "tichu-client-ts-lib";
import { GameState } from "../model/GameState";

export default {
  title: "Game",
  component: Game,
};
const events = actions("sendMessage");

export const sample_Game = () => {
  const hand = [cardFromName("*P"), cardFromName("GJ"), cardFromName("K6")];
  return <Game gameState={new GameState(hand)} {...events} />;
};
