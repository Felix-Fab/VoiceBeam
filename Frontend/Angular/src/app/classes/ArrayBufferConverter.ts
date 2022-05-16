export default class ArrayBuffer{
    public static str2ab(text:string) {
        return new TextEncoder().encode(text);
    }

    public static ab2str(buf:any) {
        return new TextDecoder().decode(buf);
    }
}