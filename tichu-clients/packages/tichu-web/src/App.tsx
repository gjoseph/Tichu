import { NotificationsProvider } from "@toolpad/core/useNotifications";
import React, { FC } from "react";
import { Room } from "./components/Room";
import { User } from "./model/User";
import { useStoredState } from "./react-utils";
import { createTheme, ThemeProvider } from "@mui/material/styles";

const theme = createTheme();

const App: FC<{ websocketUrl: string }> = (props) => {
  const [user, setUser, logOut] = useStoredState<User>("tichu.user");

  return (
    <div className="App">
      <ThemeProvider theme={theme}>
        <NotificationsProvider>
          {user ? (
            <>
              <div>
                Hello, {user.displayName}
                <button onClick={logOut}>logout</button>
              </div>
              <Room user={user} websocketUrl={props.websocketUrl} />
            </>
          ) : (
            <DummyUsernameInput setUser={setUser} />
          )}
        </NotificationsProvider>
      </ThemeProvider>
    </div>
  );
};

const DummyUsernameInput: FC<{
  setUser: (user: User) => void;
}> = ({ setUser }) => {
  const [userName, setUserName] = React.useState("");

  return (
    <form
      action=""
      onSubmit={(e) => {
        e.preventDefault();
        setUser(new User(userName, userName));
      }}
    >
      <label htmlFor="name">
        Please enter your name :
        <input
          type="text"
          id="name"
          placeholder="your name"
          value={userName}
          onChange={(e) => setUserName(e.target.value)}
        />
      </label>
      <input type="submit" value={"Send"} />
    </form>
  );
};
export default App;
