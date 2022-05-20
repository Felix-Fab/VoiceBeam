import Debug from "../Debug";

export default class Http{
    public static getAPIUrl() : string{
        switch(Debug.Test){
            case 'Local':
                return Debug.LocalAPIServer;

            default:
                return Debug.RemoteAPIServer;
        }
    }

    public static getWebSocketUrl(): string{
        switch(Debug.Test){
            case 'Local':
                return Debug.LocalWebSocketServer;

            default:
                return Debug.RemoteWebSocketServer;
        }
    }
}