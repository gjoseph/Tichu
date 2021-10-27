import { actions } from "@storybook/addon-actions";
import React from "react";
import { cardFromName } from "tichu-client-ts-lib";
import { Hand } from "../components/Hand";

export default {
  title: "Hand",
  component: Hand,
};
const events = actions("sendCards");

export const sample_Hand = () => {
  const hand = [cardFromName("*P"), cardFromName("GJ"), cardFromName("K6")];
  return <Hand cardsInHand={hand} {...events} />;
};
