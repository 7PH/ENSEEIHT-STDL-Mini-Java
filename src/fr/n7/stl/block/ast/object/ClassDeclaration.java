package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

public class ClassDeclaration {
	
	private ClassArguments modifier;
	
	private ClassName declaration;
	
	private List<ClassName> extension;
	
	private List<Definition> definitions;
	
	public ClassDeclaration(ClassArguments _modifier, ClassName _declaration, List<ClassName> _extension, List<Definition> _definitions) {
		this.modifier = _modifier;
		this.declaration = _declaration;
		LinkedList<ClassName> ext = new LinkedList<ClassName>();
		for (ClassName d : _extension) {
			ext.add(d);
		}
		this.extension = ext;
		LinkedList<Definition> def = new LinkedList<Definition>();
		for (Definition d : _definitions) {
			def.add(d);
		}
		this.definitions = def;
	}
}
