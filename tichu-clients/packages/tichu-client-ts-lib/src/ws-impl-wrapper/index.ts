import { BrowserWSWrapper } from "./ws-browser-wrapper";
import { WSLibWrapper } from "./ws-lib-wrapper";

/**
 * A thin wrapper around (dom)WebSocket and ws.WebSocket which
 * a) hides the conditional import
 * b) hides the minor differences between the two
 */
// export type WebSocketTypes = Omit<WebSocket, "dispatchEvent" | "binaryType" | "onerror">;// can't manage to reference the type of "ws" here (used to do WebSocket|wsLib.WebSocket); it's compatible enough but doesn't have the dispatchEvent method
export type WebSocketTypes = (WebSocket | WSLibWrapper) & {
  isConnected: () => boolean;
};

export const newWebsocket = (url: string, bogusCredentials: string) => {
  // for now, pass==user -- we'll want to get rid of basic auth -- https://auth0.com/docs/integrations/using-auth0-to-secure-a-cli
  const userPass = `${bogusCredentials}:${bogusCredentials}`;
  // options don't exist in the browser js API for websocket, so we can't pass a header
  // doing this for now -- will need to use proper tokens/tickets
  url = url.replace("://", `://${userPass}@`);

  let ws: WebSocketTypes;
  console.log("typeof WebSocket:", typeof WebSocket);
  if (typeof WebSocket !== "undefined") {
    const impl = require("./ws-browser-wrapper");
    ws = new impl.BrowserWSWrapper(url);
  } else {
    const impl = require("./ws-lib-wrapper");
    ws = new impl.WSLibWrapper(url);
  }
  console.log("ws.constructor.name:", ws.constructor.name);
  // TODO wtf are subprotocols
  return ws;
};
