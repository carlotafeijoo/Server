package ResidencialAreaXML;

import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.internal.oxm.schema.model.List;

import Interfaces.XMLManager;
import POJOS.Elderly;
import POJOS.Doctor;
import jdbc.JDBCManager;

public class XMLManagerImpl implements XMLManager{
	JDBCManager manager;
	
	@Override
	public void staff2xml(Integer id) {
		Doctor s= null;
		manager= new JDBCManager();
		
		try {
			//DO A SQL QUERY TO GET THE OWNER BY THE ID
			Statement stmt= manager.getConnection().createStatement(); 
			String sql= "SELECT * FROM Staff WHERE staff_id ="+id;
			ResultSet rs= stmt.executeQuery(sql); 
			String name= rs.getString("name");
			Integer phone= rs.getInt("phone");
			Date dob = rs.getDate("dob");
			String address= rs.getString("address");
			s= new Doctor(name,phone,dob,address);
			System.out.println(s);
			rs.close();
			stmt.close();
			
			//EXPORTS THE OWNER TO THE XML FILE
			JAXBContext jaxbContext= JAXBContext.newInstance(Doctor.class);
			Marshaller marshaller= jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			File file= new File ("./xmls/Staff.xml"); //ir exportando cada para no sobreescribir
			//concatenando el id
			marshaller.marshal(s, file);
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		
	}

	@Override
	public void elderly2xml(Elderly el) {
		try {
			
			//EXPORTS THE OWNER TO THE XML FILE
			JAXBContext jaxbContext= JAXBContext.newInstance(Elderly.class);
			Marshaller marshaller= jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			File file= new File ("./xmls/Elderly.xml"); //ir exportando cada para no sobreescribir
			marshaller.marshal(el, file);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Elderly xml2Elderly(File xml) {
		Elderly el= null;
		try {
			JAXBContext jaxbContext= JAXBContext.newInstance(Elderly.class);
			Unmarshaller unmarshaller= jaxbContext.createUnmarshaller();
			el= (Elderly) unmarshaller.unmarshal(xml);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return el;
		
	}
	@Override
	public Doctor xml2Staff(File xml) {
		Doctor s= null;
		try {
			JAXBContext jaxbContext= JAXBContext.newInstance(Doctor.class);
			Unmarshaller unmarshaller= jaxbContext.createUnmarshaller();
			s= (Doctor) unmarshaller.unmarshal(xml);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return s;
	}
	public void simpleTransform(String sourcePath, String xsltPath,String resultDir) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = tFactory.newTransformer(new StreamSource(new File(xsltPath)));
			transformer.transform(new StreamSource(new File(sourcePath)),new StreamResult(new File(resultDir)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
