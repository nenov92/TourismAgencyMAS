package nenov.cs4027.assessment.main;

import java.io.Serializable;

public class Ticket implements Serializable {
	private static final long serialVersionUID = -5038569846676001834L;

	private float price;
	private String companyName;
	private String type;
	private String ticketNumber;
	private boolean isPartOfPackage;

	public Ticket() {

	}

	public Ticket(float price, String companyName, String type) {
		this.price = price;
		this.companyName = companyName;
		this.type = type;
		this.isPartOfPackage = false;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public boolean isPartOfPackage() {
		return isPartOfPackage;
	}

	public void setPartOfPackage(boolean isPartOfPackage) {
		this.isPartOfPackage = isPartOfPackage;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Ticket) {
			Ticket other = (Ticket) o;
			if (this.getTicketNumber() == other.getTicketNumber()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = (int) (PRIME + this.ticketNumber.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Company Name: ");
		sb.append(getCompanyName());
		sb.append("; Price: ");
		sb.append(getPrice());
		sb.append("; Type: ");
		sb.append(getType());
		sb.append("; Ticket Number: ");
		sb.append(getTicketNumber());
		return sb.toString();
	}
}
