package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.scope.Declaration;

public abstract class Definition implements Declaration {

	private AccessModifier access;

	private DefinitionModifier arguments;

	public void setAccess(AccessModifier access) {
		this.access = access;
	}

	public void setArguments(DefinitionModifier arguments) {
		this.arguments = arguments;
	}
	


}
