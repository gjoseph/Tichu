import React, {
  Dispatch,
  FC,
  SetStateAction,
  useCallback,
  useEffect,
  useState,
} from "react";
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
import { ActivityLog, ActivityLogMessage } from "./components/ActivityLog";

type PossibleWSTichuClient = WSTichuClient | undefined;
const Room: FC<{ user: User; websocketUrl: string }> = (props) => {
  const { enqueueSnackbar, closeSnackbar } = useSnackbar();
  const [roomState, setRoomState] = useState<RoomState>(
    new RoomState(RoomStatus.OPEN)
  );
  const [gameState, setGameState] = useState<GameState>(new GameState([]));

  const [chatMessages, setChatMessages] = useState(new Array<ChatMessage>());
  const newChatMessage = (newMessage: ChatMessage) => {
    setChatMessages((currentMessages) => [...currentMessages, newMessage]);
  };

  const [activityLog, setActivityLog] = useState(
    new Array<ActivityLogMessage>()
  );
  const newActivity = (newAct: ActivityLogMessage) => {
    setActivityLog((current) => [...current, newAct]);
  };

  const [wsClient, setWsClient] = useState<PossibleWSTichuClient>(undefined);
  const connect = useCallback(() => {
    setWsClient(
      new WSTichuClient(
        props.user.id,
        newReactHandler(
          enqueueSnackbar,
          setRoomState,
          setGameState,
          newChatMessage,
          newActivity
        )
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
      <ActivityLog debug log={activityLog} />
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
  const [user, setUser] = useState<User>();
  return (
    <div className="App">
      <SnackbarProvider maxSnack={3}>
        {user ? (
          <Room user={user} websocketUrl={props.websocketUrl} />
        ) : (
          <DummyUsernameInput setUser={setUser} />
        )}
      </SnackbarProvider>
    </div>
  );
};

const DummyUsernameInput: FC<{
  setUser: Dispatch<SetStateAction<User | undefined>>;
}> = ({ setUser }) => {
  const [name, setName] = useState("");
  return (
    <form
      action=""
      onSubmit={(e) => {
        e.preventDefault();
        setUser(new User(name, name));
      }}
    >
      <label htmlFor="name">
        Please enter your name :
        <input
          type="text"
          id="name"
          placeholder="your name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
      </label>
      <input type="submit" value={"Send"} />
    </form>
  );
};
export default App;
