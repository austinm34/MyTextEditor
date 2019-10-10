//import editor;

import javax.swing.text.Document;

public class main {
    public static void main(String[] args) {

        editor e = new editor();
        Document doc = e.p.getDocument();
        doc.addDocumentListener(e);
    }
}
