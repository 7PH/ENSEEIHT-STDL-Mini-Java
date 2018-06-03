package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.expression.AbstractAttribute;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.object.AttributeDefinition;
import fr.n7.stl.block.ast.object.ClassDeclaration;
import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAssignment extends AbstractAttribute implements AssignableExpression {
	
    public AttributeAssignment(String target, String name) {
        super(target, name);
    }

    public AttributeAssignment(Expression object, String name) {
        super(object, name);
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        ClassDeclaration cd = attributeDefinition.getParent();

        if (attributeDefinition.isStatic()) {
            fragment.add(factory.createStore(attributeDefinition.getRegister(), attributeDefinition.getOffset(), attributeDefinition.getType().length()));
            return fragment;
        }

        // add code of the object
        if (object == null) {
            fragment.add(factory.createLoad(declaration.getRegister(), declaration.getOffset(), InstanceType.OBJECT_ADDR_LENGTH));
        } else {
            fragment.append(object.getCode(factory));
        }

        // at this point we have the object addr?
        // we know the relative offset of the object
        int attrOffset = cd.getAttributeAbsoluteOffset(attributeDefinition);
        fragment.add(factory.createLoadL(attrOffset));
        fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));

        // the addr is there, we load the value
        fragment.add(factory.createStoreI(getType().length()));

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
