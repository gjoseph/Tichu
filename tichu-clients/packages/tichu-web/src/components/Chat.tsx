import { IncomingChatMessage } from "tichu-client-ts-lib";
import React, { FC } from "react";

// The component's model will likely be different from the actual websocket messages:
export type ChatMessage = IncomingChatMessage;
export const Chat: FC<{ chatMessages: ChatMessage[] }> = ({ chatMessages }) => {
  return (
    <div>
      <h3>Chat</h3>
      <ul>
        {chatMessages.map((m, idx) => (
          <li key={idx}>
            {m.from}: {m.content}
          </li>
        ))}
      </ul>
    </div>
  );
};
