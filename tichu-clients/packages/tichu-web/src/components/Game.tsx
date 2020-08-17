import React, { FC } from "react";
import styles from "./Game.module.css";
import { Hand } from "./Hand";
import {
  Card,
  OutgoingGameMessage,
  PlayerPlaysParam,
  SendFunction,
} from "tichu-client-ts-lib";
import { GameState } from "../model/GameState";
import { User } from "../model/User";
import { OtherPlayer } from "./OtherPlayer";

/**
 * Represents the player at the table
 */
const Player: FC = (props) => {
  return (
    <div className={styles.player}>
      This is me
      {props.children}
    </div>
  );
};

/**
 * Placeholder for preference setting component
 */
const Prefs: FC = () => {
  return <div />;
};

/**
 * Placeholder to display game statistics, score, ...
 * @constructor
 */
const GameStats: FC = () => {
  return <div />;
};

export const Game: FC<{ sendMessage: SendFunction; gameState: GameState }> = ({
  sendMessage,
  // TODO this is a stupid prop
  gameState,
}) => {
  const sendCards = (cards: Card[]) => {
    const msg = new OutgoingGameMessage(PlayerPlaysParam.fromCards(cards));
    sendMessage(msg);
  };

  return (
    <div className={styles.game}>
      <OtherPlayer user={new User("1", "Isa")} handCardCount={3} />
      <OtherPlayer user={new User("2", "Mikayla")} handCardCount={10} />
      <OtherPlayer user={new User("3", "Shane")} handCardCount={14} />
      <Player>
        {gameState.hand ? (
          <Hand sendCards={sendCards} cardsInHand={gameState.hand} />
        ) : (
          <>dealing or empty hand?</>
        )}
        <Prefs />
      </Player>
      <GameStats />
    </div>
  );
};
