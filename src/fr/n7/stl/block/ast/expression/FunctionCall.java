/**
 * 
 */
package fr.n7.stl.block.ast.expression;

import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.declaration.FunctionDeclaration;
import fr.n7.stl.block.ast.instruction.declaration.ParameterDeclaration;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

/**
 * ABSTRACT Syntax Tree node for a function call expression.
 * @author Marc Pantel
 * @TODO resolve
 *
 */
public class FunctionCall implements Expression, Instruction {

	/**
	 * Name of the called function.
	 * TODO : Should be an expression.
	 */
	protected String name;
	
	/**
	 * Declaration of the called function after name resolution.
	 * TODO : Should rely on the VariableUse class.
	 */
	protected FunctionDeclaration function;
	
	/**
	 * List of AST nodes that computes the values of the parameters for the function call.
	 */
	protected List<Expression> arguments;
	
	/**
	 * @param _name : Name of the called function.
	 * @param _arguments : List of AST nodes that computes the values of the parameters for the function call.
	 */
	public FunctionCall(String _name, List<Expression> _arguments) {
		this.name = _name;
		this.function = null;
		this.arguments = _arguments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		//String _result = ((this.function == null) ? this.name : this.function) + "(";
		String _result = this.name + "(";
		for (Expression expression: arguments)
		    _result += expression + ", ";
        _result = _result.substring(0, _result.length() - 2);
		return  _result + ")";
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.expression.Expression#resolve(fr.n7.stl.block.ast.scope.HierarchicalScope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
	    if (! scope.knows(name)) return false;
	    for (Expression expression: arguments) {
	        if (! expression.resolve(scope))
	            return false;
        }
		this.function = (FunctionDeclaration) scope.get(name);
        return true;
	}

    /* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getType()
	 */
	@Override
	public Type getType() {
	    List<ParameterDeclaration> parameterDeclarations = function.getParameters();
	    if (parameterDeclarations.size() != arguments.size()) return AtomicType.ErrorType;
	    int i = 0;
	    while (i < arguments.size()) {
	        if (! arguments.get(i).getType().compatibleWith(parameterDeclarations.get(i).getType()))
	            return AtomicType.ErrorType;
	        ++ i;
        }
		return function.getType();
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Expression#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
    @Override
    public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();

        //int parametersLength = function.getParametersLength();

		for (Expression argument : arguments)
		    fragment.append(argument.getCode(factory));

        fragment.add(factory.createPush(function.getType().length()));
        fragment.add(factory.createCall(function.getStartLabel(), Register.SB));
        fragment.add(factory.createPop(function.getType().length(), function.getParametersLength()));

        return fragment;
    }


    /* @TODO */
    @Override
    public boolean checkType() {
        return false;
    }

    @Override
    public int allocateMemory(Register register, int offset) {
        return 0;
    }

    @Override
    public Type getReturnType() {
        return null;
    }


}
