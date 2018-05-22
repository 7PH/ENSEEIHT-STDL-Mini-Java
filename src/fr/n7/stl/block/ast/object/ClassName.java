package fr.n7.stl.block.ast.object;

import java.util.List;

public class ClassName {
	
	private String ident;
	
	private List<ClassName> generics;	

	public ClassName(String _ident) {
		this.ident = _ident;
	}
	
	public ClassName(String _ident, List<ClassName> _generics) {
		this.ident = _ident;
		this.generics = _generics;
		
	}

}
