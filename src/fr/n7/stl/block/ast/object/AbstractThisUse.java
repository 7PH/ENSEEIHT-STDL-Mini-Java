package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Register;

public class AbstractThisUse extends VariableDeclaration implements Expression {

    public final ProgramDeclaration programDeclaration;

    public AbstractThisUse(ProgramDeclaration programDeclaration) {
        super("this", new InstanceType(programDeclaration.getClassName().getName()), null);
        this.programDeclaration = programDeclaration;
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

    @Override
    public Register getRegister() {
        return Register.LB;
    }

    @Override
    public int getOffset() {
        return 3;
    }
}
