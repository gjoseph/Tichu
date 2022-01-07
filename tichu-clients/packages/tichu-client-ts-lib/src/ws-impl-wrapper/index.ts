import {
  EventParam,
  EventType,
  OnCloseParams,
  OnErrorParams,
  OnOpenParams,
  OnReceiveParams,
} from "../events";

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
  if (typeof WebSocket !== "undefined") {
    ws = new BrowserWSWrapper(url);
  } else {
    ws = new WSLibWrapper(url);
  }
  // TODO wtf are subprotocols
  return ws;
};

class BrowserWSWrapper extends WebSocket {
  isConnected = () => {
    return this.readyState === WebSocket.OPEN;
  };
}

class WSLibWrapper {
  private readonly wsLibWS;
  constructor(readonly address: string) {
    const wsLibModule = require("ws");
    this.wsLibWS = new wsLibModule.WebSocket(address);
  }

  onopen = (e: OnOpenParams) => {
    this.wsLibWS.onopen(this.extendEvent(e));
  };

  onclose = (e: OnCloseParams) => {
    // browser and ws onclose are incompatible because the latter's WebSocket.CloseEvent.target doesn't allow null
    const extendEvent: OnCloseParams & { target: any } = this.extendEvent(e);
    this.wsLibWS.onclose(extendEvent);
  };

  onerror = (e: OnErrorParams) => {
    const extendEvent: OnErrorParams & { target: any } = this.extendEvent(e);
    // error and message are not optional in wsLib ErrorEvent
    // TODO should we revisit and make them non-optional in OnErrorParams instead?
    const e2 = {
      ...extendEvent,
      error: e.error || "unspecified error",
      message: e.message || "unspecified error message",
    };
    this.wsLibWS.onerror(e2);
  };

  onmessage = (e: OnReceiveParams) => {
    this.wsLibWS.onmessage(this.extendEvent(e));
  };

  // TODO we should/could support other message types, and additional args
  send = (message: string) => {
    this.wsLibWS.send(message);
  };

  close = () => {
    this.wsLibWS.close();
  };

  isConnected = () => {
    return this.wsLibWS.readyState === WebSocket.OPEN; // we can only only assume this is standard per the protocol, so using the dom constant should be fine here
  };

  private extendEvent<E extends EventParam<T>, T extends EventType>(e: E) {
    return { ...e, target: this.wsLibWS };
  }
}
