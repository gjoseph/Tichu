import React, { useState } from "react";
import { Chat, ChatMessage } from "../components/Chat";
import { actions } from "@storybook/addon-actions";

export default {
  title: "Chat",
  component: Chat,
};

const events = actions("sendChatMessage");
const sampleMessages: ChatMessage[] = [
  {
    from: "Alex",
    content: "Hi I'm Alex",
    messageType: "chat",
  },
  {
    from: "Jules",
    content: "Hi, my name is Jules and I'm a Tichuolic",
    messageType: "chat",
  },
  {
    from: "Charlie",
    content: "Hi, this is Charlie",
    messageType: "chat",
  },
  {
    from: "Quinn",
    content: "Quinn is I",
    messageType: "chat",
  },
];

const dup = (a: any[]) => {
  return [...a, ...a];
};

const longSampleChat: ChatMessage[] = dup(
  dup(dup(dup(dup(dup(dup(dup(dup(dup(sampleMessages)))))))))
);

export const ChatOpenEmpty = () => {
  return <Chat chatMessages={[]} {...events} />;
};

export const ChatOpen = () => {
  return <Chat chatMessages={sampleMessages} {...events} />;
};

export const ChatOpenLong = () => {
  return <Chat chatMessages={longSampleChat} {...events} />;
};

export const SendNewMessage = () => {
  const [chatMessages, setChatMessages] = useState(
    new Array<ChatMessage>({
      from: "Storybook",
      content: "Go on, type and send a message below...",
      messageType: "chat",
    })
  );
  const newChatMessage = (newMessage: string, callback: () => void) => {
    // create simulation of delay between sending the message to server and reception of message back
    setTimeout(() => {
      setChatMessages((currentMessages) => [
        ...currentMessages,
        {
          from: "Storybook",
          content: newMessage,
          messageType: "chat",
        },
      ]);
      callback();
    }, 500);
  };

  return <Chat chatMessages={chatMessages} sendChatMessage={newChatMessage} />;
};
