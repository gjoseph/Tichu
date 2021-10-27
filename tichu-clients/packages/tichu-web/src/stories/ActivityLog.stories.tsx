import React from "react";
import { ActivityLog } from "../components/ActivityLog";

export default {
  title: "Activity Log",
  component: ActivityLog,
};

// const events = actions("enableDebug");
const sampleMessages = [
  { message: "Hello, world", debug: false },
  { message: "This is a debug message", debug: true },
  { message: "This is another message", debug: false },
];

export const MessageStylesWithDebugOn = () => {
  return <ActivityLog showDebug log={sampleMessages} />;
};
export const MessageStylesWithDebugOff = () => {
  return <ActivityLog log={sampleMessages} />;
};
