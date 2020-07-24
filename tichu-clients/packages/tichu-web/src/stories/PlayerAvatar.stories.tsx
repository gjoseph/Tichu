import React from "react";
import { PlayerAvatar } from "../components/PlayerAvatar";
import { User } from "../model/User";

export default {
  title: "Player Avatar",
  component: PlayerAvatar,
};

const cq = new User("abc-def", "Charlie Quinn");
export const AvatarsOrInitials = () => (
  <>
    Known avatar:
    <PlayerAvatar user={new User("abc-def", "Alex", "/cat.jpg")} />
    Fallback to initials:
    <PlayerAvatar user={new User("abc-def", "Charlie Quinn")} />
    Fallback to default avatar if no name:
    <PlayerAvatar user={new User("abc-def")} />
  </>
);

export const OnlineOffline = () => {
  return (
    <>
      Unspecified:
      <PlayerAvatar user={cq} />
      Offline:
      <PlayerAvatar user={cq} presence="offline" />
      Online:
      <PlayerAvatar user={cq} presence="online" />
    </>
  );
};

export const Busy = () => {
  return (
    <>
      Offline busy:
      <PlayerAvatar user={cq} presence="offline" busy />
      Online busy:
      <PlayerAvatar user={cq} presence="online" busy={true} />
      Offline, business unspecified:
      <PlayerAvatar user={cq} presence="offline" />
      Online explicitly not busy:
      <PlayerAvatar user={cq} presence="online" busy={false} />
    </>
  );
};
