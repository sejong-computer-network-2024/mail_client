package email;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email implements Comparable<Email> {
	private int no;
	private String subject;
	private String from;
	private String to;
	private String cc;
	private Date date;
	private String contentType;
	private String body;

	Email(String receivedIMAP) {
		parseRecievedIMAP(receivedIMAP);
	}

	private void parseRecievedIMAP(String receivedIMAP) {
		System.out.println("원문 : " + receivedIMAP);

		no = parseNo(receivedIMAP);

		from = parseField(receivedIMAP, "From");
		to = parseField(receivedIMAP, "To");
		cc = parseField(receivedIMAP, "Cc");
		subject = parseField(receivedIMAP, "Subject");

		String dateStr = parseField(receivedIMAP, "Date");
		date = parseDate(dateStr);
		contentType = parseField(receivedIMAP, "Content-Type");

		body = parseBody(receivedIMAP);
	}

	private static int parseNo(String receivedIMAP) {
		Pattern pattern = Pattern.compile("^\\* (\\d+) FETCH");
		Matcher matcher = pattern.matcher(receivedIMAP);
		int no = 0;
		System.out.println(receivedIMAP);
		// 숫자 추출
		if (matcher.find()) {
			no = Integer.parseInt(matcher.group(1)); // 캡처된 숫자

		} else {
			System.out.println("FETCH 앞의 숫자를 찾을 수 없습니다.");
		}
		return no;
	}

	private static String parseField(String receivedIMAP, String fieldName) {
		Pattern pattern = Pattern.compile(fieldName + ":\\s*(.*)");
		Matcher matcher = pattern.matcher(receivedIMAP);
		return matcher.find() ? matcher.group(1).trim() : "";
	}

	private static String parseBody(String receivedIMAP) {
		Pattern pattern = Pattern.compile("(?s)\\r\\n\\r\\n(.*)(?=\\)\\n?$)");
		Matcher matcher = pattern.matcher(receivedIMAP);

		if (matcher.find()) {
			return matcher.group(1).trim();
		}
		return "";
	}

	private static Date parseDate(String dateString) {
		// 패턴에 요일 (EEE), 월 (MMM), 일 (dd), 시간 (HH:mm:ss), 시간대 (z), 연도 (yyyy) 포함
		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
		System.out.println("===========날짜 문자열===========\n" + dateString);
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date((long) 0);
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

	public Date getDate() {
		return date;
	}

	public String getBody() {
		return body;
	}

	public int getNo() {
		return no;
	}

	@Override
	public String toString() {
		return "{\n" + "no = " + no + "\nfrom = " + from + "\nto = " + to + "\nsubject= " + subject + "\ndate = " + date
				+ "\nbody = " + body + "\n}";
	}

	@Override
	public int compareTo(Email o) {
		return -(date.compareTo(o.date));
	}

	public String getCc() {
		// TODO Auto-generated method stub
		return cc;
	}

}
