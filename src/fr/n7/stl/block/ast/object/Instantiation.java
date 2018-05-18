package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;

public class Instantiation implements Type {
	
	private String name;
	
	private List<Instantiation> instantiations;
	
	public Instantiation(String _name) {
		this.name = _name;
		this.instantiations = new LinkedList<Instantiation>();
	}
	
	public Instantiation(String _name, List<Instantiation> _instantiations) {
		this.name = _name;
		this.instantiations = _instantiations;
	}

	@Override
	public boolean equalsTo(Type _other) {
		// TODO Auto-generated method stub
		return false;
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

}
