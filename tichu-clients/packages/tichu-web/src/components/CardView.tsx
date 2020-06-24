import React, { FC } from "react";
import { Card, CardSuit, NormalCard } from "tichu-client-ts-lib";
import { mapEnumValue } from "ts-enum-util";
import "./Card.css";

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
    <div className="card">
      <span className="card-short-name" style={{ color: color }}>
        {card.shortName}
      </span>
      <span className="card-name">{card.name}</span>
    </div>
  );
};
