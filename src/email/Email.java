package email;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
	private String body;

	Email(String receivedIMAP) {
		parseRecievedIMAP(receivedIMAP);
	}

	private void parseRecievedIMAP(String receivedIMAP) {

		no = parseNo(receivedIMAP);

		from = parseField(receivedIMAP, "From");
		from = decodeBase64stringToUtf8(from);
		to = parseField(receivedIMAP, "To");
		to = decodeBase64stringToUtf8(to);
		cc = parseField(receivedIMAP, "Cc");
		cc = decodeBase64stringToUtf8(cc);
		subject = parseField(receivedIMAP, "Subject");
		subject = decodeBase64stringToUtf8(subject);

		String dateStr = parseField(receivedIMAP, "Date");
		date = parseDate(dateStr);

		body = parseAndDecodeBody(receivedIMAP);
		
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
		Pattern pattern = Pattern.compile("\\n" + fieldName + ":[ \\t]*(.*)");
		Matcher matcher = pattern.matcher(receivedIMAP);
		return matcher.find() ? matcher.group(1).trim() : "";
	}

	private static String parseAndDecodeBody(String receivedIMAP) {
 // Boundary 추출
		
//		Pattern mimePattern = Pattern.compile("[;\\s]{0,1}Content-Type:\\s[(multipart/mixed)(multipart/alternative)][^;]");
//	    Matcher mimeMatcher = mimePattern.matcher(mimeMessage);
//
//	    if (!mimeMatcher.find()) {
//	        System.out.println("Message is not MIME.");
//	        return mimeMessage;
//	    }
	    Pattern boundaryPattern = Pattern.compile("boundary=\"([^\"]+)\"");
	    Matcher boundaryMatcher = boundaryPattern.matcher(receivedIMAP);
	    StringBuilder resultText = new StringBuilder();

	    if (!boundaryMatcher.find()) {
	        System.out.println("Boundary not found.");
	        return receivedIMAP;
	    }

	    String boundary = boundaryMatcher.group(1);
	    String[] parts = receivedIMAP.split("--" + boundary);

//	    int attachmentCount = 0;

	    for (String part : parts) {
	    	
	        // Content-Type 추출
	        Pattern contentTypePattern = Pattern.compile("Content-Type: ([^;]+)");
	        Matcher contentTypeMatcher = contentTypePattern.matcher(part);
	        String contentType = contentTypeMatcher.find() ? contentTypeMatcher.group(1).trim() : "";

	        // Base64로 인코딩된 컨텐츠 추출
	        Pattern contentPattern = Pattern.compile("Content-Transfer-Encoding: base64\\s+([A-Za-z0-9+/=\\s]+)");
	        Matcher contentMatcher = contentPattern.matcher(part);

	        if (contentMatcher.find()) {
	            String base64Content = contentMatcher.group(1).replaceAll("\\s", ""); // 줄바꿈 제거
	            byte[] decodedBytes = Base64.getDecoder().decode(base64Content);

	            if (contentType.equals("text/plain")) {
	                // 텍스트 본문을 디코딩하여 추가
	                String plainText = new String(decodedBytes);
	                System.out.println("Decoded Text (" + contentType + "): " + plainText);
	                resultText.append(plainText).append("\n");
	            } 
//	            else {
//	                // 첨부 파일 처리
//	                Pattern filenamePattern = Pattern.compile("filename=\"([^\"]+)\"");
//	                Matcher filenameMatcher = filenamePattern.matcher(part);
//	                String fileName = filenameMatcher.find() ? filenameMatcher.group(1) : "attachment_" + (++attachmentCount);
//	                fileName = Email.decodeBase64stringToUtf8(fileName);
//	                System.out.println("파일 이름 : " + fileName);
//
//	                // 파일로 저장
//	                try (FileOutputStream fos = new FileOutputStream(fileName)) {
//	                    fos.write(decodedBytes);
//	                } catch (FileNotFoundException e) {
//	                    e.printStackTrace();
//	                } catch (IOException e) {
//	                    e.printStackTrace();
//	                }
//	                System.out.println("Attachment saved as: " + fileName);
//	            }
	        }
	    }
	    return resultText.toString();
	}


	private static Date parseDate(String dateString) {
		// 패턴에 요일 (EEE), 월 (MMM), 일 (dd), 시간 (HH:mm:ss), 시간대 (z), 연도 (yyyy) 포함
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		try {
			return formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date((long) 0);
	}
	
	public static String decodeBase64stringToUtf8(String text) {
        Pattern pattern = Pattern.compile("=\\?utf-8\\?B\\?([A-Za-z0-9+/=]+)\\?=", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        StringBuffer decodedText = new StringBuffer();

        // 패턴에 맞는 부분을 찾아 디코딩
        while (matcher.find()) {
            String base64String = matcher.group(1);
            byte[] decodedBytes = Base64.getDecoder().decode(base64String);
            String decodedSegment = new String(decodedBytes);
            matcher.appendReplacement(decodedText, decodedSegment);
        }
        
        matcher.appendTail(decodedText);
        return decodedText.toString();
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
		return "{\n" + "no = " + no + "\nfrom = " + from + "\nto = " + to + "\ncc = " + cc + "\nsubject= " + subject + "\ndate = " + date
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
