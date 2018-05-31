package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractAttribute;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.object.ClassDeclaration;
import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAccess extends AbstractAttribute implements Expression {

    public AttributeAccess(Expression object, String name) {
        super(object, name);
    }

    public AttributeAccess(String target, String name) {
        super(target, name);
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        ClassDeclaration cd = attributeDefinition.getParent();

        // add code of the object
        if (object == null) {
            // address is on target
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
        fragment.add(factory.createLoadI(getType().length()));

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
