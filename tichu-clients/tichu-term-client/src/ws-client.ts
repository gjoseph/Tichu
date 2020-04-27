import WebSocket from "ws";
import inquirer from "inquirer";
import { Cards } from "./cards";
import {
    ActivityMessage,
    ErrorMessage,
    IncomingChatMessage,
    IncomingGameMessage,
    IncomingMessage,
    JoinParam,
    OutgoingChatMessage,
    OutgoingGameMessage,
    OutgoingMessage,
    PlayerIsReadyParam,
    PlayerPlaysParam,
} from "./messages";
import { GameOpts } from "./startup";
import { Console } from "./console";
import PromptUI from "inquirer/lib/ui/prompt";

export class WSTichuClient {
    private readonly console: Console;
    private webSocket: WebSocket | undefined;
    private nextPrompt: (() => Promise<OutgoingMessage>) | undefined;
    private currentPromptUi: PromptUI | undefined;
    private waitingForAnswer: string[];

    constructor(readonly opts: GameOpts) {
        this.console = new Console();
        this.nextPrompt = undefined;
        this.currentPromptUi = undefined;
        this.waitingForAnswer = [];
    }

    connect(url: string) {
        this.webSocket = this.webSocketSetup(url, this.console);
        return this;
    }

    // After connection, only actions should be play or pass
    // and its maybe just a single question that lists cards and a special key/option to pass
    // .. or just, if no card selected > pass
    // and a confirmation
    //
    // https://www.npmjs.com/package/read has a timeout function which could also be interesting
    // https://www.npmjs.com/package/https-proxy-agent could be needed as well

    waitUntilDone(): Status {
        // while (true) {
        //     console.log("...next question...");
        //     this.prompt()
        //     console.log("....thanks.,,,")
        // }
        return "Done";
    }

    ask = <T>(question: any): Promise<T> => {
        // question: DistinctQuestion) => {
        this.closeCurrentPrompt();
        const promptPromiseAndUi = inquirer.prompt([question]);
        // inquirer.prompt() actuall returns Promise<T> & {ui: PromptUI} so we keep track of it to be able to close it
        this.currentPromptUi = promptPromiseAndUi.ui;
        return promptPromiseAndUi as Promise<T>;
    };

    private closeCurrentPrompt() {
        // can't use ?. operator below because of the @ts-ignore statement ...
        if (this.currentPromptUi) {
            // @ts-ignore : close() is protected but we really want to call it ...
            this.currentPromptUi.close();
        }
    }

    promptForJoin = () => {
        return this.ask({
            type: "confirm",
            name: "join",
            message: "Join table?",
        }).then((answers: any) => {
            if (answers.join) {
                return new OutgoingGameMessage(new JoinParam(this.opts.team));
            } else {
                return new OutgoingChatMessage("Not joining");
            }
        });
    };

    promptForReadiness = () => {
        return this.ask({
            type: "confirm",
            name: "ready",
            message: "Are you ready?",
        }).then((answers: any) => {
            if (answers.ready) {
                return new OutgoingGameMessage(new PlayerIsReadyParam());
            } else {
                return new OutgoingChatMessage("Not ready");
            }
        });
    };

    promptForCards = () => {
        const cards = Cards.sort(() => 0.5 - Math.random()).slice(0, 14);
        const choices = cards.map((c) => {
            return { value: c.shortName, name: c.name };
        });
        const question = {
            message: "Pick cards", // with space, hit enter to play them. Just hit enter to pass.",
            type: "checkbox",
            name: "cards",
            choices: choices,
            pageSize: 20,
        };
        return this.ask(question).then((answers: any) => {
            return new OutgoingGameMessage(new PlayerPlaysParam(answers.cards));
        });
    };

    private onConnect() {
        // TODO nextPrompt=join if game already exists in room, otherwise setup game
        this.nextPrompt = this.promptForJoin;
    }

    send = (msg: OutgoingMessage) => {
        this.waitingForAnswer.push(msg.txId); // Do we care for chat messages?
        const msgJson = JSON.stringify(msg);
        this.console.debug(" Sending", msgJson);
        this.ws().send(msgJson);
    };

    private receive(data: WebSocket.Data) {
        this.closeCurrentPrompt();

        // there's gotta be a better way than just casting to string
        const msg = JSON.parse(data as string) as IncomingMessage;
        this.console.debug("Received", msg);

        if (msg.messageType === "chat") {
            this.handleChatMessage(msg as IncomingChatMessage);
        } else if (msg.messageType === "game") {
            this.handleGameMessage(msg as IncomingGameMessage);
        } else if (msg.messageType === "activity") {
            this.handleActivityMessage(msg as ActivityMessage);
        } else if (msg.messageType === "error") {
            this.handleErrorMessage(msg as ErrorMessage);
        } else {
            throw new Error("Unknown message type: " + msg.messageType);
        }

        if (this.nextPrompt) {
            this.nextPrompt().then(this.send);
        }
    }

    private handleChatMessage(msg: IncomingChatMessage) {
        this.console.print(
            Console.Types.Incoming,
            `${msg.from}: ${msg.content}`,
            Console.Colors.Blue
        );
    }

    private handleErrorMessage(msg: ErrorMessage) {
        this.console.print(
            Console.Types.Incoming,
            `Error caused by ${msg.actor} - contact us with this reference: ${msg.traceId} (txId: ${msg.txId})`,
            Console.Colors.Red
        );
    }

    private handleActivityMessage(msg: ActivityMessage) {
        this.console.print(
            Console.Types.Incoming,
            `${msg.actor} ${msg.activity}`,
            Console.Colors.Green
        );
    }

    private handleGameMessage(msg: IncomingGameMessage) {
        // do we care for all messages ?
        const idxCorrespondingRequest = this.waitingForAnswer.indexOf(msg.txId);
        const isResponse = idxCorrespondingRequest >= 0;
        if (isResponse) {
            console.log(
                "Removing message",
                msg.txId,
                "from queue ",
                this.waitingForAnswer
            );
            this.waitingForAnswer.splice(idxCorrespondingRequest);
        }

        // set nextPrompt depending on received message
        // or leave it as-is
        // receive() will reapply nextPrompt
        switch (msg.forAction) {
            case "init":
                break;
            case "join":
                switch (msg.result) {
                    case "ok":
                        if (isResponse) {
                            this.console.debug("Waiting for others");
                            this.nextPrompt = undefined;
                        }
                        break;
                    case "ok-table-is-now-full":
                        this.nextPrompt = this.promptForReadiness;
                        break;
                }
                break;
            case "ready":
                switch (msg.result) {
                    case "ok":
                        if (isResponse) {
                            this.console.debug(
                                "Waiting for others to be ready"
                            );
                            this.nextPrompt = undefined;
                        }
                        break;
                    case "ok-started":
                        this.nextPrompt = this.promptForCards;
                        break;
                }
                break;
            case "newTrick":
                break;
            case "play":
                break;
            default:
                throw new Error("Unknown action: " + msg.forAction);
        }

        // TODO etc...
    }

    close = () => {
        this.ws().close();
    };

    private ws(): WebSocket {
        if (!this.webSocket) {
            throw new Error("Websocket is not setup!");
        }
        return this.webSocket;
    }

    private webSocketSetup(url: string, wsConsole: Console): WebSocket {
        const noop = () => {};

        // for now, pass==user -- we'll want to get rid of basic auth -- https://auth0.com/docs/integrations/using-auth0-to-secure-a-cli
        const userPass = `${this.opts.user}:${this.opts.user}`;
        const authHeaderValue =
            "Basic " + Buffer.from(userPass).toString("base64");
        const ws = new WebSocket(url, {
            headers: {
                Authorization: authHeaderValue,
            },
        }); // TODO wtf are subprotocols

        ws.on("open", () => {
            wsConsole.print(
                Console.Types.Control,
                "Connected (press CTRL+C to quit)",
                Console.Colors.Green
            );
            this.onConnect();
        });

        ws.on("close", (code, reason) => {
            wsConsole.print(
                Console.Types.Control,
                `Disconnected (code: ${code}, reason: "${reason}")`,
                Console.Colors.Green
            );
            wsConsole.clear();
            process.exit();
        });

        ws.on("error", (err) => {
            wsConsole.print(
                Console.Types.Error,
                err.message,
                Console.Colors.Yellow
            );
            process.exit(-1);
        });

        ws.on("message", (data: WebSocket.Data) => {
            this.receive(data);
        });

        ws.on("ping", () => {
            wsConsole.print(
                Console.Types.Incoming,
                "Received ping",
                Console.Colors.Blue
            );
        });

        ws.on("pong", () => {
            wsConsole.print(
                Console.Types.Incoming,
                "Received pong",
                Console.Colors.Blue
            );
        });

        return ws;
    }
}

export type Status = "Done" | "Not done";
