package Interfaces;

import POJOS.FamilyContact;

public interface FamilyContactManager {
	
	//add a family contact
	public static void addFamilyContact (FamilyContact familycontact) {
		// TODO Auto-generated method stub
		
	}
	
	//show family contact info before updating
	public static FamilyContact showFamilyContactInfo(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//update the info of a family contact
	public void updateFamilyContactInfo(FamilyContact familycontact);
	
	public FamilyContact searchFamilyContactbyId ( int id);
	
	
}
