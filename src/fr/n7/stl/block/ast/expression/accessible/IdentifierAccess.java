/**
 * 
 */
package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractIdentifier;
import fr.n7.stl.block.ast.expression.AbstractUse;
import fr.n7.stl.block.ast.instruction.declaration.ConstantDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.VariableDeclaration;
import fr.n7.stl.block.ast.object.AttributeDefinition;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/**
 * Implementation of the ABSTRACT Syntax Tree node for a variable use expression.
 * @author Marc Pantel
 * TODO : Should also hold a function and not only a variable.
 */
public class IdentifierAccess extends AbstractIdentifier implements AccessibleExpression {
	
	protected AbstractUse expression;
	
	/**
	 * Creates a variable use expression ABSTRACT Syntax Tree node.
	 * @param name Name of the used variable.
	 */
	public IdentifierAccess(String name) {
		super(name);
	}

	/*
	 * @TODO resolve? on VariableUse case
	 */
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! scope.knows(this.name)) {
            Logger.error("The identifier " + this.name + " has not been found.");
            return false;
        }

        Declaration declaration = scope.get(this.name);
        if (declaration instanceof VariableDeclaration) {
            this.expression = new VariableUse((VariableDeclaration) declaration);
            return true;
        } else if (declaration instanceof ParameterDeclaration) {
            this.expression = new ParameterUse((ParameterDeclaration) declaration, null);
            return true;
        } else if (declaration instanceof ConstantDeclaration) {
            // TODO : refactor the management of Constants
            this.expression = new ConstantUse((ConstantDeclaration) declaration);
            return true;
        } else if (declaration instanceof AttributeDefinition) {
            AttributeAccess attributeAccess = new AttributeAccess("this", name);
            attributeAccess.resolve(scope);
            this.expression = attributeAccess;
            return true;
        }

        Logger.error("The declaration for " + this.name + " is of the wrong kind.");
        return false;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		return expression.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
		return expression.getCode(factory);
	}

}
