package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AttributeAssignment implements AssignableExpression {
	
	private AssignableExpression object;
	
	private TypeInstantiation objectType;
	
	private String attributeIdentificateur;
	
	private AttributeDefinition attribute;

	public AttributeAssignment(AssignableExpression _objet, String _attributeIdentificateur) {
		this.object = _objet;
		this.attributeIdentificateur = _attributeIdentificateur;
	}

	@Override
	public Type getType() {
		throw new SemanticsUndefinedException("getType method is undefined for AttributeAssignment.");
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		throw new SemanticsUndefinedException("getCode method is undefined for AttributeAssignment.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		// Verify objet resolve.
		if (! this.object.resolve(scope)) {
    		Logger.error("Could not resolve attribute assignment because the object " + this.object.toString() + ".");
	    	return false;
		}

		// Get objet type and check that is a TypeInstantiation
	    Type type = this.object.getType();

	    if (! (type instanceof TypeInstantiation)) {
	    	Logger.error(this.object.toString() + " is not a TypeInstantiation.");
	    	return false;
	    }

	    // Verify that the TypeInstantiation contain the field
	    this.objectType = (TypeInstantiation) type;

	    if (!(this.objectType.contains(this.attributeIdentificateur))) {
	    	Logger.error(this.object.toString() + " does not contain " + this.attributeIdentificateur + " field.");
	    	return false;
	    }

	    this.attribute = this.objectType.get(this.attributeIdentificateur);

	    return true;
	}

}
