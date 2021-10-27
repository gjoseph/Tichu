import { actions } from "@storybook/addon-actions";
import { select } from "@storybook/addon-knobs";
import React from "react";
import { AllCards, cardFromName } from "tichu-client-ts-lib";
import { CardBack } from "../components/CardBack";
import { CardView } from "../components/CardView";
import { sizeKnob } from "./knobs";

export default {
  title: "Single cards",
  component: CardView,
};

const events = actions("handleSelect");

export const MahJong = () => (
  <CardView card={cardFromName("*1")} size={sizeKnob()} {...events} />
);
export const Dog = () => (
  <CardView card={cardFromName("*H")} size={sizeKnob()} {...events} />
);
export const Phoenix = () => (
  <CardView card={cardFromName("*P")} size={sizeKnob()} {...events} />
);
export const Dragon = () => (
  <CardView card={cardFromName("*D")} size={sizeKnob()} {...events} />
);

export const Card_Front = () => {
  const cardName = select(
    "Card",
    AllCards.map((c) => c.shortName),
    "*1"
  );
  return (
    <CardView card={cardFromName(cardName)} size={sizeKnob()} {...events} />
  );
};
Card_Front.story = { name: "Card Front (with knobs)" };

export const Card_Back = () => {
  return <CardBack size={sizeKnob()} {...events} />;
};
Card_Back.story = { name: "Card Back (with knobs)" };
