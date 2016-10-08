package nenov.cs4027.assessment.ticketingcompany;

import nenov.cs4027.assessment.main.Constants;
import nenov.cs4027.assessment.main.Offer;
import de.dailab.jiactng.agentcore.AbstractAgentBean;

public class TicketingCompany extends AbstractAgentBean {

	private String companyName;
	private int capacity;
	private float price;

	public Offer getOffer(Offer offer){
		offer.setCapacity(capacity);
		offer.setCompanyName(companyName);
		offer.setPrice(price);
		offer.setType(Constants.TICKET_OFFER);
		offer.loadTickets();
		return offer;
	}

	public String getCompanyName() {
		return companyName;
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
