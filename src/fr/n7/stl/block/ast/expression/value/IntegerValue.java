package fr.n7.stl.block.ast.expression.value;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node for an integer constant expression. */
public class IntegerValue implements Value {

	private int value;
	
	/** Builds an integer value expression implementation from a textual representation of the integer.
	 * @param _text Textual representation of the integer value.
	 */
	public IntegerValue(String _text) {
		value = Integer.parseInt(_text);
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
		return AtomicType.IntegerType;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _fragment = _factory.createFragment();
		_fragment.add(_factory.createLoadL(value));
		return _fragment;
	}

}
