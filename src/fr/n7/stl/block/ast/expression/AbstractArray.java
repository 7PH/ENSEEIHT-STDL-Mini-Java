package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.ArrayType;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;

/** Common elements between left (Assignable) and right (Expression) end sides of assignments. These elements
 * share attributes, toString and getType methods.
 */
public abstract class AbstractArray implements Expression {

	/** AST node that represents the expression whose result is an array. */
	protected Expression array;
	
	/** AST node that represents the expression whose result is an integer value used to index the array. */
	protected Expression index;
	
	/**
	 * Construction for the implementation of an array element access expression ABSTRACT Syntax Tree node.
	 * @param _array ABSTRACT Syntax Tree for the array part in an array element access expression.
	 * @param _index ABSTRACT Syntax Tree for the index part in an array element access expression.
	 */
	public AbstractArray(Expression _array, Expression _index) {
		this.array = _array;
		this.index = _index;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.array + "[ " + this.index + " ]");
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
	    return array.resolve(scope) && index.resolve(scope);
	}
	
	/**
	 * Synthesized Semantics attribute to compute the type of an expression.
	 * @return Synthesized Type of the expression.
	 */
	public Type getType() {
	    if (! index.getType().equalsTo(AtomicType.IntegerType))
	        return AtomicType.ErrorType;
        if (! (array.getType() instanceof ArrayType))
            return AtomicType.ErrorType;

        ArrayType arrayType = (ArrayType) array.getType();
        return arrayType.getType();

	}

}