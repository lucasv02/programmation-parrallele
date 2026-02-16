package Socket;
import java.io.*;
import java.net.*;
/** Master is a client. It makes requests to numWorkers.
 *   
 */
public class MasterSocket {
    static int maxServer = 8;
    static final int[] tab_port = {25545,25546,25547,25548,25549,25550,25551,25552};
    static String[] tab_total_workers = new String[maxServer];
    static final String ip = "127.0.0.1";
    static BufferedReader[] reader = new BufferedReader[maxServer];
    static PrintWriter[] writer = new PrintWriter[maxServer];
    static Socket[] sockets = new Socket[maxServer];
    
    
    public static void main(String[] args) throws Exception {

	// MC parameters
	int nTotal = 16000000; // total number of throws
	int total = 0; // total number of throws inside quarter of disk
	double pi; 
	int numWorkers = maxServer;
	boolean isStrongScalability = true;
	BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	String s; // for bufferRead

	System.out.println("#########################################");
	System.out.println("# Computation of PI by MC method        #");
	System.out.println("#########################################");
	
	System.out.println("\n How many workers for computing PI (< maxServer): ");
	try{
	    s = bufferRead.readLine();
	    numWorkers = Integer.parseInt(s);
	    System.out.println(numWorkers);
	    
	    System.out.println("Scalabilité forte (s) ou Scalabilité faible (w) ? ");
	    s = bufferRead.readLine();
	    if (s.equalsIgnoreCase("w")) {
		isStrongScalability = false;
	    }
	    System.out.println("Scalability: " + (isStrongScalability ? "Strong" : "Weak"));
	}
	catch(IOException ioE){
	   ioE.printStackTrace();
	}
	
	for (int i=0; i<numWorkers; i++){
	    System.out.println("Enter worker"+ i +" port : ");
	    try{
		s = bufferRead.readLine();
		System.out.println("You select " + s);
	    }
	    catch(IOException ioE){
		ioE.printStackTrace();
	    }
	}

       //create worker's socket
       for(int i = 0 ; i < numWorkers ; i++) {
	   sockets[i] = new Socket(ip, tab_port[i]);
	   System.out.println("SOCKET = " + sockets[i]);

	   // Interface pour avoir le flux d'entrée du socket
	   reader[i] = new BufferedReader( new InputStreamReader(sockets[i].getInputStream()));
	   // Interface vers le flux de sortie du socket
	   writer[i] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sockets[i].getOutputStream())),true);
       }

       int totalCountPerWorker;
       if (isStrongScalability) {
	   totalCountPerWorker = nTotal / numWorkers;
       } else {
	   totalCountPerWorker = nTotal;
       }
       
       String message_to_send;
       message_to_send = String.valueOf(totalCountPerWorker);

       String message_repeat = "y";

       long stopTime, startTime;
       
       File csvFile = new File("./Fichier/socket_result.csv");
       boolean fileExists = csvFile.exists();
       PrintWriter csvWriter = new PrintWriter(new BufferedWriter(new FileWriter(csvFile, true)));
       
       if (!fileExists || csvFile.length() == 0) {
           csvWriter.println("Notal;TotalCount;P;TPS;Erreur");
       }

       while (message_repeat.equals("y")){

	   startTime = System.nanoTime();
	   // initialize workers
	   for(int i = 0 ; i < numWorkers ; i++) {
	       writer[i].println(message_to_send);          // send a message to each worker
	   }
	   
	   //listen to workers's message 
	   for(int i = 0 ; i < numWorkers ; i++) {
	       tab_total_workers[i] = reader[i].readLine();      // read message from server
	       System.out.println("Client sent: " + tab_total_workers[i]);
	   }
	   
	   // compute PI with the result of each workers
	   total = 0; // Reset total to 0 for each repetition
	   for(int i = 0 ; i < numWorkers ; i++) {
	       total += Integer.parseInt(tab_total_workers[i]);
	   }
	   pi = 4.0 * total / totalCountPerWorker / numWorkers;

	   stopTime = System.nanoTime();
	   
	   System.out.println("PI = " + pi);

	   // Écriture dans le CSV : Notal;TotalCount;P;TPS;Erreur
	   StringBuilder line = new StringBuilder();
	   line.append(totalCountPerWorker * numWorkers).append(";"); // Notal (total de points pour tous les workers)
	   line.append(totalCountPerWorker).append(";");             // TotalCount (par worker)
	   line.append(numWorkers).append(";");            // P (nombre de processus/workers)
	   line.append(stopTime - startTime).append(";");  // TPS (durée en nano-secondes)
	   line.append(Math.abs((pi - Math.PI)) / Math.PI); // Erreur
	   csvWriter.println(line.toString());
	   csvWriter.flush();

	   System.out.println("\n Repeat computation (y/N): ");
	   try{
	       message_repeat = bufferRead.readLine();
	       System.out.println(message_repeat);
	   }
	   catch(IOException ioE){
	       ioE.printStackTrace();
	   }
       }
       
       for(int i = 0 ; i < numWorkers ; i++) {
	   System.out.println("END");     // Send ending message
	   writer[i].println("END") ;
	   reader[i].close();
	   writer[i].close();
	   sockets[i].close();
       }
       csvWriter.close();
   }
}