/**
 * Sample Skeleton for 'Ufo.fxml' Controller Class
 */

package it.polito.tdp.ufo;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.AnnoCount;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class UfoController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<AnnoCount> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxStato"
    private ComboBox<String> boxStato; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;

    @FXML
    void handleAnalizza(ActionEvent event) {
    	String stato = boxStato.getValue();
    	if(stato==null) {
    		txtResult.appendText("Seleziona uno stato");
    		return;
    	}
    	List<String> predecessori = this.model.getPredecessori(stato);
    	List<String> successori = this.model.getSuccessori(stato);
    	List<String> raggiungibili = this.model.getRaggiungibili(stato);
    	txtResult.clear();
    	txtResult.appendText("Predecessori:\n");
    	for(String stato1: predecessori) {
    		txtResult.appendText(stato1+"\n");
    	}
    	txtResult.appendText("Successori:\n");
    	for(String stato1: successori) {
    		txtResult.appendText(stato1+"\n");
    	}
    	txtResult.appendText("Raggiungibili:\n");
    	for(String stato1: raggiungibili) {
    		txtResult.appendText(stato1+"\n");
    	}

    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	AnnoCount anno = boxAnno.getValue();
    	if(anno==null) {
    		txtResult.appendText("Seleziona un anno");
    	}
    	model.creaGrafo(anno.getYear());
    	txtResult.appendText("Grafo creato!");
    	boxStato.getItems().addAll(model.getStati());
    }

    @FXML
    void handleSequenza(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		boxAnno.getItems().addAll(model.getAnni());
		
	}
}
