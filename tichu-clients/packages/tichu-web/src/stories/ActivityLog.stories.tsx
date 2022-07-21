import React from "react";
import { ActivityLog } from "../components/ActivityLog";
import { disableControls, makeStory } from "./stories";
import { Args, Meta } from "@storybook/react";

export default {
  title: "Activity Log",
  component: ActivityLog,
  parameters: disableControls,
} as Meta;

// const events = actions("enableDebug");
const sampleMessages = [
  { message: "Hello, world", debug: false },
  { message: "This is a debug message", debug: true },
  { message: "This is another message", debug: false },
];

export const MessageStylesWithDebugOn = makeStory((args: Args) => {
  return <ActivityLog showDebug log={sampleMessages} />;
});

export const MessageStylesWithDebugOff = makeStory((args: Args) => {
  return <ActivityLog log={sampleMessages} />;
});
