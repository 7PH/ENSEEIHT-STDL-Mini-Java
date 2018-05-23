package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.RecordType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AttributeAssignment implements AssignableExpression {
	
	private AssignableExpression objet;
	
	private TypeInstantiation objetType;
	
	private String attributeIdentificateur;
	
	private AttributeDefinition attribute;

	public AttributeAssignment(AssignableExpression _objet, String _attributeIdentificateur) {
		this.objet = _objet;
		this.attributeIdentificateur = _attributeIdentificateur;
	}

	@Override
	public Type getType() {
		throw new SemanticsUndefinedException("getType method is undefined for AttrobuteAssignment.");
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		throw new SemanticsUndefinedException("getCode method is undefined for AttrobuteAssignment.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean b = true;
		// Verify objet resolve.
		if (!(this.objet.resolve(_scope))) {
	    	b = false;
	    	Logger.error(this.objet.toString() + " resolve failed.");
		} else {
			// Get objet type and check that is a TypeInstantiation
		    Type type = this.objet.getType();
		    if (!(type instanceof TypeInstantiation)) {
		    	b = false;
		    	Logger.error(this.objet.toString() + " is not a TypeInstantiation.");
		    } else {
			    // Verify that the TypeInstantiation contain the field
			    this.objetType = (TypeInstantiation) type;
			    if (!(this.objetType.contains(this.attributeIdentificateur))) {
			    	b = false;
			    	Logger.error(this.objet.toString() + " does not contain " + this.attributeIdentificateur + " field.");
			    }
			    this.attribute = this.objetType.get(this.attributeIdentificateur);
		    }
		}
	    return b;
	}

}
