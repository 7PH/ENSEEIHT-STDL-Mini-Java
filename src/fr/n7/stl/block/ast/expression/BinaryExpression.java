package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/** ABSTRACT Syntax Tree node for a binary expression. */
public class BinaryExpression implements Expression {

	/** AST node for the expression whose value is the left parameter for the binary expression. */
	protected Expression left;
	
	/** AST node for the expression whose value is the left parameter for the binary expression. */
	protected Expression right;
	
	/** Binary operator computed by the Binary Expression. */
	protected BinaryOperator operator;
	
	/**
	 * Builds a binary expression ABSTRACT Syntax Tree node from the left and right sub-expressions
	 * and the binary operation.
	 * @param _left : Expression for the left parameter.
	 * @param _operator : Binary Operator.
	 * @param _right : Expression for the right parameter.
	 */
	public BinaryExpression(Expression _left, BinaryOperator _operator, Expression _right) {
		this.left = _left;
		this.right = _right;
		this.operator = _operator;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.left + " " + this.operator + " " + this.right + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		boolean _left = this.left.resolve(scope);
		boolean _right = this.right.resolve(scope);
		return _left && _right;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
		Type _left = this.left.getType();
		Type _right = this.right.getType();
		Type resultType = _left.merge(_right);
		if (resultType.equals(AtomicType.ErrorType)) {
			Logger.warning("Type error in binary expression : Merged parameters " + _left + " " + _right);
		}
		switch (this.operator) {
			case Add: {
				if (resultType.compatibleWith(AtomicType.FloatingType))  {
					return resultType;
				} else {
					Logger.warning("Type error in binary expression : " + this.operator + " parameter " + resultType);
					return AtomicType.ErrorType;
				}
			}
            case And:
            case Or: {
                if (resultType.compatibleWith(AtomicType.BooleanType)) {
                    return resultType;
                } else {
                    Logger.warning("Type error in binary expression : " + this.operator + " parameter " + resultType);
                    return AtomicType.ErrorType;
                }
            }
            case Substract:
			case Multiply:
			case Divide: {
				if (resultType.compatibleWith(AtomicType.FloatingType)) {
					return resultType;
				} else {
					Logger.warning("Type error in binary expression : " + this.operator + " parameter " + resultType);
					return AtomicType.ErrorType;
				}
			}
			case Modulo: {
				if (resultType.compatibleWith(AtomicType.IntegerType)) {
					return resultType;
				} else {
					Logger.warning("Type error in binary expression : " + this.operator + " parameter " + resultType);
					return AtomicType.ErrorType;
				}
			}
			case Lesser:
			case Greater:
			case LesserOrEqual:
			case GreaterOrEqual: {
				if (resultType.compatibleWith(AtomicType.FloatingType)) {
					return AtomicType.BooleanType;
				} else {
					Logger.warning("Type error in binary expression : " + this.operator + " parameter " + resultType);
					return AtomicType.ErrorType;
				}				
			}
			case Equals:
            case Different: {
                if (resultType.equals(AtomicType.ErrorType)) {
                    return resultType;
                } else {
                    return AtomicType.BooleanType;
                }
            }
            case Concat: {
                if (resultType.compatibleWith(AtomicType.StringType)) {
                    return resultType;
                } else {
                    return AtomicType.ErrorType;
                }
            }
			default : return AtomicType.ErrorType;
		}
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
		Fragment result = factory.createFragment();
        result.append(left.getCode(factory));
        result.append(right.getCode(factory));
		result.add(TAMFactory.createBinaryOperator(operator));
		return result;
	}

}
