package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.AbstractAttribute;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.object.AttributeDefinition;
import fr.n7.stl.block.ast.object.InstanceType;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AttributeAssignment extends AbstractAttribute implements AssignableExpression {
	
	private InstanceType objectType;
	
	private AttributeDefinition attribute;

    public AttributeAssignment(Expression object, String name) {
        super(object, name);
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
		if (! object.resolve(scope)) {
    		Logger.error("Could not resolve attribute assignment because of: " + this.object.toString() + ".");
	    	return false;
		}

		// Get objet type and check that is a InstanceType
	    Type type = this.object.getType();

	    if (! (type instanceof InstanceType)) {
	    	Logger.error(this.object.toString() + " is not a InstanceType.");
	    	return false;
	    }

	    // Verify that the InstanceType contain the field
	    this.objectType = (InstanceType) type;

        //if (!(this.objectType.contains(this.attrName))) {
        //	Logger.error(this.object.toString() + " does not contain " + this.attrName + " field.");
        //	return false;
        //}

        //this.attribute = this.objectType.get(this.attrName);

	    return true;
	}

	@Override
    public String toString() {
	    return object + "." + name;
    }
}
