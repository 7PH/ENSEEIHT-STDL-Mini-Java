package fr.n7.stl.block.ast.object;

import java.util.LinkedList;
import java.util.List;

import fr.n7.stl.block.ast.SemanticsUndefinedException;
import fr.n7.stl.block.ast.scope.Declaration;
import fr.n7.stl.block.ast.scope.HierarchicalScope;
import fr.n7.stl.block.ast.type.Type;
import fr.n7.stl.util.Logger;

public class InstanceType implements Type {

	private String name;

	/* Types génériques : <String> etc */
	private List<InstanceType> typeInstantiations = new LinkedList<>();

	private ProgramDeclaration declaration;

	public InstanceType(String name) {
		this.name = name;
	}

	public InstanceType(String name, List<InstanceType> instantiations) {
		this(name);
		this.typeInstantiations = instantiations;
	}

	@Override
	public boolean equalsTo(Type other) {		
		return this.equals(other);
	}
	
	@Override
	public boolean compatibleWith(Type other) {
		/* this compatible avec _other si :
		 *  - this et _other sont de la même classe
		 * 	- this extends_other
		 * 	- this implémente _other et _other est une interface
		 *  - this est une interface et _other implémente 
		 * /!\ Verifier les types génériques
		 */

		//La Declaration d'un InstanceType est TOUJOURS un ProgramDeclaration ?
		if (other instanceof InstanceType) {
			InstanceType _typeInst = (InstanceType) other;
			
			if (_typeInst.getDeclaration() instanceof InterfaceDeclaration) {
				/* _other est une interface. */
				InterfaceDeclaration _interface = (InterfaceDeclaration) _typeInst.getDeclaration();

				if (declaration instanceof InterfaceDeclaration) {
					/* this est aussi une interface. */
					// TODO : Vérifier que this extends _other et vérifier les types génériques.
					Logger.error("InstanceType compatibleWith : TODO #1");

				} else {
					/* this est une classe */
					ClassDeclaration thisClass = (ClassDeclaration) declaration;

					// Vérifier que this implémente _other
					if(thisClass.getImplementedClasses().contains(_interface)) {
						return true;
					} else {
						Logger.error(this.name + " is not compatible with " + _interface.toString());
						return false;
					}

				}


			} else if (_typeInst.getDeclaration() instanceof ClassDeclaration) {
				/* _other est une classe. */				
				ClassDeclaration _classe = (ClassDeclaration) _typeInst.getDeclaration();
				
				if (declaration.getName().equals(_classe.getName())) {
					/* C'est la même classe. */
					
					// Verifier les types génériques.					
					if (_typeInst.getTypeInstantiations().size() != this.getTypeInstantiations().size()) {
						Logger.error("InstanceType " + this.toString() + " is not compatible with " + _typeInst.toString() + " because one or several generic types are missing.");
						return false;
					} else {
						int i = 0;
						for (InstanceType _t : _typeInst.getTypeInstantiations()) {
							if(!this.getTypeInstantiations().get(i).compatibleWith(_t)) {
								Logger.error("InstanceType " + this.toString() + " is not compatible with " + _typeInst.toString() + " because generic type " + this.getTypeInstantiations().get(i) + " is not compatible with " + _t.toString() + ".");
								return false;
							}
						}
						// Les types génériques sont corrects.
						return true;
					}
					
				} else {
					/* Ce n'est pas la même classe. */
					// TODO : Vérifier que this extends _other et vérifier les types génériques.
					declaration.getExtendedClass().contains(_typeInst);
					// et les types generiques de _classe ont été instantiés
					Logger.error("InstanceType compatibleWith : TODO #2");
					return false;
				}
			}
			
		} else {
			/* _other est un type atomique. */
			// TODO : Compatibilité entre AtomicTypes et InstanceType
			Logger.error("Compatibility between AtomicTypes et instantiated types was not implemented.");
			return false;

		}

		Logger.error("Something went wrong.");
		return false;

	}

	@Override
	public Type merge(Type other) {
		throw new SemanticsUndefinedException("merge method is undefined for InstanceType.");
	}

	@Override
	public int length() {
		throw new SemanticsUndefinedException("length method is undefined for InstanceType.");
	}

	@Override
	public boolean resolve(HierarchicalScope<Declaration> scope) {
		if (! scope.knows(name)) {
			Logger.error("InstanceType: Unknown reference to " + name);
			return false;
		} else {
			if (scope.get(this.name) instanceof ProgramDeclaration) {
				ProgramDeclaration declaration = (ProgramDeclaration) scope.get(this.name);
				this.declaration = declaration;

				if (this.typeInstantiations.size() > 0) {
					/* Verifier que cette classe accepte les types génériques. */
					if (declaration.getClassName().getGenerics().size() == 0) {
						Logger.warning(this.name + " is a raw type. References to generic type should be parameterized.");
						return true;
					} else if (declaration.getGenerics().size() == this.typeInstantiations.size() ) {
						for (int i = 0; i < this.typeInstantiations.size(); i++) {
							// TODO : Verifier que les types generiques sont compatibles.
							// ex : si déclaré MyClass<T extends X>
							// declaration[i].compatibleWith(typeInstantiations[i])
						}
						return true;
					} else {
						Logger.error("Incorrect number of arguments for type " + this.name + ".");
						return false;
					}
				} else {
					return true;
				}
			} else {
				Logger.error(this.name + " is not a class nor an interface and cannot be instantiated.");
				return false;
			}
		}
	}

	@Override
	public String toString() {
		return this.name + (this.typeInstantiations.size() > 0 ? ("<" + this.typeInstantiations + ">") : "");
	}


	public ProgramDeclaration getDeclaration() {
		return declaration;
	}

	public void setDeclaration(ProgramDeclaration declaration) {
		this.declaration = declaration;
	}

	public List<InstanceType> getTypeInstantiations() {
		return typeInstantiations;
	}

	/** Say if the object contains this attribute.
	 * @param name the searched attribute
	 * @return true if contains, false if not
	 */
	public boolean contains(String name) {
		if (this.getDeclaration() instanceof InterfaceDeclaration) {
			Logger.error("Interface " + this.name + " does not contain attribute " + name + " because " + this.name + " is an interface.");
			return false;
		} else if (this.getDeclaration() instanceof ClassDeclaration) {
			ClassDeclaration _classe = (ClassDeclaration) this.getDeclaration();
			// TODO : check if attribute is present
		} else {
			Logger.error("This message should not appear. Contains method was called on a InstanceType that was initiated with a declaration that is not a ProgramDeclaration");
			return false;
		}
		return false;
	}

	/** Get an attribute of the object.
	 * @param attributeIdentificateur the attribute we ask.
	 * @return the attribute
	 */
	public AttributeDefinition get(String attributeIdentificateur) {
		// TODO :
		// Get the objet : ClassDeclaration
		//	Get the attribute list of it
		//	  Get the correct attribute thanks to his name field
		return null;
	}
	
	@Override
	public boolean equals(Object _o) {
		if (!(_o instanceof InstanceType)) {
			return false;
		} else {
			InstanceType _typeO = (InstanceType) _o;
			if (this.name.equals(_typeO) && this.typeInstantiations.equals(_typeO.getTypeInstantiations())) {
				// TODO ? Do we need to check if it refers to the same declaration ?
				return true;
			} else {
				return false;
			}
		}
		
	}

}
