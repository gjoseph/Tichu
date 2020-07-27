import React from "react";
import { CardBacks, CardSet } from "../components/CardSet";
import { AllCards, cardFromName } from "tichu-client-ts-lib";
import { actions } from "@storybook/addon-actions";

export default {
  title: "Card Set",
  component: CardSet,
};
const events = actions("handleSelect");

export const _3Cards_Flat = () => {
  const cards = [cardFromName("*D"), cardFromName("GJ"), cardFromName("K6")];
  return <CardSet cards={cards} style="flat" {...events} />;
};

export const _5Cards_Fanned = () => {
  const rnd = [...AllCards].sort(() => Math.random() - 0.5).slice(0, 5);
  return <CardSet cards={rnd} style="fanned" {...events} />;
};

export const _14Cards_Stacked = () => {
  const rnd = [...AllCards].sort(() => Math.random() - 0.5).slice(0, 14);
  return <CardSet cards={rnd} style="stacked" {...events} />;
};

export const All_Cards = () => (
  <CardSet cards={AllCards} cardSize="small" style="stacked" {...events} />
);
All_Cards.story = { name: "All Cards (small, stacked)" };

export const _3_Backs_Flat = () => <CardBacks count={3} style="flat" />;
export const _7_Backs_Fanned = () => <CardBacks count={7} style="fanned" />;
export const _14_Backs_Stack = () => <CardBacks count={14} style="stacked" />;
