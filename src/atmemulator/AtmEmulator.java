package atmemulator;

import java.util.Scanner;
import com.mysql.jdbc.Driver;
import java.sql.*;
import java.io.*;

public class AtmEmulator {
    
    private static Scanner in = new Scanner(System.in);
    private static Statement s;
    private static String query;

    public static void main(String[] args) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm", "root", "1234");
            System.out.println("Connection Established");
            s = con.createStatement();

            do {
                System.out.println("1. Admin login");
                System.out.println("2. User login");
                System.out.println("3. Exit");
                int ch = in.nextInt();

                switch(ch) {
                    case 1: adminLogin(con);
                            break;
                    case 2: userLogin(con);
                            break;
                    case 3: System.exit(0);
                    default:System.out.println("Invalid choice!!!");
                }
                
            }while(true);
        }
        catch(Exception e) {
            System.out.println("Exception : " + e);
        }
    }
    
    static void adminLogin(Connection conn)throws SQLException, InterruptedException {
        
        boolean check = false;
        System.out.print("Enter Adimin Id : ");
        String name=in.next();
        System.out.print("Enter password! : ");
        String pass=in.next();
        s=conn.createStatement();
        query="SELECT * from admin;";
        ResultSet rset=s.executeQuery(query);
        while(rset.next())
        {
            if(rset.getString("AdminId").equals(name) && rset.getString("Password").equals(pass))
                check=true;
        }
        
        if(check) {
            
            System.out.println("Admin login successful");
            
                while(true)
                {
                    System.out.println("1. Add a user");
                    System.out.println("2. Delete a user");
                    System.out.println("3. Add money to a user account");
                    System.out.println("4. Update details of a user");
                    System.out.println("5. Add money to atm");
                    System.out.print("Enter your choice : ");
                    int ch=in.nextInt();
                    switch(ch)
                    {
                        case 1:
                            System.out.println("\nEnter the account number : ");
                            String acn1=in.next();
                            System.out.print("Enter the pin : ");
                            int p1=in.nextInt();
                            System.out.print("Enter the user name : ");
                            String n1 =in.next();
                            System.out.print("Enter the amount : ");
                            float b1=in.nextFloat();
                            query="INSERT into user VALUES('"+ acn1 +"',"+ p1 +",'"+ n1 +"',"+ b1 +")";
                            s.executeUpdate(query);
                            System.out.println("Details of new user added successfully!");
                            break;


                        case 2:
                            System.out.print("\nEnter the account number of the user to delete : ");
                            int f=-1;
                            String acn2=in.next();
                            query="SELECT AccountNo from user;";
                            ResultSet rs = s.executeQuery(query);
                            while(rs.next())
                            {
                                if(rs.getString("AccountNo").equals(acn2))
                                    f=1;
                            }
                            if(f==1)
                            {
                                query="DELETE fROM user WHERE AccountNo='"+ acn2 +"';";
                                s.executeUpdate(query);
                                System.out.println("User deleted successfully!!!");
                            }
                            else
                                System.out.println("Sorry, the account number does not exist!!!");
                            break;



                        case 3:
                            System.out.print("Enter the account number of the user : ");
                            String acn3=in.next();
                            float balance=-1;
                            String q3="SELECT bal from user where AccountNo='"+ acn3 +"';";
                            ResultSet rs1 = s.executeQuery(q3);
                            while(rs1.next())
                            {
                                balance=rs1.getFloat("Amount");
                            }

                            if(balance!=-1)
                            {
                            System.out.print("Enter the amount you have to add : ");
                            float b=in.nextFloat();
                            balance+=b;
                            String q31="UPDATE user SET Amount="+ balance +"where AccountNo='"+ acn3 +"';";
                            s.executeUpdate(q31);
                            System.out.println("Balance updated successfully!!!");
                            }
                            else
                                System.out.println("Sorry, the account number does not exist!");

                            break;


                        case 4:
                            System.out.println("Enter the account number ");
                            int f2=-1;
                            String acn4=in.next();
                            String q41="SELECT acctno from user;";
                            ResultSet rs2 = s.executeQuery(q41);
                            while(rs2.next())
                            {
                                if(rs2.getString("AccountNo").equals(acn4))
                                    f2=1;
                            }
                            if(f2==1)
                            {
                            System.out.println("Enter new user name!");
                            String n2=in.next();
                            String q4="UPDATE user SET UserName='"+ n2 +"'where AccountNo='"+ acn4 +"';"; 
                            s.executeUpdate(q4);
                            System.out.println("Details updated successfully!");
                            }
                            else
                                System.out.println("Sorry, the account number does not exist!");
                            break;

                        case 5:
                            int dbnote1=0,dbnote2=0,dbnote3=0,dbnote4=0;
                            System.out.println("Enter number of thousand notes!");
                            int note1=in.nextInt();
                            System.out.println("Enter number of five hundred notes!");
                            int note2=in.nextInt();
                            System.out.println("Enter number of hundred notes!");
                            int note3=in.nextInt();
                            System.out.println("Enter number of fifty notes!");
                            int note4=in.nextInt();
                            String qnote="SELECT * from money;";
                            ResultSet rnote=s.executeQuery(qnote);
                            while(rnote.next())
                            {
                                dbnote1=rnote.getInt("Thousand");
                                dbnote2=rnote.getInt("FiveHundred");
                                dbnote3=rnote.getInt("Hundred");
                                dbnote4=rnote.getInt("Fifty");
                            }
                            dbnote1+=note1;
                            dbnote2+=note2;
                            dbnote3+=note3;
                            dbnote4+=note4;
                            String upnote="UPDATE money SET Thousand="+ dbnote1 +",FiveHundred="+ dbnote2 +",Hundred="+ dbnote3 +",Fifty="+ dbnote4 +";";
                            s.executeUpdate(upnote);
                            System.out.println("Notes added successfully!");
                            break;
                            
                        default:
                            System.out.println("Invalid Choice!");
                    }
            
                } 
        }
    }
    
    static void userLogin(Connection conn)throws SQLException, IOException {
        
            System.out.print("\nEnter your account number : ");
            String accno = in.next();
            System.out.print("Enter your pin : ");
            int pin = in.nextInt();
            query = "SELECT * FROM user;";
            ResultSet rs = s.executeQuery(query);
            boolean check = false;
            while(rs.next()) {
                if(rs.getString("AccountNo").equals(accno) && rs.getInt("Pin")==pin) {
                    check = true;
                    break;
                }
            }

            if(check) {
                System.out.println("\nUser login successfull");
                String choice;
                do {
                    System.out.println("1. Withdrawal");
                    System.out.println("2. Mini Statement");
                    System.out.println("3. Exit");
                    System.out.print("Enter your choice : ");
                    int ch = in.nextInt();

                    switch(ch) {
                        case 1: float bal = rs.getFloat("Amount");
                                query = "SELECT * FROM money;";
                                ResultSet rs1 = s.executeQuery(query);
                                int stn=0,sfhn=0,shn=0,sfn=0;
                                while(rs1.next()) {
                                    stn = rs1.getInt("Thousand");
                                    sfhn = rs1.getInt("FiveHundred");
                                    shn = rs1.getInt("Hundred");
                                    sfn = rs1.getInt("Fifty");
                                }
                                int tamt = stn*1000 + sfhn*500 + shn*100 + sfn*50;
                                int z1=0,z2=0,z3=0,z4=0,d1=0,d2=0,d3=0,d4=0;
                                System.out.print("Enter the amount (multiples of 50) : ");
                                int amt = in.nextInt();
                                if(bal<=0 || bal<amt)
                                    System.out.println("Insufficient balance!!!");
                                else if(amt>tamt)
                                    System.out.println("Amount greater than available in atm");
                                else if(tamt==0)
                                    System.out.println("Sorry... money not available!");
                                else if(sfn==0 && amt%100==50)
                                    System.out.println("50 rupees notes not available Enter in multiples of 100");
                                else {
                                    if(stn >= (amt/1000)) {
                                        z1 = amt/1000;
                                        d1 = amt%1000;
                                    }
                                    else if(stn > 0) {
                                        z1 = stn;
                                        d1 = amt%(stn*1000);
                                    }
                                    else {
                                        z1 = 0;
                                        d1 = amt;
                                    }

                                    if(sfhn >= (d1/1000)) {
                                        z2 = d1/500;
                                        d2 = d1%500;
                                    }
                                    else if(sfhn > 0) {
                                        z2 = sfhn;
                                        d2 = d1%(sfhn*500);
                                    }
                                    else {
                                        z2 = 0;
                                        d2 = d1;
                                    }

                                    if(shn >= (d2/100)) {
                                        z3 = d2/100;
                                        d3 = d2%100;
                                    }
                                    else if(shn > 0) {
                                        z3 = shn;
                                        d3 = d2%(shn*100);
                                    }
                                    else {
                                        z3 = 0;
                                        d3 = d2;
                                    }

                                    if(sfn >= (d3/50)) {
                                        z4 = d3/50;
                                        d4 = d3%50;
                                    }
                                    else if(sfn > 0) {
                                        z4 = sfn;
                                        d4 = d3%(sfn*50);
                                    }
                                    else {
                                        z4 = 0;
                                        d4 = d3;
                                    }


                                    System.out.println("Withdraw case");

                                    String c;
                                    System.out.println("\nDo you want a receipt <y/n> ");
                                    c = in.next();

                                    if(c.equals("Y") || c.equals("y"))
                                    {
                                       if(z1!=0)
                                           System.out.println("1000 * "+z1+" = "+z1*1000); 
                                       if(z2!=0)
                                           System.out.println("500  * "+z2+" = "+z2*500);
                                       if(z3!=0)
                                           System.out.println("100  * "+z3+" = "+z3*100);
                                       if(z4!=0)
                                           System.out.println("50   * "+z4+" = "+z4*50);

                                       System.out.println("Total Amount withdrawn = "+amt);

                                       System.out.println("Thanks for using HPES Bank!");
                                    }
                                    else
                                        System.out.println("Thanks for using HPES Bank!");

                                    stn -= z1;
                                    sfhn -= z2;
                                    shn -= z3;
                                    sfn -= z4;
                                    query = "UPDATE money SET Thousand = "+ stn +",FiveHundred = "+ sfhn +",Hundred = "+ shn +",Fifty = "+ sfn +";";
                                    s.executeUpdate(query);
                                    bal -= amt;
                                    query = "UPDATE user SET Amount = "+ bal +" where AccountNo = '"+ accno +"';";
                                    s.executeUpdate(query);
                                }
                                break;
                        case 2: float balance = rs.getFloat("Amount");
                                System.out.println("The current balance is " + balance);
                                break;
                        case 3: System.out.println("Exiting...");
                                System.exit(0);
                        default:System.out.println("Invalid choice!!!");
                    }
                    System.out.println("Make another transaction <y/n> : ");
                    choice = in.next();
            
                }while(choice.equals("Y") || choice.equals("y"));

            }
            else 
                System.out.println("\nUser not found");            
    }
    
}
