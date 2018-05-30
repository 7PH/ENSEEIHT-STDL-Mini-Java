package fr.n7.stl.block.ast;

import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.instruction.Return;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.scope.SymbolTable;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.Register;
import fr.n7.stl.tam.ast.TAMFactory;
import fr.n7.stl.util.Logger;

import java.util.List;

/** 
 * Represents a Block node in the ABSTRACT Syntax Tree node for the Bloc language.
 * Declares the various semantics attributes for the node.
 * 
 * A block contains declarations. It is thus a Scope even if a separate SymbolTable is used in
 * the attributed semantics in order to manage declarations.
 */
public class Block {

	/** Sequence of instructions contained in a block. */
	protected List<Instruction> instructions;

	protected int allocated;

    private int offset;

    /**
	 * Constructor for a block.
	 */
	public Block(List<Instruction> instructions) {
		this.instructions = instructions;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String _local = "";
		for (Instruction _instruction : this.instructions) {
			_local += _instruction;
		}
		return "{\n" + _local + "}\n" ;
	}
	
	/**
	 * Inherited Semantics attribute to check that all identifiers have been defined and
	 * associate all identifiers uses with their definitions.
	 * @param _scope Inherited Scope attribute that contains the defined identifiers.
	 * @return Synthesized Semantics attribute that indicates if the identifier used in the
	 * block have been previously defined.
	 */
	public boolean resolve(HierarchicalScope<Declaration> _scope) {
		HierarchicalScope<Declaration> newScope = new SymbolTable(_scope);

		boolean reachedReturn = false;
        int instructionsAfterReturn = 0;
        for (Instruction instruction: instructions) {
		    if (! instruction.resolve(newScope))
		        return false;

		    if (reachedReturn)
                ++ instructionsAfterReturn;

		    reachedReturn |= instruction instanceof Return;
        }

        if (instructionsAfterReturn > 0)
            Logger.warning("Unreachable code detected");

		return true;
	}

	public Type getReturnType() {
	    Type res = AtomicType.Wildcard;
	    for (Instruction instruction: instructions) {
	        Type instructionType = instruction.getReturnType();
	        if (instructionType == AtomicType.VoidType) continue;
	        res = res.merge(instructionType);
        }
	    return res;
    }

	/**
	 * Synthesized Semantics attribute to check that an instruction if well typed.
	 * @return Synthesized True if the instruction is well typed, False if not.
	 */	
	public boolean checkType() {
	    for (Instruction instruction: instructions) {
	        if (! instruction.checkType()) {
	            Logger.error("Type error for instruction " + instruction);
                return false;
            }
        }
        return true;
	}

	/**
	 * Inherited Semantics attribute to allocate memory for the variables declared in the instruction.
	 * Synthesized Semantics attribute that compute the size of the allocated memory. 
	 * @param offset Inherited Register associated to the address of the variables.
	 * @param offset Inherited Current offset for the address of the variables.
	 */	
	public void allocateMemory(Register register, int offset) {
        this.offset = offset;
	    int currentOffset = offset;
	    for (Instruction instruction: instructions)
            currentOffset += instruction.allocateMemory(register, currentOffset);
        allocated = currentOffset - offset;
	}

	/**
	 * Inherited Semantics attribute to build the nodes of the abstract syntax tree for the generated TAM code.
	 * Synthesized Semantics attribute that provide the generated TAM code.
	 * @param factory Inherited Factory to build AST nodes for TAM code.
	 * @return Synthesized AST for the generated TAM code.
	 */
	public Fragment getCode(TAMFactory factory) {
        Fragment fragment = factory.createFragment();

        for (Instruction instruction: instructions)
            fragment.append(instruction.getCode(factory));

        fragment.add(factory.createPop(0, allocated));

        return fragment;
	}

    public int getOffset() {
        return offset;
    }

    public int getAllocated() {
        return allocated;
    }
}