package fr.n7.stl.block;


import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.impl.TAMFactoryImpl;
import fr.n7.stl.util.Logger;
import org.json.JSONException;
import org.json.JSONObject;

class Driver {

    public static void printUsage() {
        System.out.println("Usage       : java fr.n7.stl.block.Driver <file> <mode>");
        System.out.println(" <file>     : Input file");
        System.out.println(" <mode>     : 0 -> verbose mode");
        System.out.println("            : 1 -> only TAM code");
        System.out.println("            : 2 -> JSON: {TAM: string, code: string, resolve: boolean, checkType: boolean}");
    }

    public static void main(String[] args) throws Exception {
        // check
        if (args.length != 2) {
            printUsage();
            System.exit(0);
        }

        // needed data
        int mode = Integer.valueOf(args[1]);

        // computing
        Parser parser = new Parser(args[0]);
        parser.parse();
        try {
            parser = execute(parser);
        } catch (Exception exception) {
            Logger.error("Something wrong happened with Parser: " + exception);
            if (mode == 0) {
                exception.printStackTrace();
            }
        }

        switch (mode) {
            case 0:
                verbose(parser);
                break;

            case 1:
                tam(parser);
                break;

            case 2:
                json(parser);
                break;

            default:
                printUsage();
                System.exit(0);
        }
    }

    private static Parser execute(Parser parser) throws Exception {
        parser.resolve = parser.program.resolve(new SymbolTable());
        parser.checkType = parser.program.checkType();
        parser.program.allocateMemory(Register.SB, 0);
        parser.fragment = parser.program.getCode(new TAMFactoryImpl());
        return parser;
    }

    private static void verbose(Parser parser) {
        System.out.println("===============================================");
        System.out.println("content     : " + "\n" + parser.program.toString());
        System.out.println("resolve     : " + (parser.resolve ? "OK   " : "ERROR") + " (" + parser.resolveTimeMs + "ms)");
        System.out.println("checkType   : " + (parser.checkType ? "OK   " : "ERROR") + " (" + parser.checkTypeTimeMs + "ms)");
        System.out.println("Logger: " + Logger.getAll());
        System.out.println("===============================================");
        System.out.println("TAM         : ");
        System.out.println(parser.fragment == null ? "ERROR" : parser.fragment);
        System.out.println("===============================================");
    }

    private static void tam(Parser parser) {
        System.out.println(parser.fragment == null ? "" : parser.fragment);
    }

    private static void json(Parser parser) throws JSONException {
        //String tamCode = parser.fragment == null ? "" : parser.fragment.toString();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("grammarCheck", true);
        jsonObject.put("code", parser.program.toString());
        jsonObject.put("resolve", parser.resolve);
        jsonObject.put("checkType", parser.checkType);
        jsonObject.put("logger", Logger.getAll());
        jsonObject.put("TAM", "" + parser.fragment);
        System.out.print(jsonObject);
    }


}