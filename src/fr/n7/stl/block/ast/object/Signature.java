package fr.n7.stl.block.ast.object;

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
	
	public Signature(Type type, String name, List<ParameterDeclaration> parameters) {
		this.type = type;
		this.name = name;
		this.parameters = parameters;
	}

	public String getMethodName() { return name; }
	
	/** Get signature name.
	 * @return the name of the signature followed by the parameters types.
	 */
	public String getName() {
		String name = this.name + "(";

		if (parameters.size() > 0) {
            for (ParameterDeclaration p : this.parameters)
                name += p.getType() + ", ";

            // only for beauty purpose
            name = name.substring(0, name.length() - 2);
        }

        name += ")";

        return name;
    }

    public void assignParametersRelativeOffset() {
	    int offset = 0;
	    for (ParameterDeclaration parameter: parameters) {
	        parameter.setOffset(offset);
	        offset += parameter.getType().length();
        }
    }

	/** Get signature type.
	 * @return the type of the signature
	 */
    public Type getType() {
        return type;
    }

    /** Get signature parameters
     * @return the parameters of the signature
     */
    public List<ParameterDeclaration> getParameters() {
        return parameters;
    }
    
	@Override
	public String toString() {
        String name = type + " " + this.name + "(";

        if (parameters.size() > 0) {
            for (ParameterDeclaration p : this.parameters)
                name += p + ", ";

            // only for beauty purpose
            name = name.substring(0, name.length() - 2);
        }

        name += ")";

        return name;
	}

	@Override
    public boolean equals(Object object) {
        if (! (object instanceof Signature)) return false;
        Signature other = (Signature) object;
        return other.getName().equals(getName())
                && other.getType().equalsTo(getType());
    }
}
