package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;

public abstract class Definition implements Declaration, Instruction {

	protected AccessModifier accessModifier;

    protected DefinitionModifier definitionModifier;
    
    protected ClassDeclaration parent;

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

	public ClassDeclaration getParent() {
		return parent;
	}

	public void setParent(ClassDeclaration parent) {
		this.parent = parent;
	}

	public boolean isStatic() {
	    return definitionModifier == DefinitionModifier.STATIC
                || definitionModifier == DefinitionModifier.STATIC_FINAL;
    }

    public boolean isFinal() {
	    return definitionModifier == DefinitionModifier.FINAL
                ||  definitionModifier == DefinitionModifier.STATIC_FINAL;
    }
	
	public abstract String toString();
}
