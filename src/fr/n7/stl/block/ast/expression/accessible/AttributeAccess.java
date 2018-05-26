package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractAttribute;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAccess extends AbstractAttribute {

    public AttributeAccess(Expression object, String name) {
        super(object, name);
    }

    public AttributeAccess(String className, String name) {
        super(className, name);
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        return null;
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
