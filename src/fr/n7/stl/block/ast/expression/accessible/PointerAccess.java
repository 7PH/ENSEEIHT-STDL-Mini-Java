package fr.n7.stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractPointer;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/** Implementation of the ABSTRACT Syntax Tree node for a pointer access expression. */
public class PointerAccess extends AbstractPointer implements Expression {

	/** Construction for the implementation of a pointer content access expression ABSTRACT Syntax Tree node.
	 * @param _pointer ABSTRACT Syntax Tree for the pointer expression in a pointer content access expression.
	 */
	public PointerAccess(Expression _pointer) {
		super(_pointer);
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();
        fragment.append(pointer.getCode(factory));
        fragment.add(factory.createLoadI(pointer.getType().length()));
        return fragment;
	}

}
