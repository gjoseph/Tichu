import { User } from "../model/User";
import { Avatar, Badge } from "@material-ui/core";
import React, { FC } from "react";
import styles from "./PlayerAvatar.module.css";

export const PlayerAvatar: FC<{
  user: User;
  presence?: "online" | "offline";
  busy?: boolean;
}> = ({ user, presence, busy = false }) => {
  const cssClass = [
    styles.avatarBadge,
    presence ? styles[presence] : null,
    busy ? styles.pulse : null,
  ].join(" ");
  return (
    <div className={cssClass}>
      <Badge
        overlap="circle"
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "right",
        }}
        variant="dot"
      >
        <Avatar src={user.avatarUrl}>{user.initials()}</Avatar>
      </Badge>
    </div>
  );
};
