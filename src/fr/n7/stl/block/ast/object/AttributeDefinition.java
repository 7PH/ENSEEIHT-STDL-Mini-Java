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
	
	public AttributeDefinition(Type _type, String _name) {
		this.type = _type;
		this.name = _name;
		this.value = null;
	}
	
	public AttributeDefinition(Type _type, String _name, Expression _valeur) {
		this.type = _type;
		this.name = _name;
		this.value = _valeur;
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
    public int allocateMemory(Register _register, int _offset) {
    	throw new SemanticsUndefinedException("allocateMemory method is undefined for AttributeDefinition.");
    }

	@Override
    public Fragment getCode(TAMFactory _factory) {
    	throw new SemanticsUndefinedException("getCode method is undefined for AttributeDefinition.");
    }

    @Override
    public String toString() {
    	String _result = "";
    	
    	if (this.getAccessModifier() != null) {
    		_result += this.getAccessModifier();
    	}
    	if (this.getDefinitionModifier() != null) {
    		_result += this.getDefinitionModifier();
    	}
    	_result += this.type + " " + this.name + ";"; 
    	
    	return _result;
    }

	@Override
	public boolean checkType() {
		boolean b = this.value.getType().compatibleWith(this.type);
		if (!b) {
			Logger.error(this.value.toString() + " is not compatible with " + this.type.toString());
		}
		return b;
	}

	@Override
	public Type getReturnType() {
    	throw new SemanticsUndefinedException("getReturnType method is undefined for AttributeDefinition.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		// Verify if the attribute is already in the scope
		if (!_scope.accepts(this)) {
			Logger.error("Object " + this.name + " is already defined in scope.");
			return false;
		}
		// Resolve type part
		if (!this.type.resolve(_scope)) {
			return false;
		}
		// Resolve value part
		if (!this.value.resolve(_scope)) {
			return false;
		}
		// Register the attribute
		_scope.register(this);
		return true;
	}
}
