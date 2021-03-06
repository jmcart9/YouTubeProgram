package main.java.quickstart;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;

/**
 * Shared class used by every sample. Contains methods for authorizing a user and caching credentials.
 */
public class AuthGmail {

	private static final String CREDENTIALS_FILE_PATH = "client_secret.json";
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
	private static final String TOKENS_DIRECTORY_PATH = "tokens";
	
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	
	
    /**
     * Authorizes the installed application to access user's protected data.
     * @throws GeneralSecurityException 
     *
     */
    public static Credential authorize() throws IOException{

    	try {
    		//final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        	
        	// Load client secrets.
            InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
            
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    	}
    	catch(IOException e) {
    		System.out.println("gmail credentials or json factory not found");
    		return null;
    	}
    }
}
