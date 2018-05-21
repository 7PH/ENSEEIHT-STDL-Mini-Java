package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.type.Type;

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
}
