import { OutgoingMessage } from "./messages";
import { WebSocketData } from "./ws-client";

export type EventType = "connect" | "disconnect" | "receive" | "send" | "error";

export interface EventParam<T extends EventType> {}

// Events params are the common properties of browser websocket and the ws package's corresponding events,

export interface OnOpenParams extends EventParam<"connect"> {}
export interface OnCloseParams extends EventParam<"disconnect"> {
  code: number;
  reason: string;
  wasClean: boolean;
}

// browser Websocket does not have an ErrorEvent type; it's a generic Event with type "error", no message or error object.
export interface OnErrorParams extends EventParam<"error"> {
  message?: string;
  error?: any;
}

// browser Websocket as `lastEventId` and `origin` properties in addition for MessageEvent, not sure what they're for
export interface OnReceiveParams extends EventParam<"receive"> {
  data: WebSocketData;
}

export interface OnSendParams extends EventParam<"send"> {
  message: OutgoingMessage;
}

export type Callback<E extends EventType, P extends EventParam<E>> = (
  param: P
) => void;

export class EventDispatcher {
  // TODO unsure if this map declaration helps typing-wise
  private readonly listeners: Map<
    EventType,
    Callback<EventType, EventParam<EventType>>[]
  >;

  constructor() {
    this.listeners = new Map<
      EventType,
      Callback<EventType, EventParam<EventType>>[]
    >();
  }

  // TODO types - this doesn't prevent anyone from calling on(open) with a function that takes a OnCloseParams
  // but interestingly enough the same method expose in ws-client worked (i.e preventing passing wrong type of param)
  // when EventType was an enum
  on<E extends EventType, P extends EventParam<E>>(
    eventType: E,
    callback: Callback<E, P>
  ) {
    const current = this.listenersFor(eventType);
    // @ts-ignore // TODO
    this.listeners.set(eventType, [...current, callback]);
  }

  // TODO allow de-registering listeners

  dispatch<T extends EventType>(eventType: T, param: EventParam<T>) {
    this.listenersFor(eventType).forEach((callback) => callback(param));
  }

  private listenersFor<E extends EventType, P extends EventParam<E>>(
    eventType: E
  ): Callback<E, P>[] {
    return this.listeners.get(eventType) || [];
  }
}
