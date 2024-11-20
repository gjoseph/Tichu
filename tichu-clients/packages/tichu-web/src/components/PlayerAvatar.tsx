import { User } from "../model/User";
import { Avatar, Badge, Theme } from "@mui/material";
import createStyles from "@mui/styles/createStyles";
import React, { FC } from "react";
import styles from "./PlayerAvatar.module.css";
import makeStyles from "@mui/styles/makeStyles";
import { classes, stringToHslColor } from "../util";

export const PlayerAvatar: FC<{
  user: User;
  presence?: "online" | "offline";
  busy?: boolean;
}> = ({ user, presence, busy = false }) => {
  const cssClass = classes(
    styles.avatarBadge,
    presence ? styles[presence] : null,
    busy ? styles.pulse : null
  );
  const colour = user.displayName ? stringToHslColor(user.displayName) : "#ccc";
  // Learn how to parameterise this so we don't need to recompute everytime
  // .. or just ditch both and learn how to hash a name into a set of 20 colour class names
  // ... and the contrastText function
  const colourStyles = makeStyles((theme: Theme) =>
    createStyles({
      coloured: {
        color: theme.palette.getContrastText(colour),
        backgroundColor: colour,
      },
    })
  )();
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
        <Avatar src={user.avatarUrl} className={colourStyles.coloured}>
          {user.initials()}
        </Avatar>
      </Badge>
    </div>
  );
};
