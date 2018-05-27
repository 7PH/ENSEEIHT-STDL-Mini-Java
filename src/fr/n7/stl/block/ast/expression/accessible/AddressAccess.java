package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.expression.assignable.VariableAssignment;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class AddressAccess implements AccessibleExpression {

	protected AssignableExpression assignable;

	public AddressAccess(AssignableExpression _assignable) {
		this.assignable = _assignable;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return assignable.resolve(scope);
	}

	@Override
	public Type getType() {
		return new PointerType(assignable.getType());
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		int offset = ((VariableAssignment) assignable).getDeclaration().getOffset();
		Fragment fragment = factory.createFragment();
		fragment.add(factory.createLoadL(offset));
		return fragment;
	}

}
