/**
 * 
 */
package stl.block.ast.expression.allocation;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * @author Marc Pantel
 * @TODO à quoi correspond le constructeur PointerAllocation(String name)?
 */
public class PointerAllocation implements Expression {

	protected Type element;
	protected String name;
	
	public PointerAllocation(String _name) {
		this.name = _name;
	}

	public PointerAllocation(Type _element) {
		this.element = _element;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "new " + ((this.element == null) ? this.name : this.element);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return ! _scope.contains(name) && element.resolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
	    return new PointerType(element);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        fragment.add(factory.createLoadL(element.length()));
        fragment.add(Library.MAlloc);
        return fragment;
	}

}
