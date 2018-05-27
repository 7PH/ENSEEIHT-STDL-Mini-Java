package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.expression.AbstractAttribute;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.object.AttributeDefinition;
import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAssignment extends AbstractAttribute implements AssignableExpression {
	
	private InstanceType objectType;
	
	private AttributeDefinition attribute;

    public AttributeAssignment(String target, String name) {
        super(target, name);
    }

    public AttributeAssignment(Expression object, String name) {
        super(object, name);
    }

    // @TODO getCode
	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        return fragment;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        return 0;
    }

    @Override
    public boolean checkType() {
        return false;
    }

    @Override
    public Type getReturnType() {
        return null;
    }
}
