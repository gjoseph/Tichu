import React, { FC } from "react";
import { Card, CardSuit, NormalCard } from "tichu-client-ts-lib";
import { mapEnumValue } from "ts-enum-util";
import styles from "./Card.module.css";
import { classes } from "../util";

type CardSize = "small" | "regular";

interface CardViewProps {
  card: Card;
  size?: CardSize;
  handleSelect: (isSelected: boolean, card: Card) => void;
}

/**
 * I don't know how to prevent children elements - does it matter?
 */
export const CardView: FC<CardViewProps> = ({
  card,
  handleSelect,
  size = "regular",
}: CardViewProps) => {
  const [isSelected, setIsSelected] = React.useState(false);

  let color: string;
  if (card.type === "normal") {
    color = mapEnumValue((card as NormalCard).suit).with({
      [CardSuit.Jade]: "#060",
      [CardSuit.Sword]: "#000",
      [CardSuit.Pagoda]: "#359",
      [CardSuit.Star]: "#922",
    });
  } else {
    color = "#999";
  }

  const onClick = () => {
    const newValue = !isSelected;
    setIsSelected(newValue);
    handleSelect(newValue, card);
  };
  const cssClass = classes(
    isSelected ? styles.selected : "",
    styles.card,
    styles.front,
    styles[size]
  );
  return (
    <div className={cssClass} onClick={onClick}>
      <div className={styles.cardShortName} style={{ color: color }}>
        {card.shortName}
      </div>
      <div className={styles.cardName}>{card.name}</div>
    </div>
  );
};
