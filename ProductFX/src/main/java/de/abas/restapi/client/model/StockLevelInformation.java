package de.abas.restapi.client.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * @author aoezenir
 *
 */
public class StockLevelInformation {
	private SimpleStringProperty zn;
	private SimpleStringProperty lgruppe;
	private SimpleStringProperty lager;
	private SimpleStringProperty lplatz;
	private SimpleStringProperty dispo;
	private SimpleStringProperty lemge;
	private SimpleStringProperty leinheit;
	private SimpleStringProperty lzu;
	private SimpleStringProperty lab;

	public StockLevelInformation() {
		this.zn = new SimpleStringProperty("");
		this.lgruppe = new SimpleStringProperty("");
		this.lager = new SimpleStringProperty("");
		this.lplatz = new SimpleStringProperty("");
		this.dispo = new SimpleStringProperty("");
		this.lemge = new SimpleStringProperty("");
		this.leinheit = new SimpleStringProperty("");
		this.lzu = new SimpleStringProperty("");
		this.lab = new SimpleStringProperty("");
	}

	public String getZn() {
		return zn.get();
	}

	public void setZn(String zn) {
		this.zn.set(zn);
	}

	public String getLgruppe() {
		return lgruppe.get();
	}

	public void setLgruppe(String lgruppe) {
		this.lgruppe.set(lgruppe);
	}

	public String getLager() {
		return lager.get();
	}

	public void setLager(String lager) {
		this.lager.set(lager);
	}

	public String getLplatz() {
		return lplatz.get();
	}

	public void setLplatz(String lplatz) {
		this.lplatz.set(lplatz);
	}

	public String getDispo() {
		return dispo.get();
	}

	public void setDispo(String dispo) {
		this.dispo.set(dispo);
	}

	public String getLemge() {
		return lemge.get();
	}

	public void setLemge(String lemge) {
		this.lemge.set(lemge);
	}

	public String getLeinheit() {
		return leinheit.get();
	}

	public void setLeinheit(String leinheit) {
		this.leinheit.set(leinheit);
	}

	public String getLzu() {
		return lzu.get();
	}

	public void setLzu(String lzu) {
		this.lzu.set(lzu);
	}

	public String getLab() {
		return lab.get();
	}

	public void setLab(String lab) {
		this.lab.set(lab);
	}

}
