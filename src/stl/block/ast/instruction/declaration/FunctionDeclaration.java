/**
 * 
 */
package stl.block.ast.instruction.declaration;

import fr.n7.stl.block.ast.Block;
import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Abstract Syntax Tree node for a function declaration.
 * @author Marc Pantel
 */
public class FunctionDeclaration implements Instruction, Declaration {

	/**
	 * Name of the function
	 */
	protected String name;
	
	/**
	 * AST node for the returned type of the function
	 */
	protected Type type;
	
	/**
	 * List of AST nodes for the formal parameters of the function
	 */
	protected List<ParameterDeclaration> parameters;
	
	/**
	 * AST node for the body of the function
	 */
	protected Block body;

	/** Offset of the function */
    private int offset;
    private Register register;
    private String startLabel;

    /**
	 * Builds an AST node for a function declaration
	 * @param _name : Name of the function
	 * @param _type : AST node for the returned type of the function
	 * @param _parameters : List of AST nodes for the formal parameters of the function
	 * @param _body : AST node for the body of the function
	 */
	public FunctionDeclaration(String _name, Type _type, List<ParameterDeclaration> _parameters, Block _body) {
		this.name = _name;
		this.type = _type;
		this.parameters = _parameters;
		this.body = _body;
	}

	public List<ParameterDeclaration> getParameters() {
		return parameters;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _result = this.type + " " + this.name + " (";
		Iterator<ParameterDeclaration> _iter = this.parameters.iterator();
		if (_iter.hasNext()) {
			_result += _iter.next();
			while (_iter.hasNext()) {
				_result += ", " + _iter.next();
			}
		}
		return _result + ") " + this.body;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Declaration#getType()
	 */
	@Override
	public Type getType() {
		return this.type;
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		if (! _scope.accepts(this)) return false;
		if (! type.resolve(_scope)) return false;
        _scope.register(this);

		HierarchicalScope<Declaration> funScope = new SymbolTable(_scope);
		int i = 0;
		for (ParameterDeclaration parameterDeclaration: parameters) {
            parameterDeclaration.getType().resolve(_scope);
            funScope.register(parameterDeclaration);
            parameterDeclaration.setOffset(i);
            parameterDeclaration.setFunctionDeclaration(this);
            i += parameterDeclaration.getType().length();
        }

        this.startLabel = name.toLowerCase() + "_" + FunctionDeclaration.getID();

        return body.resolve(funScope);
    }

    private static int ID = 0;
    private synchronized static Integer getID() {
        return ++ FunctionDeclaration.ID;
    }

    /* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
	    for (ParameterDeclaration parameterDeclaration: parameters) {
	        if (parameterDeclaration.getType().equalsTo(AtomicType.ErrorType))
	            throw new RuntimeException("Parameter " + parameterDeclaration.name + "is an ErrorType.");
        }
        if (!body.checkType())
            throw new RuntimeException("Function Body is not well typed.");
		if (!body.getReturnType().compatibleWith(type)) 
            throw new RuntimeException("Function return " + body.getReturnType().toString()
            							+ " is not compatible with " + type.toString());
	    return true;
	}

	public int getParametersLength() {
	    int length = 0;
	    for (ParameterDeclaration parameterDeclaration: parameters)
	        length += parameterDeclaration.getType().length();
	    return length;
    }

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register register, int offset) {
	    this.register = register;
	    this.offset = offset;
	    body.allocateMemory(Register.SB, offset + 3 + getParametersLength() + getType().length());
        return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 * @TODO Handle the case of 'void' return
	 * @TODO Handle parameters
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
	    String id = String.valueOf(factory.createLabelNumber());
        String endLabel = "fun_end_" + id ;

	    Fragment fragment1 = factory.createFragment();
        //fragment1.add(factory.createPush(getParametersLength()));
        fragment1.add(factory.createJump(endLabel));

        Fragment fragment2 = factory.createFragment();
        fragment2.add(factory.createLoad(Register.LB, - 1 - getReturnType().length() - getParametersLength(), getParametersLength()));
        fragment2.append(body.getCode(factory));
        fragment2.addPrefix(startLabel + ":");
        fragment2.addSuffix(endLabel + ":");

        fragment1.append(fragment2);

        return fragment1;
	}

    @Override
    public Type getReturnType() {
        return AtomicType.VoidType;
    }

    public int getOffset() {
        return offset;
    }

    public Register getRegister() {
        return register;
    }
    public String getStartLabel() {
        return startLabel;
    }

    public Block getBody() {
        return body;
    }
}
