import React, { FC, useCallback, useEffect, useState } from "react";
import { Game } from "./components/Game";
import {
  JoinParam,
  OutgoingGameMessage,
  PlayerIsReadyParam,
  WSTichuClient,
} from "tichu-client-ts-lib";
import { newReactHandler } from "./tichu-ws-handler";
import { SnackbarProvider, useSnackbar } from "notistack";
import { visitEnumValue } from "ts-enum-util";
import { GameState } from "./model/GameState";
import { RoomState, RoomStatus } from "./model/RoomState";
import { User } from "./model/User";
import { Chat, ChatMessage } from "./components/Chat";
import { ActivityLog } from "./components/ActivityLog";

type PossibleWSTichuClient = WSTichuClient | undefined;
const Room: FC<{ user: User; websocketUrl: string }> = (props) => {
  const { enqueueSnackbar, closeSnackbar } = useSnackbar();
  const [roomState, setRoomState] = useState<RoomState>(
    new RoomState(RoomStatus.OPEN)
  );
  const [gameState, setGameState] = useState<GameState>(new GameState([]));

  const [chatMessages, setChatMessages] = useState(new Array<ChatMessage>());
  const newMessage = (newMessage: ChatMessage) => {
    setChatMessages((currentMessages) => [...currentMessages, newMessage]);
  };

  const [wsClient, setWsClient] = useState<PossibleWSTichuClient>(undefined);
  const connect = useCallback(() => {
    setWsClient(
      new WSTichuClient(
        props.user.id,
        newReactHandler(enqueueSnackbar, setRoomState, setGameState, newMessage)
      ).connect(props.websocketUrl)
    );
    // Still unsure why enqueueSnackbar needs to be a dep, but not newMessage
    // TODO read https://overreacted.io/a-complete-guide-to-useeffect/ again
  }, [props.websocketUrl, props.user, enqueueSnackbar]);

  useEffect(() => {
    connect();
    // Return a cleanup function
    return () => {};
  }, [connect]);

  const justWatch = () => {};
  // TODO also better deal with absent wsClient
  const join = (teamId: number) => () => {
    wsClient!.send(new OutgoingGameMessage(new JoinParam(teamId)));
  };
  const ready = () => {
    wsClient!.send(new OutgoingGameMessage(new PlayerIsReadyParam()));
  };
  const game = visitEnumValue(roomState.status).with<JSX.Element>({
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
      <p>Hello, {props.user.name}</p>
      <p>STATUS : {roomState.status}</p>
      {game}
      <Chat chatMessages={chatMessages} />
      <ActivityLog />
    </>
  );
};

const Button: FC<{ msg: string; handleClick: () => void }> = ({
  msg,
  handleClick,
}) => {
  return <button onClick={handleClick}>{msg}</button>;
};

const App: FC<{ websocketUrl: string }> = (props) => {
  const [user, setUser] = useState<User>(
    new User("no-id-yet", "Jules Maigret")
  );

  return (
    <div className="App">
      <SnackbarProvider maxSnack={3}>
        <Room user={user} websocketUrl={props.websocketUrl} />
      </SnackbarProvider>
    </div>
  );
};

export default App;
