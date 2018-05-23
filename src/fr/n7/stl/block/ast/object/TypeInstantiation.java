package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

public class TypeInstantiation implements Type {

	private String name;

	private List<TypeInstantiation> typeInstantiations = new LinkedList<>();

	private Declaration declaration;


	public TypeInstantiation(String name) {
		this.name = name;
	}

	public TypeInstantiation(String _name, List<TypeInstantiation> _Type_instantiations) {
		this.name = _name;
		this.typeInstantiations = _Type_instantiations;
	}

	@Override
	public boolean equalsTo(Type other) {
		boolean b = false;
		if (other instanceof TypeInstantiation) {
			b = ((TypeInstantiation) other).name.equals(this.name);
		}
		return b;
	}
	
	@Override
	public boolean compatibleWith(Type _other) {
		/* this compatible avec _other si :
		 *  - this et _other sont de la même classe
		 * 	- this extends_other
		 * 	- this implémente _other et _other est une interface
		 *  - this est une interface et _other implémente 
		 * /!\ Verifier les types génériques
		 */

		if (_other instanceof TypeInstantiation) {
			TypeInstantiation _typeInst = (TypeInstantiation) _other;
			
			if (_typeInst.getDeclaration() instanceof InterfaceDeclaration) {
				/* _other est une interface. */				
				InterfaceDeclaration _interface = (InterfaceDeclaration) _typeInst.getDeclaration();
				
				if (this.getDeclaration() instanceof InterfaceDeclaration) {
					/* this est aussi une interface. */
					// TODO : Vérifier que this extends _other et vérifier les types génériques.
					
				} else if (this.getDeclaration() instanceof ClassDeclaration) {
					/* this est une classe */
					ClassDeclaration thisClass = (ClassDeclaration) this.getDeclaration();
					
					// TODO : Vérifier que this implémente _other
					
				} else {
					// TODO Cas erreur
					
					return false;
				}
				
			} else if (_typeInst.getDeclaration() instanceof ClassDeclaration) {
				/* _other est une classe. */				
				ClassDeclaration _classe = (ClassDeclaration) _typeInst.getDeclaration();
				
				if (this.getDeclaration().getName().equals(_classe.getName())) {
					/* C'est la même classe. */
					//TODO : Verifier les types génériques.
				} else {
					/* Ce n'est pas la même classe. */
					//TODO : Vérifier que this extends _other et vérifier les types génériques.
				}
			}
			
		} else {
			/* _other est un type atomique. */
			//TODO

		}

		return false;

	}

	@Override
	public Type merge(Type _other) {
		throw new SemanticsUndefinedException("merge method is undefined for TypeInstantiation.");
	}

	@Override
	public int length() {
		throw new SemanticsUndefinedException("length method is undefined for TypeInstantiation.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> _scope) {

		/* Important. */
		if (!_scope.contains(this.name)) {
			Logger.error("Could not resolve TypeInstantiation because the name " + this.name + " is not defined.");
			return false;
		} else {
			this.declaration = _scope.get(this.name);
		}
		//TODO

		throw new SemanticsUndefinedException("resolve method is undefined for TypeInstantiation.");
	}

	@Override
	public String toString() {
		return this.name + (this.typeInstantiations.size() > 0 ? ("<" + this.typeInstantiations + ">") : "");
	}


	public Declaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(Declaration declaration) {
		this.declaration = declaration;
	}

}
