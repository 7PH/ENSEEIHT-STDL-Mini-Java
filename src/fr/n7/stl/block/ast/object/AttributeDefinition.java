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
	
	private Expression valeur;
	
	public AttributeDefinition(Type _type, String _name) {
		this.type = _type;
		this.name = _name;
		this.valeur = null;
	}
	
	public AttributeDefinition(Type _type, String _name, Expression _valeur) {
		this.type = _type;
		this.name = _name;
		this.valeur = _valeur;
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
    	
    	if (this.getAccess() != null) {
    		_result += this.getAccess();
    	}
    	if (this.getArguments() != null) {
    		_result += this.getArguments();
    	}
    	_result += this.type + " " + this.name + ";"; 
    	
    	return _result;
    }

	@Override
	public boolean checkType() {
		boolean b = this.valeur.getType().compatibleWith(this.type);
		if (!b) {
			Logger.error(this.valeur.toString() + " is not compatible with " + this.type.toString());
		}
		return b;
	}

	@Override
	public Type getReturnType() {
    	throw new SemanticsUndefinedException("getReturnType method is undefined for AttributeDefinition.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
    	throw new SemanticsUndefinedException("resolve method is undefined for AttributeDefinition.");
	}
}
