import React from "react";
import { CardView } from "../components/CardView";
import { cardFromName } from "tichu-client-ts-lib";
import { action, actions } from "@storybook/addon-actions";

export default {
  title: "Single cards",
  component: CardView,
};

const events = actions("handleSelect");

// Potentially, we could use https://github.com/storybookjs/storybook/tree/master/addons/knobs
// to add a card picker without duplicating the stories (alternative, using storyOf might enable some more dynamic story generation)
export const MahJong = () => <CardView card={cardFromName("*1")} {...events} />;
export const _4Red = () => <CardView card={cardFromName("R4")} {...events} />;
export const _7Green = () => <CardView card={cardFromName("G7")} {...events} />;
export const _10Black = () => (
  <CardView card={cardFromName("K0")} {...events} />
);
export const _JackBlue = () => (
  <CardView card={cardFromName("BJ")} {...events} />
);
