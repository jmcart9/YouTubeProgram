package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistListResponse;

import main.java.quickstart.AuthYouTube;
import main.java.quickstart.YouTubeMethods;

public class TestYouTubeMethods {
	
	static YouTubeMethods youtubeMethods;
	static YouTube service;
	
	//@BeforeAll
	public static void setUp() throws GeneralSecurityException, IOException {
    	final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

		try {
			
			service = new YouTube.Builder(AuthYouTube.HTTP_TRANSPORT, AuthYouTube.JSON_FACTORY, AuthYouTube.authorize(httpTransport))
	                .setApplicationName("YouTube Playlist Creator")
	                .build();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		youtubeMethods = new YouTubeMethods(service);
	}
	
	//@Test
	public void testYoutubeService() {
		assertNotEquals(service, null);
		assertEquals(service.getApplicationName(), "YouTube Playlist Creator");
		System.out.println("service is okay, I think: " + service.toString());
	}
	
	//@Test
	public void testGetExtantPlaylists() {
		assertNotNull(youtubeMethods.getExtantPlaylists());
		youtubeMethods.getExtantPlaylists().stream().forEach(System.out::println);
		System.out.println(youtubeMethods.getExtantPlaylists().size());
	}
	
	//@Test
	public void testCreatePlaylist() {
		String playlistTitle = "the test playlist3";
		String privacy = "private";
		
		Playlist x = youtubeMethods.createPlaylist(playlistTitle);
		
		if(x == null) {
			assertNull(x);
		}
		else {
			assertEquals(x.getSnippet().getTitle(), playlistTitle);
			assertEquals(x.getStatus().getPrivacyStatus(), privacy);
			assertEquals(x.getSnippet().getDescription(), "playlist for " + playlistTitle);
			System.out.println("playlist ID: " + x.getId());
		}
	}
	
	//@Test
	public void testInsertPlaylistItem() {
		String videoId = "Ks-_Mh1QhMc";
		String playlistTitle = youtubeMethods.getVideoChannel(videoId);
		String playlistId = "PLwMubaXkpmaS7M8CryPgt8XkFa2_basIN";
		
		PlaylistItem x = youtubeMethods.insertPlaylistItem(playlistId, videoId, playlistTitle);
		
		assertEquals(x.getSnippet().getTitle(), playlistTitle);
		assertEquals(x.getSnippet().getResourceId().getVideoId(), videoId);
	}
	
	//@Test
	public void testGetVideoChannel() {
		String channel = youtubeMethods.getVideoChannel("Ks-_Mh1QhMc");
		assertNotNull(channel);
		assertEquals("TED", channel);
	}
	
	//@Test
	public void testPopulateExtantPlaylists() throws IOException {
		
		List parts = new ArrayList<String>();
		parts.add("snippet");
		
		YouTube.Playlists.List request = service.playlists().list("snippet");
    	PlaylistListResponse response = request.setMine(true).execute();
    	
    	List l = new ArrayList();
    	for(Playlist p : response.getItems()) {
    		l.add(p.getSnippet().getTitle());
    	}
    	l.stream().forEach(System.out::println);
	}
}
