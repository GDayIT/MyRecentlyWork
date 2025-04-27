
package de.bbq.wochenberichte;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;


public class AiFragenController {
    
    @FXML
    TextArea prompt;
    
    
    @FXML
    public void generieren (ActionEvent e) throws IOException {
        String text = prompt.getText();
        
        URL url = new URL("https://bbq-wochenberichte-ai.gentlent.workers.dev");
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "text/plain");
        con.setRequestProperty("Content-Length", text.length() + "");
        
        //Daten "schreibe"
        try {
            DataOutputStream schreiber = new DataOutputStream(con.getOutputStream());
            schreiber.writeBytes(text);
            
//            // Daten "lesen"
            InputStreamReader leser = new InputStreamReader(con.getInputStream());
//            BufferedReader buffer = new BufferedReader(leser);
            
            
            JAXBContext ctx = JAXBContext.newInstance(Wochenbericht.class);
            Unmarshaller unmar = ctx.createUnmarshaller();
            Wochenbericht bericht = (Wochenbericht) unmar.unmarshal(leser);
            
            bericht.setName(App.primary.name.getText());
            bericht.setDatum(App.primary.montag);
            bericht.setUmschulung(App.primary.umschulung.getText());
            
            
            
            /*
            String zeile;
            while ((zeile = buffer.readLine()) != null){
            System.out.println(zeile);
            }
            */
//            String xmlDaten = "";
//            while (true){
//                String zeile = buffer.readLine();
//                
//                if (zeile == null){
//                    break;
//                }
//                xmlDaten = xmlDaten + zeile;
//            }
            
//            System.out.pintln(Array.toString (bericht.getEingabeFelder()));

             App.primary.berichtLaden(bericht);
        }catch (Exception err){
        err.printStackTrace();
        }
        
        //Fenster schließen
        PrimaryController.aiFenster.close();
    }

}
