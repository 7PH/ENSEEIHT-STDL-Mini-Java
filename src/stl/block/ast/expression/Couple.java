/**
 * 
 */
package stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.CoupleType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Abstract Syntax Tree node for building a couple value.
 * @author Marc Pantel
 *
 */
public class Couple implements Expression {

	/**
	 * AST node for the expression whose value is the first value in a couple expression.
	 */
	private Expression first;
	
	/**
	 * AST node for the expression whose value is the second value in a couple expression.
	 */
	private Expression second;

	/**
	 * Construction for a couple expression implementation.
	 * @param _first First part of the couple.
	 * @param _second Second part of the couple.
	 */
	public Couple(Expression _first, Expression _second) {
		this.first = _first;
		this.second = _second;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "< " + this.first + ", " + this.second + ">";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		boolean _first = this.first.resolve(_scope);
		boolean _second = this.second.resolve(_scope);
		return _first && _second;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
	    final Type
            firstType = first.getType(),
            secondType = second.getType();

	    if (firstType.equalsTo(AtomicType.ErrorType)
                || secondType.equalsTo(AtomicType.ErrorType))
	        return AtomicType.ErrorType;

		return new CoupleType(firstType, secondType);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        fragment.append(first.getCode(factory));
        fragment.append(second.getCode(factory));
        return fragment;
	}

}
