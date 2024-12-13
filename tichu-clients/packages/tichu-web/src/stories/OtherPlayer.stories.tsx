import React from "react";
import { OtherPlayer } from "../components/OtherPlayer";
import { User } from "../model/User";
import { Meta } from "@storybook/react";
import { disableControls, makeStory } from "./stories";

export default {
  title: "Other Player",
  component: OtherPlayer,
  parameters: disableControls,
} as Meta;

export const With3Cards = makeStory(() => (
  <OtherPlayer user={new User("1", "Alex Jules")} handCardCount={3} />
));
