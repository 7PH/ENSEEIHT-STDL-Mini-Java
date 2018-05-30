package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.expression.AbstractArray;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.type.ArrayType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

/** ABSTRACT Syntax Tree node for an expression whose computation assigns a cell in an array. */
public class ArrayAssignment extends AbstractArray implements AssignableExpression {

	/**
	 * Construction for the implementation of an array element assignment expression ABSTRACT Syntax Tree node.
	 * @param _array ABSTRACT Syntax Tree for the array part in an array element assignment expression.
	 * @param _index ABSTRACT Syntax Tree for the index part in an array element assignment expression.
	 */
	public ArrayAssignment(AssignableExpression _array, Expression _index) {
		super(_array, _index);
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.impl.ArrayAccessImpl#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
	    // assignemnt => VariableUse
        //int offset = ((VariableAssignment)array).declaration.getOffset();
        int offset = 0;

        Fragment fragment = factory.createFragment();
        fragment.append(index.getCode(factory));
        fragment.add(factory.createLoadL(((ArrayType)array.getType()).getType().length()));
	    fragment.add(Library.IMul);
        fragment.add(factory.createLoad(Register.SB, offset, 1));
	    fragment.add(Library.IAdd);
	    fragment.add(factory.createStoreI(((ArrayType)array.getType()).getType().length()));
        return fragment;
	}

	
}
