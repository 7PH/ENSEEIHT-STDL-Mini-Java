package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

public class ClassDeclaration {
	
	private ClassArguments modifier;
	
	private ClassName declaration;
	
	private List<Instantiation> extension;
	
	private List<Definition> definitions;
	
	public ClassDeclaration(ClassArguments _modifier, ClassName _declaration, List<Instantiation> _extension, List<Definition> _definitions) {
		this.modifier = _modifier;
		this.declaration = _declaration;
		List<Instantiation> ext = new LinkedList<Instantiation>();
		for (Instantiation d : _extension) {
			ext.add(d);
		}
		this.extension = ext;
		List<Definition> def = new LinkedList<Definition>();
		for (Definition d : _definitions) {
			def.add(d);
		}
		this.definitions = def;
	}
}
