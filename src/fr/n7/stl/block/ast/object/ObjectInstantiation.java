package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class ObjectInstantiation implements Expression {

    private Type type;

    private List<Expression> parameters;

    public ObjectInstantiation(Type type, List<Expression> parameters) {
        this.type = type;
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
    public Fragment getCode(TAMFactory factory) {
        return null;
    }
}