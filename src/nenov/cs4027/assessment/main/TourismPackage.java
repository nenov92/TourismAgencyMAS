package nenov.cs4027.assessment.main;

public class TourismPackage {
	private Ticket eventTicket = null;
	private Ticket transportTicket = null;

	public Ticket getEventTicket() {
		return eventTicket;
	}

	public void setEventTicket(Ticket eventTicket) {
		this.eventTicket = eventTicket;
	}

	public Ticket getTransportTicket() {
		return transportTicket;
	}

	public void setTransportTicket(Ticket transportTicket) {
		this.transportTicket = transportTicket;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Event Ticket: ");
		sb.append(getEventTicket());
		sb.append("Transport Ticket: ");
		sb.append(getTransportTicket());
		return sb.toString();
	}

}
