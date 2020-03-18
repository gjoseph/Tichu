export class WSTichuClient {
    connect() {
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
}

export type Status = "Done" | "Not done";
