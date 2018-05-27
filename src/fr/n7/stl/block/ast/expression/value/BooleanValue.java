package fr.n7.stl.block.ast.expression.value;

import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

public enum BooleanValue implements Value {

	/** Represents the True value. */
	True,

	/** Represents the False value. */
	False;

	public String toString() {
		switch (this) {
		case False: return "false";
		case True: return "true";
		default: throw new IllegalArgumentException( "The default case should never be triggered.");
		
		}
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		return true;
	}

	@Override
	public Type getType() {
		return AtomicType.BooleanType;
	}

	@Override
	public Fragment getCode(TAMFactory _factory) {
		Fragment _code = _factory.createFragment();
		switch (this) {
			case True : {
				_code.add(_factory.createLoadL(1));
				break;
			}
			case False : {
				_code.add(_factory.createLoadL(0));
				break;
			}
		}
		return _code;
	}

}
