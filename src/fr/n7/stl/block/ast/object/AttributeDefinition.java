package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeDefinition extends Definition {

	private Type type;
	
	private String name;
	
	public AttributeDefinition(Type _type, String _name) {
		this.type = _type;
		this.name = _name;
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
    	throw new SemanticsUndefinedException("toString method is undefined for AttributeDefinition.");
    }

	@Override
	public boolean checkType() {
    	throw new SemanticsUndefinedException("checkType method is undefined for AttributeDefinition.");
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
