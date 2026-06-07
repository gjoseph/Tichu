import { User } from "../model/User";
import { Avatar, Badge } from "@mui/material";
import React, { FC } from "react";
import styles from "./PlayerAvatar.module.css";
import { classes, stringToHslColor } from "../util";

export const PlayerAvatar: FC<{
  user: User;
  presence?: "online" | "offline";
  busy?: boolean;
}> = ({ user, presence, busy = false }) => {
  const cssClass = classes(
    styles.avatarBadge,
    presence ? styles[presence] : null,
    busy ? styles.pulse : null,
  );
  const colour = user.displayName ? stringToHslColor(user.displayName) : "#ccc";
  // TODO Learn how to parameterise this so we don't need to recompute everytime
  // .. or just ditch both and learn how to hash a name into a set of 20 colour class names
  // ... and the contrastText function
  return (
    <div className={cssClass}>
      <Badge
        overlap="circular"
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "right",
        }}
        variant="dot"
      >
        <Avatar
          src={user.avatarUrl}
          sx={{
            bgcolor: colour,
            color: (theme) => theme.palette.getContrastText(colour),
          }}
        >
          {user.initials()}
        </Avatar>
      </Badge>
    </div>
  );
};
