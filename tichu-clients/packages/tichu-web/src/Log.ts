export class Log {
  constructor(readonly isDebug: boolean) {}

  error(...msg: any) {
    console.error(...msg);
  }

  debug(...msg: any) {
    if (this.isDebug) {
      console.debug(...msg);
    }
  }
}
