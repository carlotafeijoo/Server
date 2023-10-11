package Interfaces;

import java.io.File;

import POJOS.Elderly;
import POJOS.Doctor;

public interface XMLManager {
	public void staff2xml(Integer id);
	public void elderly2xml(Elderly e);
	public Elderly xml2Elderly(File xml);
	public Doctor xml2Staff(File xml);
	public void simpleTransform(String sourcePath, String xsltPath,String resultDir);
	

}
