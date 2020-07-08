import React, { FC } from "react";
import { CardView } from "./CardView";
import { Card } from "tichu-client-ts-lib";
import styles from "./Card.module.css";

interface CardSetProps {
  cards: Card[];
  handleSelect: (isSelected: boolean, card: Card) => void;
}

const CardPosition: FC<{ idx: number; children: React.ReactNode }> = ({
  idx,
  children,
}) => {
  return (
    <div style={{ transform: `translate(${idx * 5}em, 0)` }}>{children}</div>
  );
};

export const CardSet: FC<CardSetProps> = (props: CardSetProps) => {
  return (
    <div className={styles.cardSet}>
      {props.cards.map((card, idx) => (
        <CardPosition idx={idx} key={card.shortName}>
          <CardView card={card} handleSelect={props.handleSelect} />
        </CardPosition>
      ))}
    </div>
  );
};
