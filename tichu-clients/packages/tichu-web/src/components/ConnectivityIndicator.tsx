import React, { FC } from "react";
import { OutgoingMessage, WSTichuClient } from "tichu-client-ts-lib";
import { useNetworkAvailability } from "../react-utils";
import styles from "./ConnectivityIndicator.module.css";

/**
 * Render-only component that should show traffic going in and out, possibly showing pings and pongs differently than
 * other traffic. Also have a "connected" indicator?
 * Instead of re-rendering the whole component or even the arrow, we could use css animation to "flash" the appropriate
 * color?
 *
 * Connected component:
 * - add way to register itself against tichuClient/websocket
 * - onping-receive
 * - onpong-send
 * - on-send
 * - on-receive
 *
 * Prop for flashDuration?
 */
export enum ConnectivityColors {
  GREY = "#5a5a5a",
  RED = "#b74444",
  GREEN = "#44b700",
}
export const ConnectivityIndicator: FC<{
  wsConnected: boolean;
  networkConnected: boolean;
  up?: ConnectivityColors;
  down?: ConnectivityColors;
}> = ({
  wsConnected,
  networkConnected,
  up = ConnectivityColors.GREY,
  down = ConnectivityColors.GREY,
}) => {
  return (
    <div className={styles.connectivityIndicator}>
      <svg viewBox="0 0 100 50" xmlns="http://www.w3.org/2000/svg">
        <rect fill="#333" x="0" y="0" height="50" width="100" rx="10" />
        <rect fill="#000" x="2" y="2" height="46" width="96" rx="8" />
        <path fill={up} d="m25.568 45h-15l20-40 20 40h-15z" />
        <path fill={down} d="m74.432 5h15l-20 40-20-40h15z" />
      </svg>
      <p>websocket: {wsConnected ? "" : "not"} connected</p>
      <p>network: {networkConnected ? "" : "not"} connected</p>
    </div>
  );
};

export const ConnectivityIndicatorConnected: FC<{
  wsClient: WSTichuClient;
}> = ({ wsClient }) => {
  const [wsConnected, setWsConnected] = React.useState(wsClient.isConnected());
  const networkConnected = useNetworkAvailability();
  const [sending, setSending] = React.useState(ConnectivityColors.GREY);
  const [receiving, setReceiving] = React.useState(ConnectivityColors.GREY);
  React.useEffect(
    () => {
      console.log("Applying ConnectivityIndicatorConnected effect");
      wsClient.registerOnOpen(() => {
        setWsConnected(true);
      });
      wsClient.registerOnClose(() => {
        setWsConnected(false);
      });
      wsClient.registerOnSend((msg: OutgoingMessage) => {
        setSending(ConnectivityColors.GREEN);
        setTimeout(() => setSending(ConnectivityColors.GREY), 200);
      });
      wsClient.registerOnReceive(() => {
        setReceiving(ConnectivityColors.GREEN);
        setTimeout(() => setReceiving(ConnectivityColors.GREY), 200);
      });
    },
    // re-run this effect is wsClient changes
    [wsClient]
  );
  return (
    <ConnectivityIndicator
      wsConnected={wsConnected}
      networkConnected={networkConnected}
      up={sending}
      down={receiving}
    />
  );
};
