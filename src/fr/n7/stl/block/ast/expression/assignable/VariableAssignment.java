package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.expression.AbstractIdentifier;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.object.AttributeDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/** ABSTRACT Syntax Tree node for an expression whose computation assigns a variable. */
public class VariableAssignment extends AbstractIdentifier implements AssignableExpression {
	
	protected Declaration declaration;

	private Register register;

	private int offset;

	/**
	 * Creates a variable assignment expression ABSTRACT Syntax Tree node.
	 * @param name Name of the assigned variable.
	 */
	public VariableAssignment(String name) {
		super(name);
	}

    public Declaration getDeclaration() {
        return declaration;
    }

    /* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.AbstractIdentifier#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! scope.knows(this.name)) {
			Logger.error("The variable " + this.name + " is unknown.");
		    return false;
        }

        Declaration declaration = scope.get(name);

        if (declaration instanceof VariableDeclaration) {
            VariableDeclaration vr = ((VariableDeclaration) declaration);
            this.register = vr.getRegister();
            this.offset = vr.getOffset();
            this.declaration = declaration;
            return true;
        } else if (declaration instanceof AttributeDefinition) {
            // we can assign to an attribute so it's ok
            AttributeDefinition ad = ((AttributeDefinition) declaration);
            this.register = ad.getRegister();
            this.offset = ad.getRelativeOffset();
            this.declaration = declaration;
            return true;
        } else {
            Logger.error("Assignment to non variable declaration " + name + ": " + declaration.getClass());
        }

		Logger.error(this.name + " is not declared as Varaiable but as " + declaration.getClass());
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
		fragment.add(factory.createStore(register, offset, getType().length()));
		return fragment;
	}


}
