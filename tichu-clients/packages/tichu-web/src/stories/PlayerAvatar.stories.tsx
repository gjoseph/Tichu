import React from "react";
import { PlayerAvatar } from "../components/PlayerAvatar";
import { User } from "../model/User";
import { Args, Meta } from "@storybook/react";
import { disableControls, makeStory } from "./stories";

export default {
  title: "Player Avatar",
  component: PlayerAvatar,
  parameters: disableControls,
} as Meta;

const cq = new User("abc-def", "Charlie Quinn");
export const AvatarsOrInitials = makeStory((args: Args) => (
  <>
    Known avatar:
    <PlayerAvatar user={new User("abc-def", "Alex", "/cat.jpg")} />
    Fallback to initials:
    <PlayerAvatar user={new User("abc-def", "Charlie Quinn")} />
    Fallback to default avatar if no name:
    <PlayerAvatar user={new User("abc-def")} />
  </>
));

export const OnlineOffline = makeStory((args: Args) => {
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
});

export const Busy = makeStory((args: Args) => {
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
});

export const NameDependentBackgroundColor = makeStory((args: Args) => (
  <>
    <div>
      Alex Quinn: <PlayerAvatar user={new User("abc-def", "Alex Quinn")} />
    </div>
    <div>
      Charlie Jules:{" "}
      <PlayerAvatar user={new User("abc-def", "Charlie Jules")} />
    </div>
    <div>
      Arnold Quartzenegger // same initials but different name, should be
      different color:
      <PlayerAvatar user={new User("abc-def", "Arnold Quartzenegger")} />
    </div>
    <div>
      Charlie Jules // is consistent if same name
      <PlayerAvatar user={new User("abc-def", "Charlie Jules")} />
    </div>
    <div>
      Fallback to grey if no name:
      <PlayerAvatar user={new User("abc-def")} />
    </div>
  </>
));
