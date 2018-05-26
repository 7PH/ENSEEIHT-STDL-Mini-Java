package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractUse;
import fr.n7.stl.block.ast.object.ClassDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class ObjectUse extends AbstractUse {

    ClassDeclaration declaration;

    public ObjectUse(ClassDeclaration declaration) {
        super();

        this.declaration = declaration;
    }

    @Override
    protected Declaration getDeclaration() {
        return declaration;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        return null;
    }
}
