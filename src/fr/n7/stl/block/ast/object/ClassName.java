package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.ASTNode;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;

import java.util.LinkedList;
import java.util.List;

public class ClassName implements ASTNode {
	
	private String name;
	
	private List<GenericType> generics;
	
	public ClassName(String ident) {
		this.name = ident;
		this.generics = new LinkedList<>();
	}
	
	public ClassName(String ident, List<GenericType> generics) {
		this(ident);
		this.generics = generics;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<GenericType> getGenerics() {
		return this.generics;
	}
	
    @Override
    public boolean resolve(HierarchicalScope<Declaration> scope) {
        return false;
    }
	
    @Override
    public String toString() {
	    String result = name;
	    if (generics.size() > 0)
	        result += "<" + generics + ">";
	    return result;
    }
}
