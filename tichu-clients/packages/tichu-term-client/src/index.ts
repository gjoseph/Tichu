import inquirer from "inquirer";
import { program } from "commander";
import { GameOpts, setupQuestions } from "./startup";
import { newTerminalHandler } from "./term-handler";
import { Status, WSTichuClient } from "tichu-client-ts-lib";

const url = "ws://localhost:8080/room/some-room-id";

program
  .storeOptionsAsProperties(false)
  .option("-u, --user <userId>", "your name... until we figure auth out")
  .requiredOption(
    "-r, --room <roomId>",
    "the room id... until we figure out an API to list available rooms"
  );

program
  .parseAsync()
  .then((program) => program.opts() as GameOpts)
  .then((opts: GameOpts) => {
    return inquirer.prompt(setupQuestions(opts)).then((answers: any) => {
      // Combine CLI opts and answers
      return {
        room: answers.roomId ?? opts.room,
        user: answers.userId ?? opts.user,
      } as GameOpts;
    });
  })
  .then((opts: GameOpts) => {
    console.log("Connecting to room ...", opts);
    return new WSTichuClient(opts.user, newTerminalHandler)
      .connect(url)
      .waitUntilDone();
  })
  .then((status: Status) => {
    console.log("Status:", status);
  })
  .finally(() => {
    console.log("Done");
  });
