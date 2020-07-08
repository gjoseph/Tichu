import React, { FC } from "react";
import styles from "./Table.module.css";
import { Hand } from "./Hand";

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

interface TableProps {}

export const Table: FC<TableProps> = (props: TableProps) => {
  return (
    <div className={styles.table}>
      <OtherPlayer />
      <OtherPlayer />
      <OtherPlayer />
      <Player>
        <Hand />
        <Prefs />
      </Player>
      <GameStats />
    </div>
  );
};
