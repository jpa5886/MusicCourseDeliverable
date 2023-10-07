package edu.psgv.sweng861;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Class that
 */
public class Music 
{
	//keys needed to construct http request for spotify api
	private static String key = "e02310119bmsh53735ac74d9ac06p10e94bjsnee3bb35837ed";
	private static String host = "spotify23.p.rapidapi.com";
	
	/**
	 * Main method to run software program
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		//restart variable is used to ensure user inputs correct information
		String restart;
		restart = "y";
		
		String end = "";
		String userAnswer = "";
		
		//scanner to get user input
		Scanner input = new Scanner(System.in);
		
		//string to populate the http request "type" based on user selection
		String type = "";
		System.out.println("~~~~~~~Spotify Music API Search Program~~~~~~~");
		do 
		{
			System.out.println("---------------------------------------------");
			System.out.println("Do you want to search for 'Song' or 'Artist'?");
			
			//Get the user input
			//while loop to rerun the program until there is a valid selection
			while(restart.equals("y")) 
			{
				
				//get the user answer
				userAnswer = input.nextLine().toLowerCase();
				
				if(userAnswer.contains("artist")) 
				{
					System.out.println("Enter an Artist: ");
					restart="n";
					type = "tracks";
				} 
				else if(userAnswer.contains("song")) 
				{
					System.out.println("Enter a Song: ");
					restart="n";
					type = "artists";
				} 
				else 
				{
					System.out.println("You entered an invalid selection. "
							+ "Please enter 'Song' or 'Artist'.");
					restart = "y";
				}
			}

			String search = input.nextLine().toLowerCase();
			String oldSearch = search;
			//encode search for URL
			search = URLEncoder.encode(search, "UTF-8");
			
			//Query for 
			//this code snippet was provided by RapidAPI Spotify
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
					"https://spotify23.p.rapidapi.com/search/?q=" + search + "&type=" + type+
					"&offset=0&limit=10&numberOfTopResults=10"))
					.header("x-RapidAPI-Key",key)
					.header("x-RapidAPI-Host", host)
					.method("GET", HttpRequest.BodyPublishers.noBody())
					.build();
			
			HttpResponse<String> response =
					HttpClient.newHttpClient().send(
							request, HttpResponse.BodyHandlers.ofString());
			
			//Parse the Data Spotify API responded back with
			try 
			{
				String inputJson = response.body();
				
				JSONObject inputJSONObject = new JSONObject(inputJson);
				
				//class that will handle the json object parser
				JSONParser parser = new JSONParser();
				
				//Check for no information returned
				if (inputJson.equals("{}")) 
				{
					System.out.println("No information returned for " + search + ".");
				} 
				else
				{
					if(userAnswer.contains("song")) 
					{ 
						//will show the potential artists for the user input song
						System.out.println("\nPotential Artist(s): ");
						
						//searches the jsonObject for the name key to return artist names
						//to avoid printing out user input, passing in the oldSearch (not encoded
						//to the parser to check result against
						parser.getKey(inputJSONObject, "name", oldSearch);
					} 
					else if(userAnswer.contains("artist")) 
					{
						//will show the top songs for the user input artist
						System.out.println("\nTop Songs: ");
						
						//searches the jsonObject for the name key to return song names
						parser.getKey(inputJSONObject, "name", oldSearch);
					}
				}
			}
			catch (Exception e) 
			{
				System.out.println("Invalid Search! Please start again.");
				restart = "y";
				continue;
			}

			//do loop to capture valid selection
			System.out.println("\n--------------------------------------------");
			System.out.println("Would you like to search again? (y/n):");
			do 
			{
				end = input.nextLine().toLowerCase();
				
				if(end.contains("n")) 
				{
					System.out.println("Existing program. Bye!");
					
					//to exit the nested do-while loops
					end = "n";
					restart = "n";
					System.exit(0);
				} 
				else if(end.equals("y")) 
				{
					restart = "y";
					end = "n";
				}
				else 
				{
					System.out.println("Invalid answer. Please enter Y/y or N/n!");
					end = "y";
				}
			} while(end.contains("y"));
		} while(restart.contains("y"));
		
		//close scanner
		input.close();
	}
}

