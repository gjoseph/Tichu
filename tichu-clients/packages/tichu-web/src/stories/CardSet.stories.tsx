import React from "react";
import { CardSet } from "../components/CardSet";
import { AllCards, cardFromName } from "tichu-client-ts-lib";

export default {
  title: "Card Set",
  component: CardSet,
};

export const _3Cards = () => (
  <CardSet
    cards={[cardFromName("*D"), cardFromName("GJ"), cardFromName("K6")]}
  />
);

export const _14Cards = () => <CardSet cards={AllCards} />;
