package fr.n7.stl.block.ast.expression.value;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node for an integer constant expression. */
public class StringValue implements Value {

	private String value;
	
	/** Builds an integer value expression implementation from a textual representation of the integer.
	 * @param text Textual representation of the integer value.
	 */
	public StringValue(String text) {
		value = text.substring(1).substring(0, text.length() - 2);
	}

	@Override
	public String toString() {
		return "" + this.value;
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return true;
	}

	@Override
	public Type getType() {
		return AtomicType.StringType;
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
		Fragment fragment = factory.createFragment();
        fragment.add(factory.createLoadL(value));
		return fragment;
	}

}
