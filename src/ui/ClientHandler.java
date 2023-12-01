package ui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import Interfaces.DoctorManager;
import Interfaces.ElderlyManager;
import Interfaces.TaskManager;
import Interfaces.UserManager;
import POJOS.Doctor;
import POJOS.Elderly;
import POJOS.Role;
import POJOS.Task;
import POJOS.User;

public class ClientHandler implements Runnable {
    
	private Socket so;
    private UserManager userManager;
    private DoctorManager doctorManager;
    private ElderlyManager elderlyManager;
    private TaskManager tasksManager;

    public ClientHandler(Socket so, UserManager userManager, DoctorManager doctorManager, 
    		ElderlyManager elderlyManager, TaskManager tasksManager) {
    	
        this.so = so;
        this.userManager = userManager;
        this.doctorManager = doctorManager;
        this.elderlyManager = elderlyManager;
        this.tasksManager = tasksManager;
    }
    

    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException | NoSuchAlgorithmException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    private void handleClientConnection() throws IOException, NoSuchAlgorithmException, ParseException {
    	
    	 BufferedReader br = new BufferedReader(new InputStreamReader(so.getInputStream()));
         OutputStream os = so.getOutputStream();
         PrintWriter pw = new PrintWriter(os, true);
         DataInputStream dis = new DataInputStream(so.getInputStream());
         BufferedInputStream bis = new BufferedInputStream(so.getInputStream());
         BufferedOutputStream bos = null;
         FileOutputStream fos = null;
         
         System.out.println(ServerMain.clientCounter + " :num clients");

			String line;

			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				
				if (line.contains("stop")) {
					
					releaseResources(pw, br, os, so);
					System.out.println("Conexion with the client ended");
					
					ServerMain.clientCounter--;
					System.out.println(ServerMain.clientCounter + "eliminado");
					
					if (ServerMain.clientCounter==0) {
						ServerMain.switchServerOFF();
					}

					break;
				}

				//DOCTOR
				else if (line.contains("addDoctor")) {// Client wants to add a doctor

					System.out.println(line);

					String username = br.readLine();
					String password = br.readLine();
					String doctor_text = br.readLine();
					System.out.println(doctor_text);

					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(password.getBytes());
					byte[] digest = md.digest();

					// CREATE doctor AND ADD TO JPA
					User u = new User(username, digest);
					Role role = userManager.getRole("Doctor");
					u.setRole(role);
					role.addUser(u);
					userManager.newUser(u);

					//Receive Doctor
					try {

						//From Doctor TEXT to Doctor OBJECT
						Doctor doctor;
						doctor = new Doctor(doctor_text);

						//Add doctor to db
						doctorManager.addDoctorMember(doctor);

					} catch (ParseException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					pw.println("doctor added");


				}else if(line.contains("checkPassword")) {
					String username = br.readLine();
					String password = br.readLine();
					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(password.getBytes());
					byte[] digest = md.digest();
					User u = userManager.checkPassword(username, digest);
					try {
						pw.println(u.getRole().toString());
						pw.println(u.toString());
					}catch(Exception e) {
						System.out.println("no existe el usuario");
						e.printStackTrace();
					}

				}else if(line.contains("searchDoctorIdfromUId")) {
					String id_text=br.readLine();
					int id=Integer.parseInt(id_text);
					int doctor_id =  doctorManager.searchDoctorIdfromUId(id);
					pw.println(""+doctor_id);


				}else if(line.contains("searchDoctorbyId")) {
					String id_text = br.readLine();
					int id = Integer.parseInt(id_text);
					Doctor doctor = doctorManager.searchDoctorbyId(id);
					pw.println(doctor.toString());


				}else if(line.contains("updateDoctorMemberInfo")) {
					String doctorObject_string = br.readLine();
					System.out.println(doctorObject_string);
					Doctor doctorToUpdate = new Doctor(doctorObject_string);
					doctorManager.updateDoctorMemberInfo(doctorToUpdate);

				}else if(line.contains("getListOfTasksByDoctorFromElder")) {
					//HACER
					String id_text = br.readLine();
					int id_doc = Integer.parseInt(id_text);
					String id_text2 = br.readLine();
					int id_elder = Integer.parseInt(id_text2);
					List<Task> listtasks = tasksManager.getListOfTasksByDoctorFromElderly(id_doc,id_elder);
					pw.println(""+listtasks.size());
					for (Task listtask : listtasks) {
						pw.println(listtask);
					}

				}else if(line.contains("getListOfElderlyByDoctorID")) {
					//HACER
					int idDoctor = Integer.parseInt(br.readLine());
					List<Elderly> elderlys = elderlyManager.getListOfElderlyByDoctorID(idDoctor);
					pw.println(""+elderlys.size());

					for (Elderly elderly : elderlys) {
						pw.println(elderly);
					}


				}else if(line.contains("addTask")) {
					String taskObject_string = br.readLine();
					Task newTask = new Task (taskObject_string);
					try {
						tasksManager.addTask(newTask);
						pw.println("task added");
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}


				//ELDERLY
				else if(line.contains("addElderly")) {
					System.out.println(line);

					String username = br.readLine();
					String password = br.readLine();
					String elderly_text = br.readLine();
					System.out.println(elderly_text);

					MessageDigest md = MessageDigest.getInstance("MD5");
					md.update(password.getBytes());
					byte[] digest = md.digest();

					// CREATE elderly AND ADD TO JPA
					User u = new User(username, digest);

					Role role = userManager.getRole("Elderly");
					u.setRole(role);
					role.addUser(u);
					userManager.newUser(u);
					
					// Receive elderly
					try {

						//From elderly TEXT to elderly OBJECT
						Elderly elderly;
						elderly = new Elderly(elderly_text);

						//Add elderly to DB
						elderlyManager.addElderly(elderly);

					} catch (ParseException e) {
						e.printStackTrace();
					}
					pw.println("elderly added");


				}else if(line.contains("searchAllDoctors")) {
					ArrayList<Doctor> doctores = doctorManager.searchAllDoctors();
					pw.println(""+doctores.size());

					for (Doctor doctore : doctores) {
						pw.println(doctore);
					}


				}else if(line.contains("searchElderlyIdfromUId")) {
					String id_text=br.readLine();
					int id=Integer.parseInt(id_text);
					int elderly_id =  elderlyManager.searchElderlyIdfromUId(id);
					pw.println(""+elderly_id);


				}else if(line.contains("searchElderlyById")) {
					//System.out.println("dddd");

					String id_text = br.readLine();
					//System.out.println("dddd");

					int id = Integer.parseInt(id_text);
					//System.out.println("dddd");

					Elderly elderly = null;
					try {
						elderly = elderlyManager.searchElderlyById(id);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("elderly"+elderly.toString());
					pw.println(elderly.toString());


					
				}else if(line.contains("searchElderlyNameById")) {

						String eld_id_text = br.readLine();
						int eld_id = Integer.parseInt(eld_id_text);

						Elderly elderly = null;
						try {
							elderly = elderlyManager.searchElderlyById(eld_id);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//System.out.println("elderly"+elderly.toString());
						//System.out.println(elderly.getName());
						pw.println(elderly.getName());
						
						if(line.contains("seeTasksandId")) {
							
							List<Task> listtasks = elderlyManager.seeTaskANDidbyElderly(eld_id);
							pw.println(""+listtasks.size());
							
							for (Task listtask : listtasks) {
								pw.println(listtask);

							}
						}
						
							else if (line.contains("searchTaskDurationByELDid")){
							
							String task_id_text = br.readLine();
							int task_id = Integer.parseInt(task_id_text);
							
							Task task = tasksManager.getTask(task_id);
							int recordDuration = task.getDuration();
							
							pw.println(recordDuration);
							
							
						}
						
						//leemos el nombre del fichero
						String file_name = br.readLine();
						
				        String diract = System.getProperty("user.dir"); 
				        String dirfolder = diract +"\\recordstxt";
				    	
				    	File archivo = new File(dirfolder, file_name);
				    	
				    	// a partir de aqui he usado el codigo de Java para escribir en un fichero
				        PrintWriter printwriter = null;
				        try {
				            printwriter = new PrintWriter(archivo);
				            String stringleido;
				            //leemos toda la señal (recordar que esta separada por comas)
				            stringleido = br.readLine();
				           
				            //queremos que los datos se escriban con intros
				            //(asi es como está en el txt generado de bitalino)
				            //tenemos que sustituir todas las comas por \n
				            String signal = convertCommaIntoLines(stringleido);
				            //signal ya esta bien en cuanto a formato, asi que lo escribimos en el fichero
				            printwriter.println(signal);

						} catch (IOException ioe) {
				            System.out.println("Error" + ioe);
				        } finally {
				            if (printwriter != null) {
				                printwriter.close();
				            }

				        }

				}else if(line.contains("seeTasks")) {
					
					String id_text = br.readLine();
					int id_elder = Integer.parseInt(id_text);
					
					List<Task> listtasks = elderlyManager.seeTasksbyElderly(id_elder);
					pw.println(""+listtasks.size());
					
					for (Task listtask : listtasks) {
						pw.println(listtask);

					}


				}
			}
		}
	//}
    
    private static String convertCommaIntoLines(String stringleido) {
    	String signal = stringleido.replace(",", "\n");
    	return signal;
    }

    private static void releaseResources(PrintWriter printWriter, BufferedReader br, OutputStream outputStream, Socket socket) {
        printWriter.close();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
   }
    
}