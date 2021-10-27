import React, { FC } from "react";
import styles from "./Card.module.css";
import { classes } from "../util";

type CardSize = "small" | "regular";
export const CardBack: FC<{ size: CardSize }> = ({ size }) => {
  return <div className={classes(styles.card, styles.back, styles[size])} />;
};
