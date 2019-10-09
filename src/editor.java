
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class editor extends JFrame implements ActionListener{
    JFrame f;
    //JTabbedPane tabs;
    JTextPane p;
    File currentFile;

    editor(){
        f = new JFrame("Code Editor");
        p = new JTextPane();
        //tabs = new JTabbedPane(JTabbedPane.TOP);

        JMenuBar mb = new JMenuBar();  //why declare this inside while other 2 were declared outsied?

        JMenu m1 = new JMenu("File");
        //create menu items
        JMenuItem mi1 = new JMenuItem("New Project");
        JMenuItem mi2 = new JMenuItem("Open Project");
        JMenuItem mi3 = new JMenuItem("Save Project");
        JMenuItem mi9 = new JMenuItem("Print");

        //Add action listener
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi9.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi9);

        mb.add(m1);
        f.setJMenuBar(mb);
        f.add(p);
        f.setSize(500, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //======================attempt code coloring==============


        //=====================end code coloring block=============
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();

        if(s.equals("New Project")){                    //If user wants to make new project
            // Create an object of JFileChooser class
            JFileChooser j = new JFileChooser(":f");    //create j file chooser
            j.setDialogTitle("Select a project directory");                 //set the text at the top
            j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  //open a dialog to choose a directory
            j.setAcceptAllFileFilterUsed(false);                    //idk what this does
            int r = j.showSaveDialog(null);                 //show the file chooser and save dialogue.

            if(r == JFileChooser.APPROVE_OPTION){      //if option is approved, i.e. user chose save button
                String path = j.getSelectedFile().getAbsolutePath();       //get the path of the file
                String main = path + "\\Main.txt";      //make a path name for main
                String readme = path + "\\README.txt";  //add a path for readme
                String lib = path + "\\lib";            //add a path for lib directory
                //System.out.println(path);
                File mainf = new File(main);            //create main file variable
                File readmef = new File(readme);        //create readme file variable
                File libf = new File(lib);              //create lib directory variable
                try {
                    boolean createdMain = mainf.createNewFile();
                    boolean createdReadme = readmef.createNewFile();    //create the files and directory
                    boolean createdLib = libf.mkdir();
                }
                catch(IOException ioe){
                    System.out.println("Error while creating project files :" + ioe);
                }
            }


        }
        else if(s.equals("Open Project")){          //if user wants to open an existing project
            JFileChooser j = new JFileChooser(":f");
            j.setDialogTitle("Select directory of project to open");
            j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //j.setAcceptAllFileFilterUsed(false);
            int r = j.showSaveDialog(null);                 //these lines open the save dialog

            if(r == JFileChooser.APPROVE_OPTION){
                String projDirPath = j.getSelectedFile().getAbsolutePath();  //get path of existing project this
                // this should be a directory, else call open file.
                File projDir = new File(projDirPath);
                File[] filesInDir = projDir.listFiles();        //get all the filenames in the project
                String mainPath = j.getSelectedFile().getAbsolutePath() + "\\Main.txt";     //look for main
                //System.out.println(mainPath);
                for( File file : filesInDir ){      //start searching for main
                    //System.out.println(file.getAbsolutePath());
                    if( file.getAbsolutePath().equals(mainPath)){
                        //System.out.println("inside");
                        try{
                            String s1 = "", s2 = "";

                            //FileReader
                            FileReader fr = new FileReader(file);

                            BufferedReader br = new BufferedReader(fr);

                            s2 = br.readLine();

                            while((s1 = br.readLine()) != null){
                                s2 = s2 + "\n" + s1;
                            }

                            currentFile = new File(file.getAbsolutePath()); // sets the current open file to Main.
                            // this will be used in "save project" function
                            //System.out.println(file.getAbsolutePath());
                        }
                        catch(Exception evt){
                            JOptionPane.showMessageDialog(f, evt.getMessage());
                        }
                    }
                }
            }
        }
        else if(s.equals("Save Project")){
            try{
                //JOptionPane.showMessageDialog(f, currentFile.getAbsolutePath());
                FileWriter wr = new FileWriter(currentFile, false);
                BufferedWriter w = new BufferedWriter(wr);

                w.write(p.getText());

                w.flush();
                w.close();
            }
            catch(Exception evt){
                JOptionPane.showMessageDialog(f, evt.getMessage());
            }
        }
        else if(s.equals("Print")){

        }
    }

    private int findLastNonWordChar (String text, int index){
        while(--index >= 0){
            if (String.valueOf(text.charAt(index)).matches("\\W")){
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar (String text, int index){
        while(index < text.length()){
            if(String.valueOf(text.charAt(index)).matches("\\W")){
                break;
            }
            index++;
        }
        return index;
    }

}

