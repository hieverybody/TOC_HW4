///////////////////////////////////////////////////////////////////////////////////////////////////
//Name : 蔡佳蓁                                                                                  //
//Student ID : F74004096                                                                         //
//                                                                                               //
//Description : A program to parse a given URL which can load a Json file                        //
//				containing less than 2000 pieces of house trading data in our country,           //
//				and find out which road in a city has maximum distinct month of house trading.   //
//                                                                                               //
//                                                                                               //
// Input Argument : url                                                                          //
//				ex : http://www.datagarage.io/api/538447a07122e8a77dfe2d86                       // 
//                                                                                               //
// Output : the road which has maximum distinct month of house trading and                       //
//          its highest and lowest sale price                                                    //
//                                                                                               //
//                                                                                               //
// Date : 2014/6/26                                                                              //
///////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.*;


public class TocHw4 {
	public static void main(String[] args) throws MalformedURLException, IOException, JSONException
	{
		// read json data
		URL url = new URL(args[0]);
    	InputStreamReader input = new InputStreamReader(url.openStream(),"UTF-8");
        JSONTokener jsonTokener = new JSONTokener(input);
        JSONArray jsonArr = new JSONArray(jsonTokener);
        
        String[] addr_name = new String[3000];
        String[] temp_addr = new String[3000];
        long[] temp_month = new long[3000];
        long[][] trade_month = new long[3000][3000];
        long[] temp_price = new long[3000];
        long[][] trade_price = new long[3000][3000];
        int[] total_count = new int[3000];
        int[] count = new int[3000];
        int temp_count = 0;
        int max_distinct_month;
        long max = 0;
        long min = 0;
        int totoal_data = 0;
        int total_road = 0;
        int m ;
        boolean same = false;
		/*
		parse data and confirm if they match the condition or not
		if yes, save its address, trade month and trade price
		*/
        for (int i = 0; i < (jsonArr).length(); i++) 
        {
            JSONObject obj = (JSONObject) jsonArr.get(i);
            String addr = (String) obj.get("土地區段位置或建物區門牌");
            long date = (long) obj.getLong("交易年月");
            long price = (long) obj.getLong("總價元");
            
            Pattern pattern1 = Pattern.compile(".*大道.*");
            Matcher matcher1 = pattern1.matcher(addr);
            Pattern pattern2 = Pattern.compile(".*路.*");
            Matcher matcher2 = pattern2.matcher(addr);
            Pattern pattern3 = Pattern.compile(".*街.*");
            Matcher matcher3 = pattern3.matcher(addr);
            Pattern pattern4 = Pattern.compile(".*巷.*");
            Matcher matcher4 = pattern4.matcher(addr);
            
            if(matcher2.matches())
            {
            	String a[] = addr.split("路"); 
            	temp_addr[totoal_data] = a[0]+"路";
            	temp_month[totoal_data] = date;
            	temp_price[totoal_data] = price;
            	totoal_data++;
            }
            if(matcher1.matches())
            {
            	String a[] = addr.split("大道"); 
            	temp_addr[totoal_data] = a[0]+"大道";
            	temp_month[totoal_data] = date;
            	temp_price[totoal_data] = price;
            	totoal_data++;
            }
            if(matcher3.matches())
            {
            	String a[] = addr.split("街"); 
            	temp_addr[totoal_data] = a[0]+"街";
            	temp_month[totoal_data] = date;
            	temp_price[totoal_data] = price;
            	totoal_data++;
            }
            else if(matcher4.matches())
            {
            	String a[] = addr.split("巷"); 
            	temp_addr[totoal_data] = a[0]+"巷";
            	temp_month[totoal_data] = date;
            	temp_price[totoal_data] = price;
            	totoal_data++;
            }
            
        }
        // save the distinct road name in a new array
        addr_name[0] = temp_addr[0];
        total_road++;
        for (int j = 1; j < totoal_data; j++) 
        {
        	for (int i = 0; i < j; i++) 
        	{
        		if(temp_addr[j].equals(temp_addr[i]))
        		{
        			same = true;
        		}
        	}
        	if(!same)
        	{
        		addr_name[total_road] = temp_addr[j];
        		total_road++;
        	}
        	same = false;
        }
		// save trade month and trade price of each distinct road
        same = false;
        for (int i = 0; i < total_road; i++) 
        {
        	m = 0;
        	for (int j = 0; j < totoal_data; j++) 
        	{
        		if(addr_name[i].equals(temp_addr[j]))
        		{
        			trade_month[i][m] = temp_month[j];
        			trade_price[i][m] = temp_price[j];
        			m++;
        		}
        	}
			// count and save the distinct months of each road
        	total_count[i] = m;
        	temp_count = 1;
        	for (int k = 1; k < m; k++) 
	    	{
	    		for (int l = 0; l < k; l++) 
	        	{
	        		if(trade_month[i][k] == trade_month[i][l])
	        		{
	        			same = true;
	        		}
	        	}
	        	if(!same)
	        	{
	    			temp_count++;
	        	}
	        	same = false;
	        }
        	count[i] = temp_count;
        }
        // to find out the road of maximum distinct months
        max_distinct_month = count[0];
        for (int i = 1; i < total_road; i++) 
        	if(count[i] > max_distinct_month)
        		max_distinct_month = count[i];
        
        // to find out its highest and lowest sale price  
        for (int i = 0; i < total_road; i++) 
        {
        	if(count[i] == max_distinct_month)
        	{
        		max = trade_price[i][0];
                min = trade_price[i][0];
        		for (int j = 0; j < total_count[i]; j++) 
        		{
        			if(trade_price[i][j] > max)
        				max = trade_price[i][j];
        			if(trade_price[i][j] < min)
        				min = trade_price[i][j];
        		}
        		System.out.println(addr_name[i]+", 最高成交價:"+max+", 最低成交價:"+min);
        	}
        }
        	
	}
}