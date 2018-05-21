package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class MethodCall implements Instruction, Expression {

    private String identifier;

    private Expression object;

    private String method;

    private List<Expression> parameters;

    public MethodCall(Expression object, String method, List<Expression> parameters) {
        this.object = object;
        this.method = method;
        this.parameters = parameters;
    }

    public MethodCall(String identifier, String method, List<Expression> parameters) {
        this.identifier = identifier;
        this.method = method;
        this.parameters = parameters;
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        return false;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean checkType() {
        return false;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        return 0;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        return null;
    }

    @Override
    public Type getReturnType() {
        return null;
    }

    @Override
    public String toString() {
        return object + "." + method + "(" + parameters + ");";
    }
}