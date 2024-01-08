//Student-Management-System in java with exception handling


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
// import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
// import javax.swing.JOptionPane;

public class SMS 
{
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) 
    {
        Scanner sin = new Scanner(System.in);
        boolean flag1 = true;
        while (flag1) 
        {
            try 
            {

                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/SMS", "root", "");
                System.out.println("\t\t\t\u001B[31m Welcome to your STUDENT_MANAGEMENT_SYSTEM \u001B[0m ");
                System.out.println("\n1) Login\n2) Sign_up \n3) Exit");
                System.out.print("Enter Choice: ");
                int welcomeChoice = sin.nextInt();
                String loginAns = null;
                switch (welcomeChoice) 
                {
                    case 1:
                        loginAns = login();
                        boolean f2 = true;
                        if (loginAns != null) 
                        {
                            String sql = "select username from student_data where username = ?";

                            PreparedStatement pst = con.prepareStatement(sql);
                            pst.setString(1, loginAns);
                            ResultSet rs = pst.executeQuery();
                            if (!rs.next()) 
                            {
                                System.out.println("            \u001B[31m      Firstly Enter your marks\u001B[00m");
                                studentEntryDB(loginAns);
                                System.out.println("Student entry Successfully Done..!!");
                            }
                            while (f2) 
                            {

                                System.out.println("            \u001B[31m      Login Success\u001B[00m");
                                System.out.println();
                                // System.out.println();
                                System.out.println("              #1. Student Details");
                                System.out.println("              #2. Enter Marks for Marksheet");
                                System.out.println("              #3. Delete Details");
                                System.out.println("              #4. Update Details");
                                System.out.println("              #5. Rank wise List");
                                System.out.println("              #6. Back to Menu");
                                System.out.println("              =>(Enter choice in 1 to 6)");
                                System.out.println();
                                System.out.print("            \u001B[47m\u001B[37m Enter choice\u001B[00m: ");
                                int choice = sin.nextInt();

                                switch (choice) 
                                {
                                    case 1:
                                        showDetails(loginAns);
                                        // f2=false;
                                        break;
                                    case 2:
                                        studentEntryDB(loginAns);
                                        // f2=false;
                                        break;
                                    case 3:
                                        deleteDetails(loginAns);
                                        break;
                                    case 4:
                                        System.out.println("Your entered Details is: ");
                                        showDetails(loginAns);
                                        System.out.println("Enter updated details: ");
                                        updateEntry(loginAns);
                                        break;
                                    case 5:
                                        sortList();
                                        break;
                                    case 6:
                                        main(null);
                                        break;

                                    default:
                                        break;
                                }
                            }
                        } 
                        else 
                        {
                            System.out.println("Wrong creditials or record not found");

                        }
                        break;
                    case 2:
                        Sign_up();
                        break;
                    case 3:
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Please select between 1 to 3 in integer form");
                        break;
                }
            } 
            catch (InputMismatchException e) 
            {
                System.out.println("\u001B[31m      Choose option in int form btwn given choices \u001B[00m");
                flag1 = true;
                main(null);
            } 
            catch (Exception e2) 
            {
                System.out.println("Exception occured: " + e2.getMessage());
                main(null);
            }
        }

    }

    static Connection con;

    public static void getLogin(String loginAns) 
    {
        Scanner sin = new Scanner(System.in);

    }

    public static void showDetails(String id) 
    {
        try 
        {
            String details = "select data_file from student_data where username = ?";
            PreparedStatement cst = con.prepareStatement(details);
            cst.setString(1, id);
            ResultSet rs = cst.executeQuery();
            // String studentData="";
            if (rs.next()) 
            {
                Clob c = rs.getClob("data_file");
                Reader r = c.getCharacterStream();
                BufferedReader br = new BufferedReader(r);
                String line;
                String showData = "";
                while ((line = br.readLine()) != null) 
                {
                    showData += line + "\n";
                }
                System.out.println(showData);
                // JOptionPane.showMessageDialog(null, showData, "Username: "+id,
                // JOptionPane.PLAIN_MESSAGE);
            } 
            else 
            {
                System.out.println("User Data found..!!(First you have to provide data)");

            }

        } 
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
        catch (Exception e) 
        {
            System.out.println(e.getMessage());
            // TODO: handle exception
        }
    }

    public static void sortList() 
    {
        try 
        {
            String details = "select * from student_data order by total desc";
            PreparedStatement cst = con.prepareStatement(details);
            ResultSet rs = cst.executeQuery();
            System.out.println("            \u001B[31m      Sort List\u001B[00m");
            int i = 0;
            while (rs.next()) 
            {

                String name = "select name from sign_in_data where username = ?";
                PreparedStatement pst = con.prepareStatement(name);
                pst.setString(1, rs.getString("username"));
                ResultSet rs2 = pst.executeQuery();
                while (rs2.next()) 
                {
                    i++;
                    System.out.println("Rank " + i + ") Name: " + rs2.getString("name") + "\t" + "Maths: "
                            + rs.getString("maths") + "\t" + "Java: " + rs.getString("java") + "\t" + "DS: "
                            + rs.getString("ds") + "\t" + "DBMS: " + rs.getString("dbms") + "Total: "
                            + rs.getString("total") + "\t" + "Percentage: " + rs.getString("percentage"));
                    System.out.println();
                }
            }

        } 
        catch (SQLException e) 
        {
           System.out.println("Exception: "+e.getMessage());
        }
    }

    public static void deleteDetails(String id) 
    {
        try 
        {
            String details = "delete from student_data where username = ?";
            PreparedStatement cst = con.prepareStatement(details);
            cst.setString(1, id);
            int r = cst.executeUpdate();
            // String studentData="";

            if (r > 0) 
            {

                System.out.println("User has been deleted with id: " + id);
            } 
            else 
            {
                System.out.println("User not found with this id: " + id);
            }
        } 
        catch (Exception e) 
        {
            // TODO: handle exception
        }
    }

    public static String login() 
    {
        String loginDone = null;
        String id, pwd;
        // String pwd="";
        System.out.print("Enter ID:");
        id = sc.next();
        System.out.print("Enter Password:");
        pwd = sc.next();
        // connect to the database
        try 
        {

            String loginCheck = "select * from login_data";
            PreparedStatement pst = con.prepareStatement(loginCheck);
            ResultSet rs = pst.executeQuery();
            String id1 = "", pwd1 = "";
            // String inserstd="insert into "
            while (rs.next()) 
            {
                id1 = rs.getString(1);
                pwd1 = rs.getString(2);

                if (id.equals(id1) && pwd.equals(pwd1)) 
                {
                    loginDone = id;
                    break;
                }
            }
        } 
        catch (InputMismatchException e) 
        {
            System.out.println("Exception occured: " + e.getMessage());
        }
        catch (Exception e) 
        {
            System.out.println("Exception occured: " + e.getMessage());
        }

        return loginDone;
    }

    public static void studentEntryDB(String id) 
    {
        try 
        {
            String sqlEntry = "insert into student_data values(?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement pst1 = con.prepareStatement(sqlEntry);
            pst1.setString(1, id);
            System.out.println("                 ~ Enter Subject Marks ~");
            System.out.print(">> Maths: ");
            int maths = sc.nextInt();
            pst1.setInt(2, maths);
            System.out.print(">> Java: ");
            int java = sc.nextInt();
            pst1.setInt(3, java);
            System.out.print(">> DS: ");
            int ds = sc.nextInt();
            pst1.setInt(4, ds);
            System.out.print(">> DBMS: ");
            int dbms = sc.nextInt();
            pst1.setInt(5, dbms);
            int total = maths + java + ds + dbms;
            pst1.setInt(6, total);
            double pr = (total * 100) / 400;
            pst1.setDouble(7, pr);

            if (pr > 90) 
            {

                pst1.setString(8, "A+");
            } 
            else if (pr > 80)
                pst1.setString(8, "A");
            else if (pr > 70)
                pst1.setString(8, "B+");
            else if (pr > 60)
                pst1.setString(8, "B");
            else if (pr > 50)
                pst1.setString(8, "C");
            else if (pr > 40)
                pst1.setString(8, "D");
            else
                pst1.setString(8, "Fail");
            System.out.print("Enter proper location for choose your profile photo");
            String imgpath = sc.next();
            FileInputStream fis = new FileInputStream(imgpath);
            pst1.setBlob(9, fis);
            String retrive = "select * from sign_in_data where username = '" + id + "'";
            PreparedStatement pst2 = con.prepareStatement(retrive);
            ResultSet rs = pst2.executeQuery();
            rs.next();
            String txtPath = "D:/" + id + ".txt";

            File f = new File(txtPath);
            // f.mkdirs();
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write("        ABC INSTITUTE OF ENGINEERING & TECH.\n");
            bw.write("              *STUDENT MARKSHEET *\n");
            bw.newLine();
            bw.write(
                    "----------------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.write("DETAILS: \n");
            bw.write("> Name: " + rs.getString("name") + "\n");
            bw.write("> DOB: " + rs.getString("dob") + "\t" + "Gender: " + rs.getString("gender") + "\n");
            bw.write("> Contact no: " + rs.getString("phone_no") + "\n");
            bw.write("> Mail ID: " + rs.getString("mail_id") + "\n");
            bw.write("> Username: " + id + "\t" + "Password: " + rs.getString("pswd").substring(0, 3) + "*");
            bw.newLine();
            bw.write(
                    "----------------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.write("MARKS Details: \n");
            bw.write("> Maths : " + maths + "\n");
            bw.write("> JAVA : " + java + "\n");
            bw.write("> DS : " + ds + "\n");
            bw.write("> DBMS : " + dbms + "\n");
            bw.write("+---------------------------------------+\n");
            bw.write("> TOTAL : " + total + " out of 400\n");
            bw.write("Percentage : " + pr + " %\t\n");
            bw.write("+---------------------------------------+\n");
            bw.flush();
            FileReader fr = new FileReader(f);
            pst1.setClob(10, fr);
            int r = pst1.executeUpdate();
            System.out.println("1");
            if (r > 0)
                System.out.println("Student Data has been added to database..!!\n\n" + rs.getString("name")
                        + "'s Marksheet is ready..!! in D://" + id + ".txt");
            else 
            {
                System.out.println("Error in set Data Try again..!!");
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Error : " + e.getMessage());
            // TODO: handle exception
        }
        catch (Exception e) 
        {
            System.out.println("Error : " + e.getMessage());
            // TODO: handle exception
        }
    }

    public static void updateEntry(String id) 
    {
        try 
        {
            String sqlEntry = "update student_data set maths = ? , java = ?, ds =?, dbms =?, total =?,percentage =?, grade =?, data_file = ? ";
            PreparedStatement pst1 = con.prepareStatement(sqlEntry);

            System.out.println("                 ~ Enter Subject Marks ~");
            System.out.print(">> Maths: ");
            int maths = sc.nextInt();
            pst1.setInt(1, maths);
            System.out.print(">> Java: ");
            int java = sc.nextInt();
            pst1.setInt(2, java);
            System.out.print(">> DS: ");
            int ds = sc.nextInt();
            pst1.setInt(3, ds);
            System.out.print(">> DBMS: ");
            int dbms = sc.nextInt();
            pst1.setInt(4, dbms);
            int total = maths + java + ds + dbms;
            pst1.setInt(5, total);
            double pr = (total * 100) / 400;
            pst1.setDouble(6, pr);

            if (pr > 90) 
            {

                pst1.setString(7, "A+");
            } 
            else if (pr > 80)
                pst1.setString(7, "A");
            else if (pr > 70)
                pst1.setString(7, "B+");
            else if (pr > 60)
                pst1.setString(7, "B");
            else if (pr > 50)
                pst1.setString(7, "C");
            else if (pr > 40)
                pst1.setString(7, "D");
            else
                pst1.setString(7, "Fail");

            String retrive = "select * from sign_in_data where username = '" + id + "'";
            PreparedStatement pst2 = con.prepareStatement(retrive);
            ResultSet rs = pst2.executeQuery();
            rs.next();
            String txtPath = "D:/" + id + ".txt";

            File f = new File(txtPath);
            // f.mkdirs();
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write("        ABC INSTITUTE OF ENGINEERING & TECH.\n");
            bw.write("              *STUDENT MARKSHEET *\n");
            bw.newLine();
            bw.write(
                    "----------------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.write("DETAILS: \n");
            bw.write("> Name: " + rs.getString("name") + "\n");
            bw.write("> DOB: " + rs.getString("dob") + "\t" + "Gender: " + rs.getString("gender") + "\n");
            bw.write("> Contact no: " + rs.getString("phone_no") + "\n");
            bw.write("> Mail ID: " + rs.getString("mail_id") + "\n");
            bw.write("> Username: " + id + "\t" + "Password: " + rs.getString("pswd").substring(0, 3) + "*");
            bw.newLine();
            bw.write(
                    "----------------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.write("MARKS Details: \n");
            bw.write("> Maths : " + maths + "\n");
            bw.write("> JAVA : " + java + "\n");
            bw.write("> DS : " + ds + "\n");
            bw.write("> DBMS : " + dbms + "\n");
            bw.write("+---------------------------------------+\n");
            bw.write("> TOTAL : " + total + " out of 400\n");
            bw.write("Percentage : " + pr + " %\t\n");
            bw.write("+---------------------------------------+\n");
            bw.flush();
            FileReader fr = new FileReader(f);
            pst1.setClob(8, fr);
            int r = pst1.executeUpdate();

            if (r > 0)
                System.out.println("Student Data has been added to database..!!\n\n" + rs.getString("name")
                        + "'s Marksheet is ready..!! in D://" + id + ".txt");
            else 
            {
                System.out.println("Error in set Data Try again..!!");
            }
        } 
        
        catch (SQLException e) 
        {
            System.out.println("Error : " + e.getMessage());
            // TODO: handle exception
        }
        catch (Exception e) 
        {
            System.out.println("Error : " + e.getMessage());
            // TODO: handle exception
        }
    }

    static void Sign_up() 
    {

        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Gender(M/F): ");
        String gender = sc.nextLine();
        boolean bmail = true;
        String mail = "";
        while (bmail) 
        {
            System.out.print("Enter mail id: ");
            mail = sc.nextLine();
            System.out.println("\t\t\t\u001B[32mChecking Mail id... \u001B[00m");
            try 
            {
                Thread.sleep(4000);
            } 
            catch (Exception e) 
            {
                System.out.println("Exception: " + e.getMessage());
            }
            if (mail.contains("@gmail.com")) 
            {
                bmail = false;
            } 
            else if (mail.contains("@yahoo.com")) 
            {
                bmail = false;

            } 
            else if (mail.contains("@") && mail.contains(".com")) 
            {
                bmail = false;

            } 
            else 
            {
                System.out.println("\t\t\t\u001B[31m Invalid Case:Enter valid mail id\u001b[00m");
                bmail = true;
            }
        }
        String dob="";
        
       
            System.out.print("Enter DOB(dd-mm-yyyy): ");
            dob = sc.nextLine();
    

        boolean mo = true;
        String phone = "";
        while (mo) 
        {
            System.out.print("Enter Phone no: ");
            phone = sc.next();
            System.out.println("\t\t\t\u001B[32mChecking Mail id... \u001B[00m");
            try 
            {
                Thread.sleep(4000);
            } 
            catch (Exception e) 
            {
                System.out.println("Exception: " + e.getMessage());
            }

            if (phone.length() != 10) 
            {
                System.out.println("Mobile number length must be 10\nDon't start with +91");
                mo = true;
            } 
            else 
            {

                mo = false;
            }

        }
        System.out.print("Enter Password: ");
        String userPwd = sc.next();
        System.out.println("\u001B[31m\"NOTE : do remember for next login\"\u001B[0m");
        int captcha = (int) (Math.random() * ((9999 - 1000) + 1) + 1000);//9999 is max limit and 1000 is min limit
        // 1000 is lower limit and 9999 is upper limit for generating captcha value
        // between them
        System.out.println(
                "===========================================================================================================================");
        System.out.println("\t\t\t\t\t \u001B[32m" + "\u001b[4m\u001B[1m||" + captcha + "||\u001B[0m");
        System.out.println(
                "===========================================================================================================================");
        while (true) 
        {
            System.out.print("\t\t\t\tEnter Captcha: ");
            String captchaCheck = sc.next();
            if (Integer.valueOf(captchaCheck) == captcha) 
            {
                String username = name.substring(0, 4).toLowerCase() + "_" + phone.substring(0, 4);
                try 
                {
                    PreparedStatement pstuser = con.prepareStatement("Select username from sign_in_data");
                    boolean flag = true;
                    int auto_increment = 1;
                    ResultSet rs = pstuser.executeQuery();
                    while (rs.next()) 
                    {
                        if (rs.getString("username").equals(username)) 
                        {
                            flag = false;
                            username = username + "_" + auto_increment;
                            auto_increment++;
                            break;
                        }
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Excption Occured: " + e.getMessage());
                }
                System.out.println();
                System.out.println(
                        "                   ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
                System.out.println(
                        "                                      USERNAME: " + username + "                          ");
                System.out.println(
                        "                                     Password: " + userPwd + "                          ");
                System.out.println(
                        "                   ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ");
                try 
                {

                    String sql_signin = "insert into sign_in_data values(?,?,?,?,?,?,?)";
                    String sql_login = "insert into login_data values(?,?)";
                    // String sql_student = "insert into student_data values(?,?,?,?,?,?,)";
                    PreparedStatement pst = con.prepareStatement(sql_signin);
                    PreparedStatement pst2 = con.prepareStatement(sql_login);
                    // PreparedStatement pst3 = con.prepareStatement(sql_student);
                    pst.setString(1, username);
                    pst.setString(2, userPwd);
                    pst.setString(3, name);
                    pst.setString(4, gender);
                    pst.setString(5, mail);
                    pst.setString(6, dob);
                    pst.setString(7, phone);

                    pst2.setString(1, username);
                    pst2.setString(2, userPwd);
                    // pst3.setString(1, username);
                    int r = pst.executeUpdate();
                    int r2 = pst2.executeUpdate();
                    // int r3 = pst3.executeUpdate();
                    if (r > 0 && r2 > 0) {
                        System.out.println("");
                        System.out.println(">> Sign_Up Successfully Done..!!");
                        System.out.println("");
                    }
                } 
                catch (InputMismatchException e) 
                {
                    System.out.println("Exception occured in signin process: " + e.getMessage());
                    // TODO: handle exception
                }
                catch (SQLException e) 
                {
                    System.out.println("Exception occured in signin process: " + e.getMessage());
                    // TODO: handle exception
                }
                catch (Exception e) 
                {
                    System.out.println("Exception occured in signin process: " + e.getMessage());
                    // TODO: handle exception
                }
                break;
            } 
            else 
            {
                System.out.println("Enter correct captcha value which is shown in green text..!!");
            }
        }

    }
}
