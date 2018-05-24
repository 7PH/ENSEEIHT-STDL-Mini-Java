package fr.n7.stl.block.ast.object;

import java.util.List;

public class GenericType {

	private String genericIdent;
	
	/* Exemple : MyClass<T extends A,B,C> */
	private List<TypeInstantiation> extendedTypes;
	
	public GenericType(String _ident) {
		this.genericIdent = _ident;
	}
	
	public GenericType(String _ident, List<TypeInstantiation> _extendedTypes) {
		this(_ident);
		this.extendedTypes = _extendedTypes;
		
	}
}