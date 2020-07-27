import React from "react";
import { CardBacks, CardSet } from "../components/CardSet";
import { AllCards, cardFromName } from "tichu-client-ts-lib";
import { actions } from "@storybook/addon-actions";

export default {
  title: "Card Set",
  component: CardSet,
};
const events = actions("handleSelect");

export const _3Cards = () => {
  const cards = [cardFromName("*D"), cardFromName("GJ"), cardFromName("K6")];
  return <CardSet cards={cards} {...events} />;
};

export const _5Cards = () => {
  const rnd = [...AllCards].sort(() => Math.random() - 0.5).slice(0, 5);
  return <CardSet cards={rnd} {...events} />;
};

export const _14Cards = () => {
  const rnd = [...AllCards].sort(() => Math.random() - 0.5).slice(0, 14);
  return <CardSet cards={rnd} {...events} />;
};

export const All_Cards = () => (
  <CardSet cards={AllCards} {...events} cardSize="small" />
);
All_Cards.story = { name: "All Cards (small)" };

export const _3_Backs = () => <CardBacks count={3} />;
export const _14_Backs = () => <CardBacks count={14} />;
