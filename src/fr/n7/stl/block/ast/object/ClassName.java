package fr.n7.stl.block.ast.object;

import java.util.List;

public class ClassName {
	
	private String className;
	
	private List<GenericType> genericTypes;	
	
	public ClassName(String _ident) {
		this.className = _ident;
	}
	
	public ClassName(String _ident, List<GenericType> _generics) {
		this(_ident);
		this.genericTypes = _generics;
	}
	
	public String getName() {
		return this.className;
	}
	
	public List<GenericType> getGenerics() {
		return this.genericTypes;
	}

}
