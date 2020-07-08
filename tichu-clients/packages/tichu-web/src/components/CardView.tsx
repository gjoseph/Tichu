import React, { FC } from "react";
import { Card, CardSuit, NormalCard } from "tichu-client-ts-lib";
import { mapEnumValue } from "ts-enum-util";
import styles from "./Card.module.css";

type CardViewProps = {
  card: Card;
};

/**
 * I don't know how to prevent children elements - does it matter?
 */
export const CardView: FC<CardViewProps> = ({ card }: CardViewProps) => {
  var color: string;
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
  return (
    <div className={styles.card}>
      <span className={styles.cardShortName} style={{ color: color }}>
        {card.shortName}
      </span>
      <span className={styles.cardName}>{card.name}</span>
    </div>
  );
};
