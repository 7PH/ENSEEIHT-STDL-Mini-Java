package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.type.Type;

public class Signature {

	/** Type of the method.*/
	private Type type;

	/** Method name. */
	private String name;
	
	/** Method parameters. */
	private List<ParameterDeclaration> parameters;
	
	public Signature(Type _type, String _name) {
		this.type = _type;
		this.name = _name;
		this.parameters = new LinkedList<ParameterDeclaration>();
	}
	
	public Signature(Type _type, String _name, List<ParameterDeclaration> _parameters) {
		this.type = _type;
		this.name = _name;
		this.parameters = _parameters;
	}
	
	@Override
	public String toString() {
		throw new SemanticsUndefinedException("toString method is undefined for Signature.");
	}
	
	/** Get signature name.
	 * @return the name of the signature
	 */
	public String getName() {
        return this.name;
    }

	/** Get signature type.
	 * @return the type of the signature
	 */
    public Type getType() {
        return this.type;
    }

    /** Get signature parameters
     * @return the parameters of the signature
     */
    public List<ParameterDeclaration> getParameters() {
        return this.parameters;
    }

}
