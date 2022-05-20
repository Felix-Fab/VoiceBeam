export default class ArrayBufferConverter{
    public static str2ab(text:string) : ArrayBuffer {
        return new TextEncoder().encode(text);
    }

    public static ab2str(buf:any) : string {
        return new TextDecoder().decode(buf);
    }
}