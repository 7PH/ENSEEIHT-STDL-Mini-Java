package fr.n7.stl.block.ast.object;

import java.util.List;

import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.type.Type;

public class Signature {

	/* Type of the method.*/
	private Type type;

	/* Method name. */
	private String name;
	
	/* Method parameters. */
	private List<ParameterDeclaration> parameters;
	
	public Signature(Type _type, String _name) {
		this.type = _type;
		this.name = _name;
		this.parameters = null;
	}
	
	public Signature(Type _type, String _name, List<ParameterDeclaration> _parameters) {
		this.type = _type;
		this.name = _name;
		this.parameters = _parameters;
	}
		
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
