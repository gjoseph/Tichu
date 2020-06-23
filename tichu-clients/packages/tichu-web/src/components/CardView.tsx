import React, { FunctionComponent } from "react";
import { ReactComponent as SampleCard } from "../sample_card.svg";
import { Card, cardFromName } from "tichu-client-ts-lib";

type CardViewProps = {};

/**
 * I don't know how to prevent children elements - does it matter?
 */
export const CardView: FunctionComponent<CardViewProps> = (
  props: CardViewProps
) => {
  const card: Card = cardFromName("*P");
  console.log(card);
  return (
    <div className="card" style={{ width: "18rem" }} draggable>
      <SampleCard />
      <span>
        {card.name} ({card.shortName})
      </span>
    </div>
  );
};
