package fr.n7.stl.block.ast.object;

import java.util.List;

public class ClassName {
	
	private String className;
	
	private List<ClassName> generics;	

	public ClassName(String _ident) {
		this.className = _ident;
	}
	
	public ClassName(String _ident, List<ClassName> _generics) {
		this.className = _ident;
		this.generics = _generics;
	}
	
	public String getName() {
		return this.className;
	}

}
