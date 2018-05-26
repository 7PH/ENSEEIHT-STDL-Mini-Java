package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractAttribute;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAccess extends AbstractAttribute implements Expression {
    public AttributeAccess(Expression object, String name) {
        super(object, name);
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        return null;
    }
}
