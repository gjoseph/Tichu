import React, { FC } from "react";
import { CardView } from "./CardView";
import { Card } from "tichu-client-ts-lib";
import "./Card.css";

type CardSetProps = {
  cards: Card[];
};

export const CardSet: FC<CardSetProps> = (props: CardSetProps) => {
  return (
    <div className="card-set">
      {props.cards.map((card, idx) => (
        <div style={{ left: `${idx * 5}em` }}>
          <CardView card={card} />
        </div>
      ))}
    </div>
  );
};
