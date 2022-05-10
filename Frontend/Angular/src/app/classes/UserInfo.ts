export default class UserInfo{
    private static username: string;
    private static email: string;
    private static accessToken: string;

    public static getUsername() : string{
        return this.username;
    }

    public static setUsername(username: string){
        this.username = username;
    }

    public static getEmail() : string{
        return this.email;
    }

    public static setEmail(email: string){
        this.email = email;
    }

    public static getAccessToken() : string{
        return this.accessToken;
    }

    public static setAccessToken(accessToken : string){
        this.accessToken = accessToken;
    }
}