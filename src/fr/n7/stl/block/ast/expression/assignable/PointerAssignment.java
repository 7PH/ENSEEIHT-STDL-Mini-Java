package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.expression.AbstractPointer;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.type.PointerType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/** ABSTRACT Syntax Tree node for an expression whose computation assigns the content of a pointed cell. */
public class PointerAssignment extends AbstractPointer implements AssignableExpression {

	/** Construction for the implementation of a pointer content assignment expression ABSTRACT Syntax Tree node.
	 * @param _pointer ABSTRACT Syntax Tree for the pointer expression in a pointer content assignment expression.
	 */
	public PointerAssignment(Expression _pointer) {
		super(_pointer);
	}

	@Override
	public Fragment getCode(TAMFactory factory) {
        int offset = ((VariableAssignment)pointer).declaration.getOffset();
        int size = ((PointerType)pointer.getType()).getPointedType().length();

	    Fragment fragment = factory.createFragment();
	    fragment.add(factory.createLoad(Register.SB, offset, 1)); // load addr
	    fragment.add(factory.createStoreI(size)); // store data
	    return fragment;
	}

}
