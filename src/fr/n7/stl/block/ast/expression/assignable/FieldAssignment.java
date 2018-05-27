package fr.n7.stl.block.ast.expression.assignable;

import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.type.declaration.FieldDeclaration;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.TAMFactory;

/** ABSTRACT Syntax Tree node for an expression whose computation assigns a field in a record. */
public class FieldAssignment extends AbstractField implements AssignableExpression {

	/** Construction for the implementation of a record field assignment expression ABSTRACT Syntax Tree node.
	 * @param record ABSTRACT Syntax Tree for the record part in a record field assignment expression.
	 * @param name Name of the field in the record field assignment expression.
	 */
	public FieldAssignment(AssignableExpression record, String name) {
		super(record, name);
	}
	
	@Override
	public Fragment getCode(TAMFactory factory) {
        int offset = ((VariableAssignment)record).declaration.getOffset();
        FieldDeclaration field = null;
        int lengthBefore = 0;
        for (FieldDeclaration fieldDeclaration: recordType.getFields()) {
            if (fieldDeclaration.getName().equals(name)) {
                field = fieldDeclaration;
                break;
            }
            lengthBefore += fieldDeclaration.getType().length();
        }
        Fragment fragment = factory.createFragment();
        fragment.add(factory.createLoadL(offset));
        fragment.add(factory.createLoadL(lengthBefore));
        fragment.add(Library.IAdd);
        fragment.add(factory.createStoreI(field.getType().length()));
        return fragment;
	}
	
}
