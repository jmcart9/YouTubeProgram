package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import main.java.quickstart.AuthGmail;
import main.java.quickstart.GmailMethods;

public class TestGmailMethods {
	
	static GmailMethods gmailMethods;
	static Gmail service;
	static final String query = "from:noreply@youtube.com";;
	static final String userID = "thisistheforestprimeval@gmail.com";
	static final String messageID = "169c3b18a7d9dadb";
	
	Message message;
	
	//@BeforeAll
	public static void setUp() throws IOException {
		service = new Gmail.Builder(AuthGmail.HTTP_TRANSPORT, AuthGmail.JSON_FACTORY, AuthGmail.authorize())
	            .setApplicationName("YouTube Playlist Creator")
	            .build();
		gmailMethods = new GmailMethods(service);
	}
	
	//@Test
	public void testGmailService() {
		assertNotEquals(service, null);
		assertEquals(service.getApplicationName(), "YouTube Playlist Creator");
		assertNotNull(service.users());
		System.out.println("gmail service is okay, I think: " + service.toString());
	}
	
	//@Test
	public void testSetEmailMessageList(){
		
		List<Message> list = gmailMethods.getEmailMessageList();
		
		assertTrue(list.isEmpty());
		System.out.println("unfilled email message list is null");
		
		System.out.println("filling now...");
		gmailMethods.setEmailMessageList(service, userID, query);
		list = gmailMethods.getEmailMessageList();
	
		assertNotNull(list);
		System.out.println("now the email message list is not null");
		
		assertFalse(list.isEmpty());
		System.out.println("EmailMessageList empty? " + list.isEmpty());
	}
	
	//@Test
	public void testSetEmailMessageListSIMPLE() {
		gmailMethods.setEmailMessageList(service, userID, query);
		assertNotNull(gmailMethods.getEmailMessageList());
		assertFalse(gmailMethods.getEmailMessageList().isEmpty());
	}
	
	//@Test
	public void testGetEmailMessageList() {
		List<Message> list = gmailMethods.getEmailMessageList();
		
		assertFalse(list.isEmpty());
		System.out.println("EmailMessageList: " + list.toString());
	}
	
	//@Test
	public void testGetMessage() {
		
		message = gmailMethods.getMessage(messageID);
		assertNotNull(message);
		
		//Files.writeString(Paths.get("output.txt"), m.toPrettyString());
		
	}
	
	//@Test
	public void testMessageBodyToString() {
		message = gmailMethods.getMessage(messageID);
		String out = gmailMethods.messageBodyToString(message);
		
		assertNotNull(out);
		assertNotEquals(out,"");
		System.out.println(out);
	}
	
	//@Test
	public void testGetVideoUrl() {
		String s = gmailMethods.getVideoUrl(gmailMethods.messageBodyToString(gmailMethods.getMessage(messageID)));
		assertNotNull(s);
		assertNotEquals(s,"");
		System.out.println(s);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	//@Test
	public void testCreateVideoList() {
		gmailMethods.setEmailMessageList(service, userID, query);
		List list = gmailMethods.getEmailMessageList();
		list.replaceAll(x -> gmailMethods.getVideoUrl(gmailMethods.messageBodyToString((Message)x)));

		assertFalse(list.isEmpty());
		assertNotNull(list);
		list.forEach(System.out::println);
	}
	
	//@Test
	public void testgetVideoUrls(){
		
	}
	
	//@Test
	public void testGetVideoIDFromUrl() {
		String url1 = "https://www.youtube.com/watch?v=oBIQbja-BPY";
		String url2 = "http://youtu.be/dQw4w9WgXcQ";
		String url3 = "http://www.youtube.com/watch?feature=player_embedded&v=xxx";
		String url4 = "https://www.youtube.com/watch?v=dm66kyU5vWA&feature=em-uploademail";
		String url5 = "https://www.youtube.com/watch?v=EOBqpDcfVW0";
		
		assertEquals("oBIQbja-BPY", gmailMethods.getVideoIDFromUrl(url1));
		assertEquals("dQw4w9WgXcQ", gmailMethods.getVideoIDFromUrl(url2));
		assertEquals("xxx", gmailMethods.getVideoIDFromUrl(url3));
		assertEquals("dm66kyU5vWA", gmailMethods.getVideoIDFromUrl(url4));
		assertEquals("EOBqpDcfVW0", gmailMethods.getVideoIDFromUrl(url5));
	}
	
	public static void main(String... args) {
		//setUp();
		//testCreateVideoList();
	}
}
