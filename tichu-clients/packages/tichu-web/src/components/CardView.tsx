import React, { FC } from "react";
import { Card, CardSuit, SpecialCards } from "tichu-client-ts-lib";
import { mapEnumValue } from "ts-enum-util";
import { classes } from "../util";
import styles from "./Card.module.css";

export type OnCardClick = (card: Card) => void;
export type CardSize = "small" | "regular";

// no children elements in CardView
interface CardViewProps {
  card: Card;
  selected: boolean; // TODO wonder if selected is anything than a css class that could be set by parent
  size?: CardSize;
  onClick: OnCardClick;
}

const colorFor = (card: Card) => {
  if (card.type === "normal") {
    return mapEnumValue(card.suit).with({
      [CardSuit.Jade]: "#060",
      [CardSuit.Sword]: "#000",
      [CardSuit.Pagoda]: "#359",
      [CardSuit.Star]: "#922",
    });
  } else {
    return "#999";
  }
};

const symbolFor = (card: Card): string => {
  if (card.type === "normal") {
    if (card.number > 10) {
      return ["J", "Q", "K", "A"][card.number - 11]; // wooh untested magic
    } else {
      return card.number.toString();
    }
  } else {
    return mapEnumValue(card.special).with({
      [SpecialCards.MahJong]: "1",
      [SpecialCards.Dog]: "🐶",
      [SpecialCards.Phoenix]: "🦚",
      [SpecialCards.Dragon]: "🐉",
    });
  }
};

export const CardView: FC<CardViewProps> = ({
  card,
  selected,
  onClick,
  size = "regular",
}: CardViewProps) => {
  const color = colorFor(card);
  const symbol = symbolFor(card);

  const handleClick = () => onClick(card);
  const cssClass = classes(
    selected ? styles.selected : "",
    styles.card,
    styles.front,
    styles[size],
  );
  return (
    <div className={cssClass} onClick={handleClick}>
      <div className={styles.cardShortName} style={{ color: color }}>
        {symbol}
      </div>
      <div className={styles.cardName}>{card.name}</div>
    </div>
  );
};
