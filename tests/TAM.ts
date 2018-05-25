import * as child_process from "child_process";
import * as fs from "fs";
import {log} from "util";


export interface TAMExpectedResult {
    resolve: boolean;
    checkType: boolean;
    output?: string[];
}

export interface TAMResult {
    grammarCheck: boolean;
    resolve: boolean;
    checkType: boolean;
    code: string;
    TAM: string;
    logger: string;
    output?: string[];
}

export class TAM {

    public static parse(fileName: string): TAMResult {
        let d: TAMResult;
        try {
            const shellResult: Buffer = child_process.execSync("sh launch.sh " + fileName + " 2");
            d = JSON.parse(shellResult.toString());
        } catch (e) {
            d = {
                grammarCheck: false,
                resolve: false,
                checkType: false,
                code: "",
                TAM: "",
                logger: "Unable to parse data"
            };
        }
        return d;
    }

    public static executeTam(tam: string): string[] {
        fs.writeFileSync('tmp.tam', tam);
        child_process.execSync("java -jar tools/aspartam.jar tmp.tam >/dev/null 2>&1");
        child_process.execSync("java -jar tools/tammachine.jar tmp.tamo > tmp.res 2>&1");
        let r: string = fs.readFileSync("tmp.res")
            .toString()
            .replace(/Execution de (.+)\n/, "")
            .trim();
        return r.split("\n");
    }

    public static parseAndExecute(fileName: string): TAMResult {
        let parsed: TAMResult = TAM.parse(fileName);
        //if (parsed.resolve && parsed.checkType)
        //    parsed.output = TAM.executeTam(parsed.TAM);
        return parsed;
    }

    public static ensureResult(code: string, expected: TAMExpectedResult): void {
        const fileName: string = TAM.storeCodeInTmp(code);
        const result: TAMResult = TAM.parseAndExecute(fileName);
        const logger: string = result.logger.trim();

        //if (logger.length > 0)
        //    throw new Error("Error: " + logger);

        if (expected.resolve !== result.resolve)
            throw new Error("Resolve expected to be " + expected.resolve + " but is " + result.resolve + " \n " + logger);

        if (expected.checkType !== result.checkType)
            throw new Error("CheckType expected to be " + expected.checkType + " but is " + result.checkType + " \n " + logger);

        if (expected.output != null && result.output == null)
            throw new Error("Output expected to be " + expected.output + " but is null");

        if (expected.output != null) {
            if (result.output == null)
                throw new Error("Output expected to be " + expected.output + " but is null");
            if (result.output.length != expected.output.length)
                throw new Error("Output expected to be " + expected.output + " but is " + result.output);

            for (let i in expected.output) {
                if (expected.output[i] != result.output[i]) {
                    throw new Error("Output expected to be " + expected.output + " but is " + result.output);
                }
            }
        }

        return;
    }

    public static storeCodeInTmp(code: string) {
        fs.writeFileSync('tmp.mjava', code);
        return 'tmp.mjava';
    }
}