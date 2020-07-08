import React, { FC } from "react";
import { CardView } from "./CardView";
import { Card } from "tichu-client-ts-lib";
import styles from "./Card.module.css";

type CardSetProps = {
  cards: Card[];
};

const CardPosition: FC<{ idx: number; children: React.ReactNode }> = ({
  idx,
  children,
}) => {
  // <div style={{ left: `${idx * 5}em` }}>
  return (
    <div style={{ transform: `translate(${idx * 5}em, 0)` }}>{children}</div>
  );
};

export const CardSet: FC<CardSetProps> = (props: CardSetProps) => {
  return (
    <div className={styles.cardSet}>
      {props.cards.map((card, idx) => (
        <CardPosition idx={idx}>
          <CardView card={card} />
        </CardPosition>
      ))}
    </div>
  );
};
