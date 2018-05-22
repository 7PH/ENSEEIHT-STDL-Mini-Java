package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;

public abstract class Definition implements Declaration, Instruction {

	private AccessModifier accessModifier;
	
	private DefinitionModifier definitionModifier;

	public AccessModifier getAccessModifier() {
		return this.accessModifier;
	}

	public DefinitionModifier getDefinitionModifier() {
		return this.definitionModifier;
	}

	public void setAccessModifier(AccessModifier _accessModifier) {
		this.accessModifier = _accessModifier;
	}

	public void setDefinitionModifier(DefinitionModifier _definitionModifier) {
		this.definitionModifier = _definitionModifier;
	}
	
	public abstract String toString();
}
