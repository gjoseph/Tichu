import { program } from "commander";
import { newTerminalHandler } from "./term-handler";
import { WSTichuClient } from "tichu-client-ts-lib";

const url = "ws://localhost:8080/room/some-room-id";

interface GameOpts {
  room: string;
  user: string;
}

program
  .storeOptionsAsProperties(false)
  .option("-u, --user <userId>", "your name... until we figure auth out")
  .requiredOption(
    "-r, --room <roomId>",
    "the room id... until we figure out an API to list available rooms",
  );

program
  .parseAsync()
  .then((program) => program.opts() as GameOpts)
  .then((opts: GameOpts) => {
    console.log("Connecting to room ...", opts);
    return new WSTichuClient(opts.user, newTerminalHandler).connect(url);
  })
  .finally(() => {
    console.log("Done");
  });
