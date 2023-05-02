package Interfaces;

import POJOS.FamilyContact;

public interface FamilyContactManager {
	
	//add a family contact
	public void addFamilyContact (FamilyContact familycontact);
	
	//show family contact info before updating
	public FamilyContact showFamilyContactInfo(int id);
	
	//update the info of a family contact
	public void updateFamilyContactInfo(FamilyContact familycontact);
	
	
	
	
	
}
