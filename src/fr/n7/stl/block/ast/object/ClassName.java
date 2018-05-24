package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.ASTNode;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

import java.util.List;

public class ClassName implements ASTNode {
	
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

    @Override
    public boolean resolve(HierarchicalScope<Declaration> _scope) {
        return false;
    }

    @Override
    public String toString() {
	    return className;
    }
}
