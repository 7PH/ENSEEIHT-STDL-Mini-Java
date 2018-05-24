package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.util.Logger;

public class Constructor extends MethodDefinition {

    public Constructor(String className, Block body) {
        super(new Signature(new TypeInstantiation(className), ""), body);
    }

    @Override
    public boolean checkType() {
        boolean b = true;
        if (this.getDefinitionModifier() != null) {
            Logger.error("Constructor " + this.getName() + " has a definition modifier.");
        }
        b &= this.body.checkType();
        if (!b) {
            Logger.error("Body of Constructor " + this.getName() + this.signature.getParameters().toString() + " is not well typed.");
        }
        for (ParameterDeclaration p : this.signature.getParameters()) {
            b &= !(p.getType().equalsTo(AtomicType.ErrorType));
            if (!b) {
                Logger.error("Parameter : " + p.toString() + " (in Constructor " + this.getName() + ") is considered as ErrorType");
            }
        }
        return b;
    }
}
