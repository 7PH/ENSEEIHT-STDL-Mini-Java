package fr.n7.stl.block;


import org.json.JSONException;
import org.json.JSONObject;

class Driver {

    public static void pringUsage() {
        System.out.println("Usage       : java fr.n7.stl.block.Driver <file> <mode>");
        System.out.println(" <file>     : Input file");
        System.out.println(" <mode>     : 0 -> verbose mode");
        System.out.println("            : 1 -> only TAM code");
        System.out.println("            : 2 -> JSON: {TAM: string, code: string, resolve: boolean, checkType: boolean}");
    }

	public static void main(String[] args) throws Exception {
		// check
        if (args.length != 2) {
		    pringUsage();
            System.exit(0);
        }

        // needed data
        String file;
        int mode;

        // parsing input
        file = args[0];
        mode = Integer.valueOf(args[1]);

        // computing
        Parser parser = new Parser(file);
		parser.parse();

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
                pringUsage();
                System.exit(0);
        }
    }

    private static void verbose(Parser parser) {
        System.out.println("===============================================");
        System.out.println("content     : " + parser.block.toString());
        System.out.println("name        : " + parser.name);
        System.out.println("resolve     : " + (parser.resolve ? "OK   " : "ERROR") + " (" + parser.resolveTimeMs + "ms)");
        System.out.println("getType     : " + (parser.checkType ? "OK   " : "ERROR") + " (" + parser.checkTypeTimeMs + "ms)");
        System.out.println("===============================================");
        System.out.println("TAM         : ");
        System.out.println(parser.fragment == null ? "ERROR" : parser.fragment);
        System.out.println("===============================================");
    }

    private static void tam(Parser parser) {
        System.out.println(parser.fragment == null ? "" : parser.fragment);
    }

    private static void json(Parser parser) throws JSONException {
        String tamCode = parser.fragment == null ? "" : parser.fragment.toString();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TAM", tamCode);
        jsonObject.put("code", parser.block.toString());
        jsonObject.put("resolve", parser.resolve);
        jsonObject.put("checkType", parser.checkType);
        System.out.print(jsonObject);
    }


}