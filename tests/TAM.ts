import * as child_process from "child_process";
import * as fs from "fs";


export interface TAMExpectedResult {
    resolve: boolean;
    checkType: boolean;
    output?: string[];
}

export interface TAMResult {
    resolve: boolean;
    checkType: boolean;
    code: string;
    TAM: string;
    output?: string[];
}

export class TAM {

    public static parse(fileName: string): TAMResult {
        const shellResult: Buffer = child_process.execSync("./launch.sh " + fileName + " 2");
        return JSON.parse(shellResult.toString());
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
        if (parsed.resolve && parsed.checkType)
            parsed.output = TAM.executeTam(parsed.TAM);
        return parsed;
    }

    public static ensureResult(code: string, expected: TAMExpectedResult): void {
        const fileName: string = TAM.storeCodeInTmp(code);
        const result: TAMResult = TAM.parseAndExecute(fileName);

        if (expected.resolve !== result.resolve)
            throw new Error("Resolve expected to be " + expected.resolve + " but is " + result.resolve);

        if (expected.checkType !== result.checkType)
            throw new Error("CheckType expected to be " + expected.checkType + " but is " + result.checkType);

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

    private static storeCodeInTmp(code: string) {
        const program: string = `
        
        test_program {
            ${code}
        }
        
        `;
        fs.writeFileSync('tmp.bc', program);
        return 'tmp.bc';
    }
}