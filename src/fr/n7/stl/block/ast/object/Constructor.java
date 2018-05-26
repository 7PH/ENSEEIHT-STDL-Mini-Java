package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.util.Logger;

import java.util.List;

public class Constructor extends MethodDefinition {

    public Constructor(String className, List<ParameterDeclaration> parameters, Block body) {
        super(new Signature(new InstanceType(className), "", parameters), body);
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        if (! super.resolve(scope))
            return false;

        return true;
    }

    @Override
    public boolean checkType() {
        boolean ok = true;

        if (this.getDefinitionModifier() != null) {
            Logger.error("Constructor " + this.getName() + " cannot have a static/final modifier");
            ok = false;
        }

        // @TODO delete this note
        // no need to display a second error.
        // if an instruction within the body is not well typed,
        // it will show an error in the Logger. no need to display it twice here.
        // so i've deleted the 'if' block there
        // {deleted code}
        ok &= body.checkType();

        // @TODO delete this note
        // no need to check parameters in MethodDefinition and here
        // factorized in MethodDefinition
        // {deleted code}

        return ok;
    }
}
