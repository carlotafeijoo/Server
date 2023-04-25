package Interfaces;

import POJOS.FamilyContact;
import POJOS.Staff;

public abstract class FamilyContactManager {
	
	//add a family contact
	public abstract void addFamilyContact (FamilyContact familycontact);
	
	//show family contact info before updating
	public abstract Staff showFamilyContactInfo(int id);
	
	//update the info of a family contact
	public abstract void updateFamilyContactInfo(FamilyContact familycontact);
	
	
	
}
