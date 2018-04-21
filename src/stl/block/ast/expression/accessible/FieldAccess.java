/**
 * 
 */
package stl.block.ast.expression.accessible;

import fr.n7.stl.block.ast.expression.AbstractField;
import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

/**
 * Implementation of the Abstract Syntax Tree node for accessing a field in a record.
 * @author Marc Pantel
 *
 */
public class FieldAccess extends AbstractField implements Expression {

	/**
	 * Construction for the implementation of a record field access expression Abstract Syntax Tree node.
	 * @param _record Abstract Syntax Tree for the record part in a record field access expression.
	 * @param _name Name of the field in the record field access expression.
	 */
	public FieldAccess(Expression _record, String _name) {
		super(_record, _name);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
        // Compute values to skip with 'pop'
        int i = 0;
        int lengthToKeep = 0;
        int lengthBefore = 0;
        while (i < recordType.getFields().size()) {
            // this is the accessed field
            if (recordType.getFields().get(i).getName().equals(name)) {
                lengthToKeep = recordType.getFields().get(i).getType().length();
                ++ i;
                break;
            }
            lengthBefore += recordType.getFields().get(i).getType().length();
            ++ i;
        }
        int lengthAfter = 0;
        while (i < recordType.getFields().size()) {
            lengthAfter += recordType.getFields().get(i).getType().length();
            ++ i;
        }

        Fragment fragment = factory.createFragment();
        fragment.append(record.getCode(factory));
        fragment.add(factory.createPop(0, lengthAfter));
        fragment.add(factory.createPop(lengthToKeep, lengthBefore));
        return fragment;
	}

}
