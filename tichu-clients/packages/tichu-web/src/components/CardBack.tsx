import type { FC } from "react";
import { classes } from "../util";
import styles from "./Card.module.css";

type CardSize = "small" | "regular";
export const CardBack: FC<{ size: CardSize }> = ({ size }) => {
  return <div className={classes(styles.card, styles.back, styles[size])} />;
};
