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
const Player: FC<{ children?: React.ReactNode }> = (props) => {
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
      <div className={styles.otherPlayers}>
        {gameState.otherPlayers.map((player, idx) => (
          // TODO: lookup players?
          <OtherPlayer
            key={idx}
            user={new User(player.id, player.id)}
            handCardCount={player.cardsInHand}
          />
        ))}
      </div>
      <p>It&apos;s {gameState.currentPlayer}&apos;s turn</p>
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
