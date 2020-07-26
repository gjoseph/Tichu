import React, { FC } from "react";
import { CardBack } from "./CardBack";
import { CardView } from "./CardView";
import { Card } from "tichu-client-ts-lib";
import styles from "./Card.module.css";
import { classes } from "../util";

type CardSize = "small" | "regular";

interface CardSetProps {
  cards: Card[];
  cardSize?: CardSize;
  handleSelect: (isSelected: boolean, card: Card) => void;
}

const CardPosition: FC<{
  idx: number;
  cardSize: CardSize;
  children: React.ReactNode;
}> = ({ idx, cardSize, children }) => {
  const space = idx * (cardSize === "small" ? 2 : 6);
  return (
    <div style={{ transform: `translate(${space}em, 0)` }}>{children}</div>
  );
};

export const CardSet: FC<CardSetProps> = ({
  cards,
  handleSelect,
  cardSize = "regular",
}) => {
  const classNames = classes(
    styles.cardSet,
    cardSize === "small" ? styles.smallSet : styles.regularSet
  );
  return (
    <div className={classNames}>
      {cards.map((card, idx) => (
        <CardPosition idx={idx} cardSize={cardSize} key={idx}>
          <CardView card={card} size={cardSize} handleSelect={handleSelect} />
        </CardPosition>
      ))}
    </div>
  );
};

export const CardBacks: FC<{ count: number; cardSize?: CardSize }> = ({
  count,
  cardSize = "small",
}) => {
  const classNames = classes(
    styles.cardSet,
    cardSize === "small" ? styles.smallSet : styles.regularSet
  );
  return (
    <div className={classNames}>
      {Array.from(new Array(count), (ignoreNull, idx) => (
        <CardPosition idx={idx} cardSize={cardSize} key={idx}>
          <CardBack size={cardSize} />
        </CardPosition>
      ))}
    </div>
  );
};
