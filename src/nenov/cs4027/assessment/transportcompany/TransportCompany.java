package nenov.cs4027.assessment.transportcompany;

import de.dailab.jiactng.agentcore.AbstractAgentBean;
import nenov.cs4027.assessment.main.Constants;
import nenov.cs4027.assessment.main.Offer;

public class TransportCompany extends AbstractAgentBean {

	private String companyName;
	private int capacity;
	private float price;

	public String getCompanyName() {
		return companyName;
	}

	public Offer getOffer(Offer offer) {
		offer.setCapacity(capacity);
		offer.setCompanyName(companyName);
		offer.setPrice(price);
		offer.setType(Constants.TRANSPORT_OFFER);
		offer.loadTickets();
		return offer;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
