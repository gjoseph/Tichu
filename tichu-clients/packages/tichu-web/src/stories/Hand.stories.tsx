import { actions } from "@storybook/addon-actions";
import React from "react";
import { cardFromName } from "tichu-client-ts-lib";
import { Hand } from "../components/Hand";
import { Args, Meta } from "@storybook/react";
import { disableControls, makeStory } from "./stories";

export default {
  title: "Hand",
  component: Hand,
  parameters: disableControls,
} as Meta;
const events = actions("sendCards");

export const sample_Hand = makeStory((args: Args) => {
  const hand = [cardFromName("*P"), cardFromName("GJ"), cardFromName("K6")];
  return <Hand cardsInHand={hand} {...events} />;
});
