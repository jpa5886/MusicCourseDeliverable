package edu.psgv.sweng861;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class Parses JSON based on key words/keys
 */
public class JSONParser {

	public  void main(String[] args) 
	{
		// TODO Auto-generated method stub
	}
	
	//searches through the JSON file for keys
	public void getKey(JSONObject json, String key, String search) 
	{
		boolean exists = json.has(key);
		
		Iterator<?> keys;
		
		String nextKey;
		
		// run a loop to pass through the JSON object and find the next key to search
		if(!exists)
		{
			keys = json.keys();
			
			while(keys.hasNext()) 
			{
				nextKey = (String)keys.next();
				
				try 
				{
					if(json.get(nextKey) instanceof JSONObject) 
					{				
						if(exists == false) 
						{
							getKey(json.getJSONObject(nextKey), key, search);
						}
					} 
					else if (json.get(nextKey) instanceof JSONArray) 
					{
						JSONArray jsonarray = json.getJSONArray(nextKey);
						
						for(int i=0; i<jsonarray.length(); i++) 
						{
							String jsonarrayString = jsonarray.get(i).toString();
							
							JSONObject innerJSON = new JSONObject(jsonarrayString);

							//run function again with smaller json object until key is found
							if(exists == false) 
							{
								getKey(innerJSON, key, search);
							}
						}
					}
				}
				catch (Exception e) 
				{
					// for now do nothing, just catch exception
				}
			}
		}
		else 
		{
			parseJsonObject(json, key, search);
		}
	}

	/**
	 * Parses the json object and prints out the key
	 * @param json
	 * @param key
	 */
	public void parseJsonObject(JSONObject json, String key, String search) 
	{
		//convert to string to filter results
		String result = json.getString(key).toString();
		
		//results for 'name' are not unique with this API
		//so filtering on certain keywords generates the information
		//we want to show the user
		if(result.contains("hub") || result.contains("apple")) 
		{
			//do nothing
		} 
		//do not want to print out result same as search
		else if (!result.toLowerCase().equals(search))
		{
			//because of how the API response structure filter out any results that
			//may contain the user search in the result
			if (!result.toLowerCase().contains(search))
			{
				System.out.println(json.get(key));
			}
		}
	}
}
