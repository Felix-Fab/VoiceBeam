import Parameters from "../Parameters.js";

export default class Http{

    static getApiUrl() {
        
        switch(Parameters.Status){
            case "Local":
                return Parameters.LocalAPIUrl + Parameters.ApiPort;

            case "Remote":
                return Parameters.RemoteAPIUrl + Parameters.ApiPort;
        }
    }
}