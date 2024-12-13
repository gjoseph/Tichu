import React, { FC, useState } from "react";
import styles from "./ActivityLog.module.css";

// The component's model will likely be different from the actual websocket messages:
// I expect we'll have a richer type for activity logs rather than a boolean for debug messages
export interface ActivityLogMessage {
  message: string;
  debug: boolean;
}

export const ActivityLog: FC<{
  log: ActivityLogMessage[];
  showDebug?: boolean;
}> = (props) => {
  const [showDebug, setShowDebug] = useState(props.showDebug ?? false);
  return (
    <div>
      <h3>Activity log</h3>
      <ul>
        {props.log
          .filter((lm) => showDebug || !lm.debug)
          .map((lm, idx) => (
            <li key={idx} className={lm.debug ? styles.debug : ""}>
              {lm.message}
            </li>
          ))}
      </ul>
      <form>
        <label>
          <input
            type="checkbox"
            checked={showDebug}
            onChange={() => {
              setShowDebug(!showDebug);
            }}
          />
          Show debug
        </label>
      </form>
    </div>
  );
};
