/**
 * 
 */
package fr.n7.stl.block.ast.instruction;

import fr.n7.stl.block.ast.expression.Expression;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.NamedType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Library;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;

import static fr.n7.stl.block.ast.type.AtomicType.*;


/**
 * Implementation of the ABSTRACT Syntax Tree node for a printer instruction.
 * @author Marc Pantel
 *
 */
public class Printer implements Instruction {

	protected Expression parameter;

	protected Type parameterType;

	public Printer(Expression _value) {
		this.parameter = _value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "print " + this.parameter + ";\n";
	}
	
	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.instruction.Instruction#resolve(fr.n7.stl.block.ast.scope.Scope)
	 */
	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		return parameter.resolve(_scope);
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#checkType()
	 */
	@Override
	public boolean checkType() {
        parameterType = parameter.getType();
        while (parameterType instanceof NamedType)
            parameterType = ((NamedType)parameterType).getType();
        boolean b = parameterType == IntegerType
                || parameterType == BooleanType
                || parameterType == CharacterType
                || parameterType == StringType;
        if (!b)
        	throw new RuntimeException("You print a wrong type : " + parameterType.toString());
        return b;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#allocateMemory(fr.n7.stl.tam.ast.Register, int)
	 */
	@Override
	public int allocateMemory(Register register, int offset) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see fr.n7.stl.block.ast.Instruction#getCode(fr.n7.stl.tam.ast.TAMFactory)
	 */
	@Override
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();

        if (AtomicType.IntegerType.equalsTo(parameterType)) {
            // int
            fragment.append(parameter.getCode(factory));
            fragment.add(Library.IOut);
        } else if (AtomicType.BooleanType.equalsTo(parameterType)) {
            // bool
            fragment.append(parameter.getCode(factory));
            fragment.add(Library.B2I);
            fragment.add(Library.IOut);
        } else if (AtomicType.CharacterType.equalsTo(parameterType)) {
            // char
            fragment.add(factory.createLoadL('\''));
            fragment.add(Library.COut);
            fragment.append(parameter.getCode(factory));
            fragment.add(Library.COut);
            fragment.add(factory.createLoadL('\''));
            fragment.add(Library.COut);
        } else if (AtomicType.StringType.equalsTo(parameterType)) {
            // str
            fragment.add(factory.createLoadL('\"'));
            fragment.add(Library.COut);
            fragment.append(parameter.getCode(factory));
            fragment.add(Library.SOut);
            fragment.add(factory.createLoadL('\"'));
            fragment.add(Library.COut);
        }

        fragment.add(factory.createLoadL("\\n"));
        fragment.add(Library.SOut);
        return fragment;
	}

	@Override
	public Type getReturnType() {
		return VoidType;
	}

}
