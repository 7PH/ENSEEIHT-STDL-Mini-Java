package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractUse;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class ParameterUse extends AbstractUse {

    public Expression value;

    private ParameterDeclaration parameterDeclaration;

    /**
     * Creates a variable use expression Abstract Syntax Tree node.
     * @param parameterDeclaration Name of the used variable.
     */
    ParameterUse(ParameterDeclaration parameterDeclaration, Expression value) {
        this.parameterDeclaration = parameterDeclaration;
        this.value = value;
    }

    @Override
    protected Declaration getDeclaration() {
        return parameterDeclaration;
    }

    @Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        fragment.add(factory.createLoad(
                Register.SB,
                parameterDeclaration.getFunctionDeclaration().getOffset(),
                parameterDeclaration.getType().length()));
        return fragment;
    }
}
