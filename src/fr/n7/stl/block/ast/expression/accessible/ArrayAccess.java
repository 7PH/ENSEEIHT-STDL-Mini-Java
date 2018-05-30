package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractArray;
import fr.n7.stl.block.ast.expression.BinaryOperator;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node for accessing an array element. */
public class ArrayAccess extends AbstractArray implements AccessibleExpression {

	/**
	 * Construction for the implementation of an array element access expression ABSTRACT Syntax Tree node.
	 * @param _array ABSTRACT Syntax Tree for the array part in an array element access expression.
	 * @param _index ABSTRACT Syntax Tree for the index part in an array element access expression.
	 */
	public ArrayAccess(Expression _array, Expression _index) {
		super(_array, _index);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();

        fragment.append(this.array.getCode(factory));
        fragment.append(this.index.getCode(factory));
        fragment.add(factory.createLoadL(this.getType().length()));
        fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Multiply));
        fragment.add(TAMFactory.createBinaryOperator(BinaryOperator.Add));
        fragment.add(factory.createLoadI(this.getType().length()));

        return fragment;
	}

}
