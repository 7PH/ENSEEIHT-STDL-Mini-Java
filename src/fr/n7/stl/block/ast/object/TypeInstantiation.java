package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;

public class TypeInstantiation implements Type {
	
	private String name;
	
	private List<TypeInstantiation> typeInstantiations = new LinkedList<>();
	
	public TypeInstantiation(String name) {
		this.name = name;
	}
	
	public TypeInstantiation(String _name, List<TypeInstantiation> _Type_instantiations) {
		this.name = _name;
		this.typeInstantiations = _Type_instantiations;
	}

	@Override
	public boolean equalsTo(Type other) {
        boolean b = false;
        if (other instanceof TypeInstantiation) {
        	b = ((TypeInstantiation) other).name.equals(this.name);
        }
        return b;
    }

	@Override
	public boolean compatibleWith(Type _other) {
    	throw new SemanticsUndefinedException("compatibleWith method is undefined for TypeInstantiation.");
	}

	@Override
	public Type merge(Type _other) {
    	throw new SemanticsUndefinedException("merge method is undefined for TypeInstantiation.");
	}

	@Override
	public int length() {
    	throw new SemanticsUndefinedException("length method is undefined for TypeInstantiation.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
    	throw new SemanticsUndefinedException("resolve method is undefined for TypeInstantiation.");
	}

	@Override
    public String toString() {
        return this.name + (this.typeInstantiations.size() > 0 ? ("<" + this.typeInstantiations + ">") : "");
    }

}
