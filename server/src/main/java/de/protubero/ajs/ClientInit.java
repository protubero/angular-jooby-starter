package de.protubero.ajs;

/**
 * Contains initialization data for the client
 * 
 * @author MSchaefer
 *
 */
public class ClientInit {

	// application title
	private String title;

	public ClientInit(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
