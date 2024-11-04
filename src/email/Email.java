package email;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email implements Comparable<Email>{
	private int order;
	private String subject;
	private String from;
	private String to;
	private String cc;
	private String date;
	private String contentType;
	private String body;
	
	Email(String receivedIMAP) {
		parseRecievedIMAP(receivedIMAP);
	}
	
	private void parseRecievedIMAP(String receivedIMAP) {
//		order = 
		from = parseField(receivedIMAP, "From");
        to = parseField(receivedIMAP, "To");
        subject = parseField(receivedIMAP, "Subject");
        date = parseField(receivedIMAP, "Date");
        contentType = parseField(receivedIMAP, "Content-Type");

        body = parseBody(receivedIMAP);

        System.out.println("From: " + from);
        System.out.println("To: " + to);
        System.out.println("Cc: " + cc);
        System.out.println("Subject: " + subject);
        System.out.println("Date: " + date);
        System.out.println("Content-Type: " + contentType);
        System.out.println("Body: " + body);
	}
	
    public static String parseField(String receivedIMAP, String fieldName) {
        Pattern pattern = Pattern.compile(fieldName + ":\\s*(.*)");
        Matcher matcher = pattern.matcher(receivedIMAP);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    public static String parseBody(String receivedIMAP) {
        String[] parts = receivedIMAP.split("\r?\n\r?\n", 2); // split at the first blank line
        
        return parts.length > 1 ? parts[1].trim() : "";
    }

	public String getSubject() {
		return subject;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getDate() {
		return date;
	}

	public String getBody() {
		return body;
	}
    
    @Override
    public String toString() {
    	return "{"
    			+ "order = " + order
    			+ "\nfrom = " + from
    			+ "\nto = "  + to
    			+ "\nsubject= " + subject
    			+ "\ndate = " + date
    			+ "\nbody = " + body
    			+ "}";
    }

	@Override
	public int compareTo(Email o) {
//		this.date o.date
		return 0;
	}
    
    
	
}
