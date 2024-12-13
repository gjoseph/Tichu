import React from "react";
import {
  ConnectivityColors,
  ConnectivityIndicator,
} from "../components/ConnectivityIndicator";
import { Meta } from "@storybook/react";
import { disableControls, makeStory } from "./stories";

export default {
  title: "Connectivity Indicator",
  component: ConnectivityIndicator,
  parameters: disableControls,
} as Meta;

export const SimpleStates = makeStory(() => (
  <div style={{ width: "100px" }}>
    <ConnectivityIndicator wsConnected={false} networkConnected={false} />
    <hr />
    <ConnectivityIndicator wsConnected={true} networkConnected={false} />
    <hr />
    <ConnectivityIndicator wsConnected={false} networkConnected={true} />
    <hr />
    <ConnectivityIndicator wsConnected={true} networkConnected={true} />
    <hr />
    <ConnectivityIndicator
      wsConnected={false}
      networkConnected={true}
      up={ConnectivityColors.GREEN}
      down={ConnectivityColors.RED}
    />
    <hr />
    <ConnectivityIndicator
      wsConnected={true}
      networkConnected={true}
      up={ConnectivityColors.GREEN}
      down={ConnectivityColors.RED}
    />
  </div>
));

/*
Tried using jest-websocket-mock but that didn't seem to work, without importing a whole lot of other test dependencies.
We're probably better off writing this as a test anyway, which I started in `ConnectivityIndicator-test` branch.

export const SimulatedConnection = async () => {
  // create a WS instance, listening on port 1234 on localhost
  const server = new MockWebSocket("ws://localhost:1234");
  const wsClient = new WSTichuClient(
    "bogus",
    () => new DummyTichuWebSocketHandler()
  );
  wsClient.connect("ws://localhost:1234");
  await server.connected; // wait for the server to have established the connection

  setInterval(() => {
    wsClient.send(new OutgoingChatMessage("hello"));
  }, 2000);
  setInterval(() => {
    wsClient.receive('{ "messageType": "chat" }');
  }, 3000);

  return (
    <>
      <p>Sending a message every 2 seconds, receiving one every 3</p>
      <div style={{ width: "100px" }}>
        <ConnectivityIndicatorConnected wsClient={wsClient} />
      </div>
    </>
  );
};
*/
