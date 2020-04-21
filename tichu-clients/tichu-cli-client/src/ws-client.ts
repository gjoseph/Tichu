import WebSocket from "ws";
import inquirer from "inquirer";
import { Cards } from "./cards";
import {
    IncomingChatMessage,
    IncomingGameMessage,
    IncomingMessage,
    JoinParam,
    OutgoingChatMessage,
    OutgoingGameMessage,
    OutgoingMessage,
    PlayerIsReadyParam,
    PlayerPlaysParam
} from "./messages";
import { GameOpts } from "./startup";
import { Console } from "./console";

export class WSTichuClient {
    private _webSocket: WebSocket | undefined;
    private console: Console;
    private nextPrompt: (() => Promise<OutgoingMessage>) | undefined;

    constructor(readonly opts: GameOpts) {
        this.console = new Console();
        this.nextPrompt = undefined;
    }

    connect(url: string) {
        this._webSocket = this.webSocketSetup(url, this.console);
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

    ask = (question: any) => {
        // question: DistinctQuestion) => {
        return inquirer.prompt([question]);
    };

    promptForCards = () => {
        const question = {
            message: "Pick cards", // with space, hit enter to play them. Just hit enter to pass.",
            type: "checkbox",
            name: "cards",
            choices: Cards.sort(() => 0.5 - Math.random()).slice(0, 14),
            pageSize: 20
        };
        return this.ask(question).then((answers: any) => {
            return new OutgoingGameMessage(new PlayerPlaysParam(answers.cards));
        });
    };

    promptForJoin = () => {
        return this.ask({
            type: "confirm",
            name: "join",
            message: "Join table?"
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
            message: "Are you ready?"
        }).then((answers: any) => {
            if (answers.ready) {
                return new OutgoingGameMessage(new PlayerIsReadyParam());
            } else {
                return new OutgoingChatMessage("Not ready");
            }
        });
    };

    private onConnect() {
        // TODO nextPrompt=join if game already exists in room, otherwise setup game
        this.nextPrompt = this.promptForJoin;
    }

    send = (msg: OutgoingMessage) => {
        const msgJson = JSON.stringify(msg);
        this.console.debug(" Sending", msgJson);
        this.ws().send(msgJson);
    };

    private receive(data: WebSocket.Data) {
        // there's gotta be a better way than just casting to string
        const res = JSON.parse(data as string) as IncomingMessage;
        this.console.debug("Received", res);

        if (res.messageType === "chat") {
            this.handleChatMessage(res as IncomingChatMessage);
        } else if (res.messageType === "game") {
            this.handleGameMessage(res as IncomingGameMessage);
        } else {
            throw new Error("Unknown message type: " + res.messageType);
        }

        if (this.nextPrompt) {
            this.nextPrompt().then(this.send);
        }
    }

    private handleChatMessage(res: IncomingChatMessage) {
        this.console.print(
            Console.Types.Incoming,
            res.content,
            Console.Colors.Blue
        );
    }

    private handleGameMessage(msg: IncomingGameMessage) {
        // set nextPrompt depending on received message
        // unclear what happens with prompts if we receive multiple messages. Probably fucks us over
        console.log("msg.forAction:", msg.forAction);
        if (msg.forAction === "join" && msg.result === "ok") {
            this.nextPrompt = this.promptForReadiness;
        }
        // TODO etc...
    }

    close = () => {
        this.ws().close();
    };

    private ws(): WebSocket {
        if (!this._webSocket) {
            throw new Error("Websocket is not setup!");
        }
        return this._webSocket;
    }

    private webSocketSetup(url: string, wsConsole: Console): WebSocket {
        const noop = () => {};

        // for now, pass==user -- we'll want to get rid of basic auth -- https://auth0.com/docs/integrations/using-auth0-to-secure-a-cli
        const userPass = `${this.opts.user}:${this.opts.user}`;
        const authHeaderValue =
            "Basic " + Buffer.from(userPass).toString("base64");
        const ws = new WebSocket(url, {
            headers: {
                Authorization: authHeaderValue
            }
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

        ws.on("error", err => {
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
