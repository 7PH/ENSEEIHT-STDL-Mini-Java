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
        return super.resolve(scope);
    }

    @Override
    public boolean checkType() {
        boolean ok = true;
        if (this.getDefinitionModifier() != null) {
            Logger.error("Constructor " + this.getName() + " cannot have a static/final modifier");
            ok = false;
        }
        ok &= body.checkType();
        return ok;
    }
}
