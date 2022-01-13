export class BrowserWSWrapper extends WebSocket {
  isConnected = () => {
    return this.readyState === WebSocket.OPEN;
  };
}
