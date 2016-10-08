package nenov.cs4027.assessment.main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.dailab.jiactng.agentcore.knowledge.IFact;

public class Offer implements IFact {

	private static final long serialVersionUID = -1535100183029006992L;

	private int capacity;
	private String companyName;
	private float price;
	private List<Ticket> availableTickets = new ArrayList<Ticket>();
	private List<Ticket> boughtTickets = new ArrayList<Ticket>();
	private String type;

	public Offer() {

	}

	public Offer(int capacity, String companyName, float price, String type) {
		this.setCapacity(capacity);
		this.setCompanyName(companyName);
		this.setPrice(price);
		this.type = type;
	}

	public void loadTickets() {
		String ticketType;
		if (getType().equals(Constants.TICKET_OFFER)) {
			ticketType = Constants.EVENT_TICKET;
		} else {
			ticketType = Constants.TRANSPORT_TICKET;
		}

		for (int i = 0; i < getCapacity(); i++) {
			Ticket ticket = new Ticket((getPrice() / getCapacity()), getCompanyName(), ticketType);
			
			// set unique ticket number, which is used in Ticket class equals() and hashCode() methods
			ticket.setTicketNumber(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5));
			addTicket(ticket);
		}
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public List<Ticket> getAvailableTickets() {
		return availableTickets;
	}

	public void setAvailableTickets(List<Ticket> availableTickets) {
		this.availableTickets = availableTickets;
	}

	public void addTicket(Ticket ticket) {
		this.availableTickets.add(ticket);
	}

	public void removeTickets(int numberOfTickets) {
		for (int i = getAvailableTickets().size() - 1; i >= 0 && numberOfTickets > 0; i--) {
			boughtTickets.add(getAvailableTickets().remove(i));
			numberOfTickets--;
		}
	}

	public List<Ticket> getBoughtTickets() {
		return boughtTickets;
	}

	public void setBoughtTickets(List<Ticket> boughtTickets) {
		this.boughtTickets = boughtTickets;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Company Name: ");
		sb.append(getCompanyName());
		sb.append("; Capacity: ");
		sb.append(getCapacity());
		sb.append("; Price: ");
		sb.append(getPrice());
		sb.append("; Available Tickets: ");
		sb.append(getAvailableTickets().size());
		sb.append("; Bought Tickets: ");
		sb.append(getBoughtTickets().size());
		return sb.toString();
	}

}
