/**
 * 
 */
package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.expression.AbstractIdentifier;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.object.AbstractThisUse;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * ABSTRACT Syntax Tree node for an expression whose computation assigns a variable.
 * @author Marc Pantel
 *
 */
public class VariableAssignment extends AbstractIdentifier implements AssignableExpression {
	
	protected VariableDeclaration declaration;

	/**
	 * Creates a variable assignment expression ABSTRACT Syntax Tree node.
	 * @param name Name of the assigned variable.
	 */
	public VariableAssignment(String name) {
		super(name);
	}

    public VariableDeclaration getDeclaration() {
        return declaration;
    }

    /* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.AbstractIdentifier#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! scope.knows(this.name)) {
		    return false;
        }

        Declaration declaration = scope.get(this.name);

        if (declaration instanceof VariableDeclaration) {
            this.declaration = ((VariableDeclaration) declaration);
            return true;
        }

        return false;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.VariableUseImpl#getType()
	 */
	@Override
	public Type getType() {
	    return declaration.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.VariableUseImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
		Fragment fragment = factory.createFragment();
		fragment.add(factory.createStore(declaration.getRegister(), declaration.getOffset(), getType().length()));
		return fragment;
	}


}
