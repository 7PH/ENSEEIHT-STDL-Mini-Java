package fr.n7.stl.block.ast.object;

import java.util.List;

public class Declaration {
	
	private String ident;
	
	private List<Declaration> generics;	
	
	
	public Declaration(String _ident) {
		this.ident = _ident;
	}
	
	public Declaration(String _ident, List<Declaration> _generics) {
		this.ident = _ident;
		this.generics = _generics;
		
	}
	

}
