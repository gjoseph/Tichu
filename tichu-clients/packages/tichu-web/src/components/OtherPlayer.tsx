import React, { FC } from "react";
import { PlayerAvatar } from "./PlayerAvatar";
import { User } from "../model/User";
import { Paper } from "@mui/material";
import { CardBacks } from "./CardSet";
import styles from "./Game.module.css";

/**
 * Represents another player at the table - hidden cards, etc.
 */
interface OtherPlayerProps {
  user: User;
  handCardCount: number;
}

export const OtherPlayer: FC<OtherPlayerProps> = (props: OtherPlayerProps) => {
  return (
    <Paper elevation={3} classes={{ root: styles.otherPlayer }}>
      <PlayerAvatar user={props.user} />
      <CardBacks count={props.handCardCount} layout="fanned" />
      has {props.handCardCount} card in their hand
    </Paper>
  );
};
