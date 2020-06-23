import React from "react";
import logo from "./logo.svg";
import "./App.css";
import { ReactComponent as SampleCard } from "./sample_card.svg";
import { Card, cardFromName } from "tichu-client-ts-lib";
import { CardView } from "./components/CardView";

const CardComp = () => {
  const card: Card = cardFromName("*P");
  console.log(card);
  return <CardView />;
};

function App() {
  return (
    <div className="App">
      <CardComp />
    </div>
  );
}

export default App;
