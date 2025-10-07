
package main;
import UserAuthentication.Authentication;
import config.config;
//import static config.config.viewUsers;
import java.util.Scanner;
public class mainCode {
     public static void viewUsers() {
        String Query = "SELECT * FROM tbl_users";
        
        String[] membershipHeaders = {"ID", "Name", "Role", "Email", "Status"};
        String[] membershipColumns = {"u_id", "u_name", "u_role", "u_email", "u_status"};
        config con = new config();
        con.viewRecords(Query, membershipHeaders, membershipColumns);
    }
    
    public static Scanner inp = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        
        Authentication aut = new Authentication();
        
        System.out.println("\nWELCOME TO MY SYSTEM!");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit\n");
        
        System.out.print("Choose an option: ");
        int option = inp.nextInt();
        inp.nextLine();
        
            switch(option){

                case 1:
                    aut.addLogin();
                    break;
                case 2:
                    aut.register();
                    break;
                case 3:
                    System.out.println("Exit......");
                    break;

                default: 
                    System.out.println("Invalid inputed, Try Again.");
                    break;
                    
            }
        
        

    }
    
    
    
    
     //public static void adminDashboard(){

        //int aId = Authentication.loggedInUserId;
        //manageUser mu = new manageUser();
       // manageProduct mp = new manageProduct();
       // manageSale ms = new manageSale();
       // manageInventory mi = new manageInventory();
        
        
        //System.out.println("==ADMIN DASHBOARD==");
        
        
    //}
    //public static void TrainerDashboard(){
        
        //createSale cs = new createSale();
        //manageProduct mp = new manageProduct();
        //viewMySale vs = new viewMySale();
        
        //String res;
        //do{
         //   System.out.println("==TRAINER DASHBOARD==");
          //  System.out.println("1. Sell Product");
          //  System.out.println("2. View Products");
          //  System.out.println("3. View My Sales");
          //  System.out.println("4. Logout\n");

          //  System.out.print("Choose an option: ");
          //  int option = inp.nextInt();
          //  inp.nextLine();

          //  switch(option){

            //    case 1:
                    //cs.createSale();
              //      break;

                //case 2:
                    //mp.viewProduct();
                    //break;

                //case 3:
                    //vs.viewSale();
                    //break;

                //case 4:
                   // main(null);

                //default: System.out.println("\nInvalid input, Try again.");
                //TrainerDashboard();
            //}
            //System.out.print("\nDo You Want To Continue (yes / no): ");
           // res = mainCode.inp.next();
       //}while(res.equals("yes") || res.equals("1"));
        //System.out.println("Back to Main");
        //main(null);
    //}
}

