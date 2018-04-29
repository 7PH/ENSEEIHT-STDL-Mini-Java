package fr.n7.stl.block.ast;

import fr.n7.stl.block.ast.instruction.Instruction;
import fr.n7.stl.block.ast.type.AtomicType;
import fr.n7.stl.tam.ast.Fragment;
import fr.n7.stl.tam.ast.TAMFactory;

import java.util.List;

public class RootBlock extends Block {
    /**
     * Constructor for the root block.
     * @param _instructions
     */
    public RootBlock(List<Instruction> _instructions) {
        super(_instructions);
    }

    @Override
    public boolean checkType() {
        return super.checkType() && getReturnType() == AtomicType.Wildcard;
    }
    
    @Override
    public Fragment getCode(TAMFactory factory) {
    	Fragment fragment = super.getCode(factory);
    	fragment.add(factory.createHalt());
    	return fragment;    	
    	
    }
}
