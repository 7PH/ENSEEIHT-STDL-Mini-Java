package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractAttribute;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.object.ClassDeclaration;
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
        // absolute offset in class
        int offset = cd.getAttributeAbsoluteOffset(attributeDefinition);


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
