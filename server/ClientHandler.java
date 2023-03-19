import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler extends Thread {
    private Socket socket = null;
    private static BufferedReader in;
    private static PrintWriter out;

    public ClientHandler(Socket socket) throws IOException {
        try {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            System.err.println("Error");
            e.printStackTrace();
            socket.close();
            in.close();
            out.close();
        }
    }


    // function to print information about the list
    public static void returnListInfo() {
        int count;
        out.println("there are "+Server.matrix.length+ " list each with a maximum of " +Server.matrix[0].length );
        for (int i = 0; i < Server.matrix.length; i++) {
            count = 0;
            for (int j = 0; j < Server.matrix[i].length; j++) {
                if (Server.matrix[i][j] != null) {
                    count += 1;
                }
            }
            out.println("list "+ (i+1) +" has " +  count + " member(s)");
        }
        out.println("exit");
    }

    //function to store the members in a specified list.
    public static void joinMembers(int list_num,String member) {
        String s = "Failed";
        if(list_num<Server.matrix.length){
            for (int j = 0; j < Server.matrix[list_num].length; j++) {
                if (Server.matrix[list_num][j] == null) {
                    Server.matrix[list_num][j] = member;
                    s = "Success";
                    out.println(s);
                    break;
                }
            }
        }
        if(!(s.equals("Success"))){
            out.println("Failed");
        }
        out.println("exit");
    }

    //function to get members from a specified list.
    public void getListMembers( int list) {
        if(list < Server.matrix.length) {
            for (int i = 0; i < Server.matrix.length; i++) {
                for (int j = 0; j < Server.matrix[i].length; j++) {
                    if (i == list && Server.matrix[i][j] != null) {
                        out.println(Server.matrix[i][j] + "\n");
                    }
                }
            }
        }
        else{
            out.println("invalid command");
        }
        out.println("exit");
    }

    // function to log users activity in a text file.
    private void Logging(String request){
        // Logging.
        InetAddress inet = socket.getInetAddress();
        Date date = new Date();
        SimpleDateFormat Date= new SimpleDateFormat("dd-MM-yyyy|H:mm:ss a");

        try {
            File f = new File("log.txt");

            BufferedWriter w = new BufferedWriter(new FileWriter(f, true));
            w.write(Date.format(date) + "|" + inet.getHostName() + "|" + request );
            w.newLine();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //function to handle all the inputs.
    private void handleInputs(String processedInput, Protocol p){
        if(processedInput.equalsIgnoreCase("totals")){
            returnListInfo();
            Logging(processedInput);
        } else if (processedInput.equalsIgnoreCase("join")) {
            joinMembers(p.getListNum(),p.getMemberName());
            Logging(processedInput);
        } else if (processedInput.equalsIgnoreCase("list")) {
            getListMembers(p.getListNum());
            Logging(processedInput);
        } else if(processedInput.equalsIgnoreCase("error")){
            out.println("Invalid command, please enter a valid command");
            out.println("exit");
        }

    }

    public void run() {
        try{
            String inputLine = in.readLine();

            // Initialise a protocol object for this client.
            Protocol p = new Protocol();
            String input = p.processInput(inputLine);
            handleInputs(input, p);

        }catch(IOException e){
            try {
                in.close();
                out.close();
                System.err.println("error");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
