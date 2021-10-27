import { actions } from "@storybook/addon-actions";
import React from "react";
import { AllCards } from "tichu-client-ts-lib";
import { CardBacks, CardSet } from "../components/CardSet";
import { countKnob, sizeKnob, styleKnob } from "./knobs";

export default {
  title: "Card Set",
  component: CardSet,
};
const events = actions("handleSelect");

export const Card_Set = () => {
  const cards = [...AllCards]
    .sort(() => Math.random() - 0.5)
    .slice(0, countKnob(14));
  return (
    <CardSet
      cards={cards}
      cardSize={sizeKnob()}
      style={styleKnob()}
      {...events}
    />
  );
};

export const Stack_of_56 = () => (
  <CardSet cards={AllCards} cardSize={sizeKnob()} style="stacked" {...events} />
);

export const Card_Backs = () => {
  return (
    <CardBacks
      count={countKnob(56)}
      cardSize={sizeKnob()}
      style={styleKnob()}
      {...events}
    />
  );
};
