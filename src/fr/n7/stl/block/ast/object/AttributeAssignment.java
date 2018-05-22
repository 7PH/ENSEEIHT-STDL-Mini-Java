package fr.n7.stl.block.ast.object;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.expression.assignable.AssignableExpression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public class AttributeAssignment implements AssignableExpression {
	
	private AssignableExpression objet;
	
	private String identificateur;

	public AttributeAssignment(AssignableExpression _objet, String _identificateur) {
		this.objet = _objet;
		this.identificateur = _identificateur;
	}

	@Override
	public Type getType() {
		throw new SemanticsUndefinedException("getType method is undefined for AttrobuteAssignment.");
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		throw new SemanticsUndefinedException("getCode method is undefined for AttrobuteAssignment.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		throw new SemanticsUndefinedException("resolve method is undefined for AttrobuteAssignment.");
	}

}
