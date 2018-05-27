package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.expression.accessible.AccessibleExpression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

/** Implementation of the ABSTRACT Syntax Tree node for an unary operation expression. */
public class UnaryExpression implements Expression {

	private UnaryOperator operator;

	private Expression parameter;
	
	/** Builds a unary expression ABSTRACT Syntax Tree node from the parameter sub-expression
	 * and the unary operation.
	 * @param _operator : Unary Operator.
	 * @param _parameter : Expression for the parameter.
	 */
	public UnaryExpression(UnaryOperator _operator, Expression _parameter) {
		this.operator = _operator;
		this.parameter = _parameter;
	}

	@Override
	public String toString() {
		return "(" + this.operator + " " + this.parameter + ")";
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return this.parameter.resolve(scope);
	}

	@Override
	public Type getType() {
		Type resultType = this.parameter.getType();
		if (resultType.equals(AtomicType.ErrorType)) {
			return resultType;
		} else {
			switch (this.operator) {
				case Negate: {
					if (resultType.compatibleWith(AtomicType.BooleanType))  {
						return resultType;
					} else {
						Logger.warning("Type error in unary expression : Negate parameter " + resultType);
						return AtomicType.ErrorType;
					}
				}
				case Opposite: {
					if (resultType.compatibleWith(AtomicType.FloatingType)) {
						return resultType;
					} else {
						Logger.warning("Type error in unary expression : Opposite parameter " + resultType);
						return AtomicType.ErrorType;
					}
				}
				default : return AtomicType.ErrorType;
			}
		}
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _result = this.parameter.getCode(_factory);
		if (this.parameter instanceof AccessibleExpression) {
			_result.add(_factory.createLoadI(this.parameter.getType().length()));
		}
		_result.add(TAMFactory.createUnaryOperator(this.operator));
		return _result;
	}

}
