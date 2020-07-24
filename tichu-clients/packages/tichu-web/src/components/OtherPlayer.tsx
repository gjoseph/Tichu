import React, { FC } from "react";
import { PlayerAvatar } from "./PlayerAvatar";
import { User } from "../model/User";

/**
 * Represents another player at the table - hidden cards, etc.
 */
interface OtherPlayerProps {
  user: User;
  handCardCount: number;
}

export const OtherPlayer: FC<OtherPlayerProps> = (props: OtherPlayerProps) => {
  return (
    <div>
      <PlayerAvatar user={props.user} />
      <span>{props.user.displayName}</span>
    </div>
  );
};
