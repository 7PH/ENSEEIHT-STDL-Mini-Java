package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;

public class AbstractThisUse extends VariableDeclaration implements Expression {

    public AbstractThisUse(ProgramDeclaration programDeclaration) {
        super("this", new InstanceType(programDeclaration.getClassName().getName()), null);
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        if (! scope.accepts(this)) return false;
        if (! type.resolve(scope)) return false;
        scope.register(this);
        return true;
    }

    @Override
    public String getName() {
        return "this";
    }

    @Override
    public Type getType() {
        return type;
    }
}
