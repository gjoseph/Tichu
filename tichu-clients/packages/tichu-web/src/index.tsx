import React from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App";

const container = document.getElementById("root");
const root = createRoot(container!);
root.render(
  <React.StrictMode>
    <App websocketUrl={"ws://localhost:8080/room/some-room-id"} />
  </React.StrictMode>,
);

// TODO do we need service workers, perhaps for offline more? Removed this when removing CRA:
// https://bit.ly/CRA-PWA
