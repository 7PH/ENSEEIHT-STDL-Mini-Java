package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.scope.Declaration;

public abstract class Definition implements Declaration {

	private AccessArguments access;

	private DefinitionArguments arguments;

	public void setAccess(AccessArguments access) {
		this.access = access;
	}

	public void setArguments(DefinitionArguments arguments) {
		this.arguments = arguments;
	}
	


}
