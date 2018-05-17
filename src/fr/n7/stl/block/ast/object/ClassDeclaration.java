package fr.n7.stl.block.ast.object;

import java.util.List;

public class ClassDeclaration {
	
	private ClassArguments modifier;
	
	private ClassName declaration;
	
	private List<ClassName> extension;
	
	private List<Definition> definitions;
	
	public ClassDeclaration(ClassArguments _modifier, ClassName _declaration, List<ClassName> _extension, List<Definition> _definitions) {
		this.modifier = _modifier;
		this.declaration = _declaration;

		this.extension = extension;
		this.definitions = _definitions;
	}
}
