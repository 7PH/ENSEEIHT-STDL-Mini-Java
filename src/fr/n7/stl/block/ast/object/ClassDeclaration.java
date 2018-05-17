package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

public class ClassDeclaration {
	
	private ClassArguments modifier;
	
	private ClassName declaration;
	
	private List<ClassName> extension = new LinkedList<>();
	
	private List<Definition> definitions = new LinkedList<>();
	
	public ClassDeclaration(ClassArguments _modifier, ClassName _declaration, List<ClassName> _extension, List<Definition> _definitions) {
		this.modifier = _modifier;
		this.declaration = _declaration;

		this.extension.addAll(_extension);
		this.definitions.addAll(_definitions);
	}
}
