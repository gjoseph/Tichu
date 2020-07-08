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

/**
 * Represents another player at the table - hidden cards, etc.
 */
const OtherPlayer: FC = () => {
  return <div className={styles.otherPlayer}>this is another player</div>;
};

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

const Prefs: FC = () => {
  return <div />;
};

const GameStats: FC = () => {
  return <div />;
};

export const Game: FC<{ sendMessage: SendFunction; gameState: GameState }> = ({
  sendMessage,
  // this is a stupid prop
  gameState,
}) => {
  const sendCards = (cards: Card[]) => {
    const msg = new OutgoingGameMessage(PlayerPlaysParam.fromCards(cards));
    sendMessage(msg);
  };

  return (
    <div className={styles.game}>
      <OtherPlayer />
      <OtherPlayer />
      <OtherPlayer />
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
