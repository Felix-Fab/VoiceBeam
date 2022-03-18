package tk.felixfab.voicebeam.etc;

public class Logger {

    public static void writeInfoMessage(String message){
        System.out.println(ConsoleColors.RESET + "[Info] " + message);
    }

    public static void writeWarningMessage(String message){
        System.out.println(ConsoleColors.RESET + "[" + ConsoleColors.YELLOW + "Warning" + ConsoleColors.RESET + "] " + message);
    }

    public static void writeErrorMessage(String message){
        System.out.println(ConsoleColors.RESET + "[" + ConsoleColors.RED + "Error" + ConsoleColors.RESET + "] " + ConsoleColors.RED_BACKGROUND + message + ConsoleColors.RESET);
    }

    public static void writeSuccessMessage(String message){
        System.out.println(ConsoleColors.RESET + "[" + ConsoleColors.GREEN + "Success" + ConsoleColors.RESET + "] " + message);
    }
}