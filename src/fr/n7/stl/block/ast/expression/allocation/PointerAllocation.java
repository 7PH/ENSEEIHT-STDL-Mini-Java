package fr.n7.stl.block.ast.expression.allocation;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

public class PointerAllocation implements Expression {

	protected Type element;
	protected String name;
	
	public PointerAllocation(String _name) {
		this.name = _name;
	}

	public PointerAllocation(Type _element) {
		this.element = _element;
	}

	@Override
	public String toString() {
		return "new " + ((this.element == null) ? this.name : this.element);
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return ! scope.contains(name) && element.resolve(scope);
	}

	@Override
	public Type getType() {
	    return new PointerType(element);
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        fragment.add(factory.createLoadL(element.length()));
        fragment.add(Library.MAlloc);
        return fragment;
	}

}
