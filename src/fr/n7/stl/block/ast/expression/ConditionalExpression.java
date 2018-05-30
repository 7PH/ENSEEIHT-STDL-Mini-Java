package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/** ABSTRACT Syntax Tree node for a conditional expression. */
public class ConditionalExpression implements Expression {

	/** AST node for the expression whose value is the condition for the conditional expression. */
	protected Expression condition;
	
	/** AST node for the expression whose value is the then parameter for the conditional expression. */
	protected Expression thenExpression;
	
	/** AST node for the expression whose value is the else parameter for the conditional expression. */
	protected Expression elseExpression;
	
	/**
	 * Builds a binary expression ABSTRACT Syntax Tree node from the left and right sub-expressions
	 * and the binary operation.
	 * @param _else : Expression for the left parameter.
	 * @param _condition : Binary Operator.
	 * @param _then : Expression for the right parameter.
	 */
	public ConditionalExpression(Expression _condition, Expression _then, Expression _else) {
		this.condition = _condition;
		this.thenExpression = _then;
		this.elseExpression = _else;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return condition.resolve(scope)
                && thenExpression.resolve(scope)
                && elseExpression.resolve(scope);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.condition + " ? " + this.thenExpression + " : " + this.elseExpression + ")";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
	    if (! condition.getType().equalsTo(AtomicType.BooleanType)) return AtomicType.ErrorType;
	    return thenExpression.getType().merge(elseExpression.getType());
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();

        String endLabel = "endif_" + factory.createLabelNumber();
        String elseLabel = "else_" + factory.createLabelNumber();

        fragment.append(condition.getCode(factory));
        fragment.add(factory.createJumpIf(elseLabel, 0));
        fragment.append(thenExpression.getCode(factory));
        fragment.add(factory.createJump(endLabel));
        fragment.addSuffix(elseLabel + ":");
        fragment.append(elseExpression.getCode(factory));
        fragment.addSuffix(endLabel + ":");

        return fragment;
	}

}
