import WebSocket from "ws";
import inquirer from "inquirer";
import { Cards } from "./cards";
import { GameMessage, JoinParam, Message } from "./actions";
import { GameOpts } from "./startup";
import { Console } from "./console";

export class WSTichuClient {
    private console: Console;
    private _webSocket: WebSocket | undefined;

    constructor(readonly opts: GameOpts) {
        this.console = new Console();
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
        return "Done";
    }

    promptForCards = () => {
        inquirer
            .prompt([
                {
                    message:
                        "Pick cards with space, hit enter to play them. Just hit enter to pass.",
                    type: "checkbox",
                    name: "cards",
                    choices: Cards.sort(() => 0.5 - Math.random()).slice(0, 14),
                    pageSize: 20
                }
            ])
            .then((answers: any) => {
                console.log("answers:", answers);
                return "ok";
            });
    };

    private onConnect() {
        const joinParam = new JoinParam(this.opts.user, this.opts.team);
        const msg = new GameMessage(joinParam);
        this.send(msg);

        // on receive, set next prompt function

        // loop:
        // prompt for action, send we message
        this.promptForCards();
    }

    send = (msg: Message) => {
        this.ws().send(JSON.stringify(msg));
    };

    private receive(data: WebSocket.Data) {
        // cast will pbly fail
        this.console.print(
            Console.Types.Incoming,
            data as string,
            Console.Colors.Blue
        );
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

        const ws = new WebSocket(url); // TODO wtf are subprotocols

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
