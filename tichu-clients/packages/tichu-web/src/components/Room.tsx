import { useNotifications } from "@toolpad/core/useNotifications";
import React, { FC, useCallback, useEffect, useState } from "react";
import {
  JoinParam,
  OnResponse,
  OutgoingChatMessage,
  OutgoingGameMessage,
  PlayerIsReadyParam,
  WSTichuClient,
} from "tichu-client-ts-lib";
import { visitEnumValue } from "ts-enum-util";
import { GameState } from "../model/GameState";
import { RoomState, RoomStatus } from "../model/RoomState";
import { User } from "../model/User";
import { newReactHandler } from "../tichu-ws-handler";
import { ActivityLog, ActivityLogMessage } from "./ActivityLog";
import { Button } from "./Button";
import { Chat, ChatMessage } from "./Chat";
import { ConnectivityIndicatorConnected } from "./ConnectivityIndicator";
import { Game } from "./Game";
import { withToolpad } from "../notifications";

type PossibleWSTichuClient = WSTichuClient | undefined;
export const Room: FC<{ user: User; websocketUrl: string }> = (props) => {
  const notifications = useNotifications();
  const [roomState, setRoomState] = useState<RoomState>(
    new RoomState(RoomStatus.OPEN),
  );
  const [gameState, setGameState] = useState<GameState>(
    new GameState(undefined, [], []),
  );

  const [chatMessages, setChatMessages] = useState(new Array<ChatMessage>());
  const newChatMessage = (newMessage: ChatMessage) => {
    setChatMessages((currentMessages) => [...currentMessages, newMessage]);
  };
  const sentChatMessage = (text: string, onResponse: OnResponse) => {
    wsClient?.send(new OutgoingChatMessage(text), onResponse);
  };

  const [activityLog, setActivityLog] = useState(
    new Array<ActivityLogMessage>(),
  );
  const newActivity = (newAct: ActivityLogMessage) => {
    setActivityLog((current) => [...current, newAct]);
  };

  // TODO: move all this connectivity stuff one level up
  const [wsClient, setWsClient] = useState<PossibleWSTichuClient>(undefined);
  const connect = useCallback(() => {
    setWsClient(
      new WSTichuClient(
        props.user.id,
        newReactHandler(
          withToolpad(notifications),
          setRoomState,
          setGameState,
          // TODO so for example this and all other callbacks could be set by the listening components now?
          // using on("message") and filtering on the message type
          // which we should maybe do in ws-client
          newChatMessage,
          newActivity,
        ),
      ).connect(props.websocketUrl),
    );
    // Still unsure why notifications/enqueueSnackbar needs to be a dep, but not newMessage
    // TODO read https://overreacted.io/a-complete-guide-to-useeffect/ again
  }, [props.websocketUrl, props.user, notifications]);

  useEffect(() => {
    connect();
    return () => {
      /* nothing to do on cleanup */
    };
  }, [connect]);

  const justWatch = () => {
    /* do nothing */
  };
  // TODO also better deal with absent wsClient
  const join = (teamId: number) => () => {
    wsClient?.send(new OutgoingGameMessage(new JoinParam(teamId)));
  };
  const ready = () => {
    wsClient?.send(new OutgoingGameMessage(new PlayerIsReadyParam()));
  };
  const game = visitEnumValue(roomState.status).with<React.JSX.Element>({
    [RoomStatus.OPEN]: () => <p>Trying to connect</p>,
    [RoomStatus.CONNECTED]: () => (
      <>
        <Button msg="Join team 1?" handleClick={join(0)} />
        <Button msg="Join team 2?" handleClick={join(1)} />
        <Button msg="Just watch" handleClick={justWatch} />
      </>
    ),
    [RoomStatus.NEED_PLAYERS]: () => <>need players</>,
    [RoomStatus.WAIT_FOR_READY]: () => (
      // remove button if this player is ready
      <Button msg="I'm ready" handleClick={ready} />
    ),
    [RoomStatus.PLAYING]: () =>
      wsClient ? (
        <Game gameState={gameState} sendMessage={wsClient.send} />
      ) : (
        <p>no ws client yet...</p>
      ),
    [RoomStatus.FINISHED]: () => <>fin</>,
  });

  return (
    <>
      <p>STATUS : {roomState.status}</p>
      {game}
      <Chat chatMessages={chatMessages} sendChatMessage={sentChatMessage} />
      <ActivityLog showDebug log={activityLog} />
      <div style={{ width: "100px" }}>
        {wsClient ? (
          <ConnectivityIndicatorConnected wsClient={wsClient} />
        ) : (
          <p>not connected yet</p>
        )}
      </div>
    </>
  );
};
