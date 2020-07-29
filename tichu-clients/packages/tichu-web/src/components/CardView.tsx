import React, { FC } from "react";
import {
  Card,
  CardSuit,
  NormalCard,
  SpecialCard,
  SpecialCards,
} from "tichu-client-ts-lib";
import { mapEnumValue } from "ts-enum-util";
import styles from "./Card.module.css";
import { classes } from "../util";

type CardSize = "small" | "regular";

interface CardViewProps {
  card: Card;
  size?: CardSize;
  handleSelect: (isSelected: boolean, card: Card) => void;
}

const colorFor = (card: Card) => {
  if (card.type === "normal") {
    return mapEnumValue((card as NormalCard).suit).with({
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
    const nCard = card as NormalCard;
    if (nCard.number > 10) {
      return ["J", "Q", "K", "A"][nCard.number - 11]; // wooh untested magic
    } else {
      return nCard.number.toString();
    }
  } else {
    return mapEnumValue((card as SpecialCard).special).with({
      [SpecialCards.MahJong]: "1",
      [SpecialCards.Dog]: "🐶",
      [SpecialCards.Phoenix]: "🦚",
      [SpecialCards.Dragon]: "🐉",
    });
  }
};

/**
 * I don't know how to prevent children elements - does it matter?
 */
export const CardView: FC<CardViewProps> = ({
  card,
  handleSelect,
  size = "regular",
}: CardViewProps) => {
  const [isSelected, setIsSelected] = React.useState(false);
  const color = colorFor(card);
  const symbol = symbolFor(card);

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
        {symbol}
      </div>
      <div className={styles.cardName}>{card.name}</div>
    </div>
  );
};
