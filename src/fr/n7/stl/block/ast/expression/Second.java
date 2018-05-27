package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.CoupleType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node  for an expression extracting the second component in a couple. */
public class Second implements Expression {

	/** AST node for the expression whose value must whose second element is extracted by the expression. */
	private Expression target;

    private CoupleType targetType;

    /** Builds an ABSTRACT Syntax Tree node for an expression extracting the second component of a couple.
	 * @param _target : AST node for the expression whose value must whose second element is extracted by the expression.
	 */
	public Second(Expression _target) {
		this.target = _target;
	}

	@Override
	public String toString() {
		return "(snd " + this.target + ")";
	}

	@Override
	public Type getType() {
        Type targetType = target.getType();
        while (targetType instanceof NamedType)
            targetType = ((NamedType)targetType).getType();
        if (! (targetType instanceof CoupleType))
            return AtomicType.ErrorType;
        this.targetType = (CoupleType) targetType;
        return this.targetType.getSecond();
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return target.resolve(scope);
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
        final int firstLength = this.targetType.getFirst().length();
        final int secondLength = this.targetType.getSecond().length();
        Fragment fragment = factory.createFragment();
        fragment.append(target.getCode(factory));
        fragment.add(factory.createPop(secondLength, firstLength));
        return fragment;
	}

}
