
public class Logger {
    static Boolean show = true;
    static Boolean show_err = true;
    static Boolean show_suc = true;
    static Boolean show_info = true;
    static Boolean show_any = true;
    static Boolean show_abort_send = true;
    static Boolean show_request= true;

    public static void ERR(String className, String err){
        if (show && show_err) System.err.println("[ ERR     ] " + className + " - " + err);
    }
    public static void SUC(String className, String message){
        if (show && show_suc) System.err.println("[     SUC ] " + className + " - " + message);
    }

    public static void INFO(String className, String message){
        if (show && show_info) System.err.println("[  INFO   ] " + className + " - " + message);
    }

    public static void ANY(String className, String message){
        if (show && show_any) System.err.println("[=========] " + className + " - " + message);
    }

    public static void ABORT_SEND(String className, String message){
        if (show && show_abort_send) System.err.println("[ ABORT   ] " + className + " - " + message);
    }

    public static void REQUEST(String className, String message){
        if (show && show_request) System.err.println("[ REQUEST ] " + className + " - " + message);
    }

}
