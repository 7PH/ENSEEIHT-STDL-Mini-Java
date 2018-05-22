package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;


import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.type.Type;

public class Signature implements Declaration {

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
    
	@Override
	public String toString() {
		String _result = "";
		_result += this.type + " ";
		_result += this.name + "(";
		boolean putComaFirst = false;
		for (ParameterDeclaration p : this.parameters) {
			if (putComaFirst) {
				_result += ", ";
			}
			_result += p;  
		}
		_result += ")";
		return _result;
	}

}
