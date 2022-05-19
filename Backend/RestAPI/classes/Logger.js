export default class Logger{
    
    static writeSuccess(prefix,message){
        console.log(`${prefix}Success | ${message}`.green);
    }

    static writeWarning(prefix,message){
        console.log(`${prefix}Warning | ${message}`.yellow);
    }

    static writeError(prefix,message){
        console.log(`${prefix}Error | ${message}`.red);
    }

    static writeInfo(prefix,message){
        console.log(`${prefix}Info | ${message}`.gray);
    }

    static writeServerLog(prefix,message){
        console.log(`${prefix}Server | ${message}`.blue);
    }
}