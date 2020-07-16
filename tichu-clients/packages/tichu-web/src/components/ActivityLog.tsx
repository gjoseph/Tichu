import React, { FC } from "react";
import styles from "./ActivityLog.module.css";

// The component's model will likely be different from the actual websocket messages:
// I expect we'll have a richer type for activity logs rather than a boolean for debug messages
export type ActivityLogMessage = { message: string; debug: boolean };

export const ActivityLog: FC<{ log: ActivityLogMessage[]; debug: boolean }> = ({
  log,
  debug,
}) => {
  return (
    <div>
      <h3>Activity log</h3>
      <ul>
        {log
          .filter((lm) => debug || !lm.debug)
          .map((lm, idx) => (
            <li key={idx} className={lm.debug ? styles.debug : ""}>
              {lm.message}
            </li>
          ))}
      </ul>
    </div>
  );
};
