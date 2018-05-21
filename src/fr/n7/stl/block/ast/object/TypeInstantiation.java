package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

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
        return
                other instanceof TypeInstantiation
                && ((TypeInstantiation) other).name.equals(name);
    }

	@Override
	public boolean compatibleWith(Type _other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type merge(Type _other) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
    public String toString() {
        return name + (typeInstantiations.size() > 0 ? ("<" + typeInstantiations + ">") : "");
    }

}
