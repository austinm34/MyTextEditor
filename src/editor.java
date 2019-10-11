import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class editor extends JFrame implements ActionListener, DocumentListener {
    JFrame f;
    //JTabbedPane tabs;
    JTextPane p;
    File currentFile = null;
    File projectDir = null;
    //=================stuff to highlight keywords
    DefaultStyledDocument document = new DefaultStyledDocument();
    Pattern keywordPatt = keywordPattern();
    Pattern arithmeticPatt = arithmeticPattern();
    StyleContext styleContext = StyleContext.getDefaultStyleContext();
    AttributeSet keyWords = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
    AttributeSet arithmetic = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.RED);
    AttributeSet normal = styleContext.addAttribute(styleContext.getEmptySet(), StyleConstants.Foreground, Color.BLACK);

    editor(){
        f = new JFrame("Code Editor");
        p = new JTextPane(document);
        //Font font = new Font(Font.SANS_SERIF, 3, 20);
        //p.setFont(font);
        //tabs = new JTabbedPane(JTabbedPane.TOP);

        JMenuBar mb = new JMenuBar();  //why declare this inside while other 2 were declared outsied?

        JMenu m1 = new JMenu("File");
        //create menu items
        JMenuItem mi1 = new JMenuItem("New Project");
        JMenuItem mi2 = new JMenuItem("Open Project");
        JMenuItem mi3 = new JMenuItem("Save Project");
        JMenuItem mi4 = new JMenuItem("Close Project");
        JMenuItem mi5 = new JMenuItem("New File");
        JMenuItem mi6 = new JMenuItem("Open File");
        JMenuItem mi7 = new JMenuItem("Save File");
        JMenuItem mi8 = new JMenuItem("Close File");
        JMenuItem mi9 = new JMenuItem("Print");

        //Add action listener
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        mi4.addActionListener(this);
        mi5.addActionListener(this);
        mi6.addActionListener(this);
        mi7.addActionListener(this);
        mi8.addActionListener(this);
        mi9.addActionListener(this);

        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        m1.add(mi4);
        m1.add(mi5);
        m1.add(mi6);
        m1.add(mi7);
        m1.add(mi8);
        m1.add(mi9);

        mb.add(m1);
        f.setJMenuBar(mb);
        f.add(p);
        f.setSize(500, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


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

                projectDir = new File(path);            //set the project directory to the one selected.
                currentFile = mainf;
                f.setTitle(projectDir.getPath());
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
                projectDir = new File(projDirPath);     //set this as the current project
                f.setTitle(projectDir.getPath());
                // this should be a directory, else call open file.
                File projDir = new File(projDirPath);
                File[] filesInDir = projDir.listFiles();        //get all the filenames in the project
                String mainPath = j.getSelectedFile().getAbsolutePath() + "\\Main.txt";     //look for main
                //System.out.println(mainPath);
                for( File file : filesInDir ){      //start searching for main
                    System.out.println(file.getAbsolutePath());
                    if( file.getAbsolutePath().equals(mainPath)){
                        System.out.println("inside");
                        try{
                            //FileReader
                            FileReader fr = new FileReader(file);
                            BufferedReader br = new BufferedReader(fr);
                            String line = null;
                            StringBuilder sb = new StringBuilder();
                            while((line = br.readLine()) != null){
                                sb.append(line + '\n');

                            }
                            p.setText(sb.toString());
                            br.close();

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
        else if(s.equals("Save Project")){      //save all files in the project directory
            if(projectDir != null){
                File[] projectFiles = projectDir.listFiles();
                for( File file : projectFiles) {        // for all the files in the project
                    if(file.isFile()) {             // if it is actually a file, i.e, not a directory,
                        try {                     //save its contents.
                            //JOptionPane.showMessageDialog(f, currentFile.getAbsolutePath());
                            FileWriter wr = new FileWriter(currentFile, false);
                            BufferedWriter w = new BufferedWriter(wr);

                            w.write(p.getText());

                            w.flush();
                            w.close();
                        } catch (Exception evt) {
                            JOptionPane.showMessageDialog(f, evt.getMessage());
                        }
                    }
                }
            }
            else{
                JOptionPane.showMessageDialog(f, "You need to open or create a project to save one!\n if this is just a file, use save file.");
            }
        }

        else if(s.equals("Close Project")){
            if(projectDir != null) {
                int option = JOptionPane.showConfirmDialog(f, "Are you sure you want to close project?\n " +
                        "any unsaved work will be lost.", "Closing project", JOptionPane.YES_NO_OPTION);
                //.out.println(option);
                if(option == 0){    //if they say yes they want to close
                    p.setText("");
                    f.setTitle("Code Editor");
                    projectDir = null;
                    currentFile = null;
                }
            }
            else{
                JOptionPane.showMessageDialog(f, "No project is currently opened.");
            }
        }

        else if(s.equals("New File")){  //makes a new file, adds it to project if one is open. else asks where to make it.
            //need to have something to save currently open files before making new one.
            String newFileName;
            String newPathName;
            File newFile;
            if(projectDir != null){     // if we are currently in a project
                newFileName = JOptionPane.showInputDialog(f,"Enter a name for the new file:");
                if(newFileName != null){    //if they didn't cancel
                    newPathName = projectDir.getAbsolutePath() + "\\" + newFileName;    // need to handle bad characters in input.
                                                                                        //also need to handle duplicate file names.
                    newFile = new File(newPathName);   //make new file with appropriate name
                    try{
                        boolean createdNewFile = newFile.createNewFile();   //create file
                        currentFile = newFile;  //set the current file to the one we just made
                        p.setText("");      //reset text pane
                    }catch(IOException ioe){
                        System.out.println("Error creating new file: " + ioe);
                    }
                }

            }
            else{   // if we aren't in a project
                JFileChooser j = new JFileChooser(":f");
                j.setDialogTitle("Where do you want to make your file?");                 //set the text at the top
                j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  //open a dialog to choose a directory

                int r = j.showSaveDialog(null);                 //show the file chooser and save dialogue.
                if(r == JFileChooser.APPROVE_OPTION){   //if the pick a place
                    String newFileDir = j.getSelectedFile().getAbsolutePath();
                    newFileName = JOptionPane.showInputDialog(f,"Enter a name for the new file");
                    if(newFileName != null){
                        newPathName =  newFileDir + "\\" + newFileName;
                        newFile = new File(newPathName);   //make new file with appropriate name
                        try{
                            boolean createdNewFile = newFile.createNewFile();   //create file
                            currentFile = newFile;  //set current file to the one we just created
                            p.setText("");      //delete all text in the pane.
                        }catch(IOException ioe){
                            System.out.println("Error creating new file: " + ioe);
                        }
                    }

                }
            }
        }

        else if(s.equals("Open File")){

        }

        else if(s.equals("Print")){

        }
    }

    @Override
    public void insertUpdate(DocumentEvent d) {
        //System.out.println(d.getOffset());
        //System.out.println("inserted text");

        handleTextChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent d) {
        //System.out.println("removed text");
        handleTextChanged();
    }

    @Override
    public void changedUpdate(DocumentEvent d) {
        //handleTextChanged();
        System.out.println("changedUpdate");
    }

    private Pattern keywordPattern() {
        StringBuilder sb = new StringBuilder();
        String[] keyWords = new String[]{"if", "else", "for", "while"};
        for (String word : keyWords) {
            sb.append("\\b");   //start of word boundary
            sb.append(word);
            sb.append("\\b|");  //end of word boundary
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);   //Remove the trailing "|";
        }

        Pattern p = Pattern.compile(sb.toString());

        return p;
    }

    private Pattern arithmeticPattern() {
        StringBuilder sb = new StringBuilder();
        String[] keyWords = new String[]{"\\+", "\\-", "\\*", "\\/", "\\|\\|", "&&", "<", ">", "<=", ">=", "==", "!="};
        for (String word : keyWords) {
            sb.append("\\B");   //start of word boundary
            sb.append(word);
            sb.append("\\B|");  //end of word boundary
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);   //Remove the trailing "|";
        }

        Pattern p = Pattern.compile(sb.toString());

        return p;
    }

    private void updateKeywordStyles(StyledDocument doc, AttributeSet normal, AttributeSet keyword, AttributeSet arithmetic) throws BadLocationException {
        System.out.println("updating keyword styles");
        doc.setCharacterAttributes(0, p.getText().length(), normal, true);

        Matcher keywordMatcher = keywordPatt.matcher(p.getDocument().getText(0,p.getDocument().getLength()));
        Matcher arithmeticMatcher = arithmeticPatt.matcher(p.getDocument().getText(0,p.getDocument().getLength()));
        while(keywordMatcher.find()){
            //changed the color of keywords
            doc.setCharacterAttributes(keywordMatcher.start(), keywordMatcher.end() - keywordMatcher.start() , keyword, false);
        }
        while(arithmeticMatcher.find()){
            //changed the color of keywords
            doc.setCharacterAttributes(arithmeticMatcher.start(), arithmeticMatcher.end() - arithmeticMatcher.start() , arithmetic, false);
        }
        System.out.println("done updating keyword styles");
    }

    private void handleTextChanged(){
        System.out.println("handling text change");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    updateKeywordStyles(document, normal, keyWords, arithmetic);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("done handling text change");
    }
}



