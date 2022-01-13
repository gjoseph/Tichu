import {
  EventParam,
  EventType,
  OnCloseParams,
  OnErrorParams,
  OnOpenParams,
  OnReceiveParams,
} from "../events";

export class WSLibWrapper {
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
