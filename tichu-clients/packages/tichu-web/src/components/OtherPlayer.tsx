import React, { FC } from "react";
import { PlayerAvatar } from "./PlayerAvatar";
import { User } from "../model/User";
import { Paper } from "@material-ui/core";
import { CardBacks } from "./CardSet";

/**
 * Represents another player at the table - hidden cards, etc.
 */
interface OtherPlayerProps {
  user: User;
  handCardCount: number;
}

export const OtherPlayer: FC<OtherPlayerProps> = (props: OtherPlayerProps) => {
  return (
    <Paper elevation={3}>
      <PlayerAvatar user={props.user} />
      <CardBacks count={props.handCardCount} style="fanned" />
      has {props.handCardCount} card in their hand
    </Paper>
  );
};
