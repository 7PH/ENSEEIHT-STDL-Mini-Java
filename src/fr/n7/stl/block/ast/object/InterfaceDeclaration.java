package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.ASTNode;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

import java.util.LinkedList;
import java.util.List;

public class InterfaceDeclaration implements ASTNode {
	
	/** Class name of the interface. */
	private ClassName name;
	
	/** List of the methods signatures. */
	private List<Signature> signatures = new LinkedList<Signature>();
	
	/** Extended class. */
	private List<TypeInstantiation> extendedClass = new LinkedList<TypeInstantiation>();
	
	public InterfaceDeclaration(ClassName _name, List<Signature> _signatures) {
		this.name = _name;
		for (Signature s : _signatures) {
			this.signatures.add(s);
		}
	}
	
	public InterfaceDeclaration(ClassName _declaration, List<TypeInstantiation> _Type_instantiations, List<Signature> _signatures) {
		this(_declaration, _signatures);
		for (TypeInstantiation i : _Type_instantiations) {
			extendedClass.add(i);
		}
	}

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        return false;
    }
}
