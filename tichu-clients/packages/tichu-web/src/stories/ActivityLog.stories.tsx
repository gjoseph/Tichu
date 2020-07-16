import React from "react";
import { action, actions } from "@storybook/addon-actions";
import { ActivityLog } from "../components/ActivityLog";

export default {
  title: "Activity Log",
  component: ActivityLog,
};

// const events = actions("enableDebug");

export const MessageStylesWithDebugOn = () => (
  <ActivityLog
    debug
    log={[
      { message: "Hello, world", debug: false },
      { message: "This is a debug message", debug: true },
      { message: "This is another message", debug: false },
    ]}
  />
);
