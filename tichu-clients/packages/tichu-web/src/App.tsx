import React from "react";
import "./App.css";
import { AllCards } from "tichu-client-ts-lib";
import { CardSet } from "./components/CardSet";

function App() {
  return (
    <div className="App">
      <CardSet cards={AllCards} />;
    </div>
  );
}

export default App;
