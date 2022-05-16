import Debug from "../Debug";

export default class Http{
    public static getServerUrl(){
        if(Debug.Test == 'Local'){
            return Debug.LocalServer;
        }else{
            return Debug.RemoteServer;
        }
    }
}