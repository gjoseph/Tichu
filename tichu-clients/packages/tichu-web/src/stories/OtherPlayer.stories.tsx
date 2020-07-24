import React from "react";
import { OtherPlayer } from "../components/OtherPlayer";
import { User } from "../model/User";

export default {
  title: "Other Player",
  component: OtherPlayer,
};

export const With3Cards = () => (
  <OtherPlayer user={new User("1", "Alex Jules")} handCardCount={3} />
);
