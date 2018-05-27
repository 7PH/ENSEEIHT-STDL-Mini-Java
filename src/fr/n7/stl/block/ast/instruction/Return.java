package fr.n7.stl.block.ast.instruction;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node for a return instruction. */
public class Return implements Instruction {

    protected Expression value;

    public Return(Expression _value) {
        this.value = _value;
    }

    @Override
    public String toString() {
        return "return " + this.value + ";\n";
    }

    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        return value.resolve(scope);
    }

    @Override
    public boolean checkType() {
    	boolean b = value.getType() != AtomicType.ErrorType;
    	if (!b)
    		throw new RuntimeException("You return a ErrorType");
    	return b;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        return value.getType().length();
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        fragment.append(value.getCode(factory));
        fragment.add(factory.createReturn(value.getType().length(), value.getType().length()));
        return fragment;
    }

    @Override
    public Type getReturnType() {
        return value.getType();
    }

}