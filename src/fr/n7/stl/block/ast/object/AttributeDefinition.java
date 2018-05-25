package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

public class AttributeDefinition extends Definition {

	private Type type;
	
	private String name;
	
	private Expression value;
	
	public AttributeDefinition(Type type, String name) {
		this.type = type;
		this.name = name;
		this.value = null;
	}
	
	public AttributeDefinition(Type type, String name, Expression value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Type getType() {
		return this.type;
	}
	
	@Override
    public int allocateMemory(Register register, int _offset) {
    	throw new SemanticsUndefinedException("allocateMemory method is undefined for AttributeDefinition.");
    }

	@Override
    public Fragment getCode(TAMFactory factory) {
    	throw new SemanticsUndefinedException("getCode method is undefined for AttributeDefinition.");
    }

    @Override
    public String toString() {
    	String result = "";
    	
    	if (this.getAccessModifier() != null)
    		result += this.getAccessModifier();

    	if (this.getDefinitionModifier() != null)
    		result += this.getDefinitionModifier();

    	result += this.type + " " + this.name;

    	if (value != null)
    	    result += " = " + value;

    	result += ";";

    	return result;
    }

	@Override
	public boolean checkType() {
        boolean ok = true;
		if (value != null && ! value.getType().compatibleWith(type)) {
			Logger.error(value.toString() + " is not compatible with " + type.toString());
            ok = false;
		}
		return ok;
	}

	@Override
	public Type getReturnType() {
    	throw new SemanticsUndefinedException("getReturnType method is undefined for AttributeDefinition.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		// Verify if the attribute is already in the scope
		if (!scope.accepts(this)) {
			Logger.error("Object " + this.name + " is already defined in scope.");
			return false;
		}
		// Resolve type part
		if (!this.type.resolve(scope)) {
    		Logger.error("Could not resolve attribute definition because of the type " + this.type.toString() + ".");
			return false;
		}
		// Resolve value part
		if (!this.value.resolve(scope)) {
    		Logger.error("Could not resolve attribute definition because of the value " + this.value.toString() + ".");
			return false;
		}
		// Register the attribute
		scope.register(this);
		return true;
	}
}
