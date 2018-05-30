package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractUse;
import fr.n7.stl.block.ast.instruction.declaration.ConstantDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node for a constant use expression. */
public class ConstantUse extends AbstractUse {
	
	protected ConstantDeclaration declaration;
	
	/**
	 * Creates a variable use expression ABSTRACT Syntax Tree node.
	 * @param declaration Name of the used variable.
	 */
	public ConstantUse(ConstantDeclaration declaration) {
		this.declaration = declaration;
	}

	@Override
	protected Declaration getDeclaration() {
		return declaration;
	}

	/* (non-Javadoc)
     * @see fr.n7.stl.block.ast.expression.AbstractUse#getCode(fr.n7.stl.tam.ast.TAMFactory)
     */
	public Fragment getCode(TAMFactory factory) {
        Fragment _result = factory.createFragment();
        _result.add(factory.createLoadL(/*declaration.getOffset()*/0));
        return declaration.getCode(factory);
	}

}
