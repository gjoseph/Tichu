import { IncomingChatMessage, OnResponse } from "tichu-client-ts-lib";
import React, { FC, FormEvent, useState } from "react";
import { LinearProgress } from "@mui/material";
// import styles from "./Chat.module.css";

// The component's model will likely be different from the actual websocket messages:
export type ChatMessage = IncomingChatMessage;
export const Chat: FC<{
  chatMessages: ChatMessage[];
  sendChatMessage: (text: string, onResponse: OnResponse) => void;
}> = ({ chatMessages, sendChatMessage }) => {
  const [sending, setSending] = useState(false);
  const [messageText, setMessageText] = useState("");
  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    // spinner until new message comes in
    setSending(true);
    sendChatMessage(messageText, stopSendSpinner);
    setMessageText("");
  };
  const stopSendSpinner = () => {
    setSending(false);
  };
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
      {sending ? (
        <LinearProgress />
      ) : (
        <form onSubmit={handleSubmit}>
          <label>
            Chat:
            <textarea
              value={messageText}
              onChange={(e) => setMessageText(e.target.value)}
            />
          </label>
          <input type="submit" value="Send" />
        </form>
      )}
    </div>
  );
};
