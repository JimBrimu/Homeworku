
package javafxhomworku;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 *
 * @author jimbrimu
 */
public class FXMLController implements Initializable {
    private String fileNameXML;
    
    Map<String, Double> currencies = new HashMap<String, Double>();
    
    @FXML
    private TextField filenameXML;
    @FXML
    private ComboBox<String> comboBox1;
    @FXML
    private Button btnAtvaltas;
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private TextField mennyiFt;
    @FXML
    private TextField osszegFt;
    @FXML
    private RadioButton rButton1;
    @FXML
    private ToggleGroup group1;
    @FXML
    private TextField urlAddress;
    @FXML
    private RadioButton rButton2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBox1.setPromptText("Válassz!");
        urlAddress.setText("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
    }    
 
    @FXML
    private void actionXMLloader(ActionEvent event) {
        currencies.clear();
        comboBox1.getItems().clear();
        if (rButton1.isSelected()) {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Válassza ki a valuta árfolyamokat tartalmazó XML-t!");
            Stage stage = (Stage) anchorpane.getScene().getWindow();
            File file = filechooser.showOpenDialog(stage);
            fileNameXML = file.getPath();
            filenameXML.setText(fileNameXML);
        }    
 
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
           
            Document doc = rButton1.isSelected() ? db.parse(new File(fileNameXML)) : db.parse(new URL(urlAddress.getText()).openStream());

            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Cube");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);
                if (elem.getAttribute("currency") != null && !elem.getAttribute("currency").isEmpty()) {
                    currencies.put(elem.getAttribute("currency"), Double.valueOf(elem.getAttribute("rate")));

                }
            }
            comboBox1.getItems().addAll(currencies.keySet());
            comboBox1.getSelectionModel().select(0);
//            comboBox1 = FXCollections.observableMap(currencies.values());
            
//        } catch (ParserConfigurationException | SAXException | IOException e) {
//            e.printStackTrace();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            java.util.logging.Logger.getLogger(FXMLController.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }

        if (!currencies.isEmpty()) {
            comboBox1.setVisibleRowCount(8);
            comboBox1.setDisable(false);
            btnAtvaltas.setDisable(false);
            mennyiFt.setDisable(false);
            osszegFt.setDisable(false);
        }
            else {
            System.out.println("Hiba történt a betöltéskor! Próbálja újra.");
            comboBox1.setDisable(true);
            btnAtvaltas.setDisable(true);
            mennyiFt.setDisable(true);
            osszegFt.setDisable(true);
        }

        
        
    }

    @FXML
    private void actionCombobox1(ActionEvent event) {
    }

    @FXML
    private void actionAtvaltas(ActionEvent event) {
    
        Double ennyiFt;
        
        ennyiFt = (Double.valueOf(mennyiFt.getText()) / currencies.get(comboBox1.getSelectionModel().getSelectedItem()).doubleValue() ) * currencies.get("HUF");
        osszegFt.setText(ennyiFt.toString());

    }
  
    
}
