package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

public class InterfaceDeclaration {
	
	/** Class name of the interface. */
	private ClassName declaration;
	
	/** List of the methods signatures. */
	private List<Signature> signatures = new LinkedList<Signature>();
	
	/** Extended class. */
	private List<Instantiation> extendedClass = new LinkedList<Instantiation>();
	
	public InterfaceDeclaration(ClassName _declaration, List<Signature> _signatures) {
		this.declaration = _declaration;
		for (Signature s : _signatures) {
			this.signatures.add(s);
		}
	}
	
	public InterfaceDeclaration(ClassName _declaration, List<Instantiation> _instantiations, List<Signature> _signatures) {
		this(_declaration, _signatures);
		for (Instantiation i : _instantiations) {
			extendedClass.add(i);
		}
	}

}
