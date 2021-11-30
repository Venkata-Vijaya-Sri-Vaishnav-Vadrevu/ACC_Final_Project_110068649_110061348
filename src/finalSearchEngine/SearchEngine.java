package finalSearchEngine;

import java.io.*;
import java.util.*;
import textprocessing.*;
import sorting.*;
import static java.util.stream.Collectors.toMap;
public class SearchEngine {

	//This method is responsible for removing stop words from the input keywords  
	public static String[] getKeywords(String inputStr) 
	{
		int i = 0;
		In in = new In("C:/Users/vadre/OneDrive/Documents/acc notes/trial5-1/ACC_FinalProject/src/finalSearchEngine/stop-words.txt");	
		inputStr = inputStr.toLowerCase();
		
		while (!in.isEmpty()) 
		{
			
			String text = in.readLine();
			text = text.toLowerCase();
			text = "\\b"+text+"\\b";
			inputStr = inputStr.replaceAll(text,"");	
		}
		System.out.println(inputStr);
		StringTokenizer st  = new StringTokenizer(inputStr, " ");
		String[] keyWords = new String[st.countTokens()];
		while (st.hasMoreTokens()) 
		{ 
			keyWords[i]=st.nextToken();
			i++;
        }
		return keyWords;
	}

	//This methods is responsible for indexing URLs by fetching URLs from file and inserting each URL into Hashmap
	public static HashMap<Integer,String> indexURLS() 
	{	
		int i = 0;
		HashMap<Integer,String> UrlIndex = new HashMap<Integer,String>();	
		In in = new In("C:/Users/vadre/OneDrive/Documents/acc notes/trial5-1/ACC_FinalProject/src/finalSearchEngine/output.txt");
		
        while (!in.isEmpty()) 
        {
        	String text = in.readLine();
        	UrlIndex.put(i,text);
        	i++;	    	
        }
		return UrlIndex;
	}
	
	//This method is responsible for creating TST of each text file 
	public static TST<Integer> getTST(String finalPath) 
	{	
		int j = 0;
		TST<Integer> tst = new TST<Integer>();	
		In in = new In(finalPath);
        while (!in.isEmpty()) 
        {
        	String text = in.readLine();
	        if (j == 0) { 
	        	j = 1;
	            continue;
	        } else if (j == 1) {  
	        	j = 0; 
	        	StringTokenizer st  = new StringTokenizer(text, " ");
	        	while (st.hasMoreTokens()) { 
	    			String word = st.nextToken();
	    			word = word.toLowerCase();
	        		if(tst.contains(word)) {
	        			tst.put(word, tst.get(word)+1);    			
	        		} else {
	        			tst.put(word, 1);
	        		}
	            }
	        }	
        }
		return tst;
	}
	
	//This method is responsible to find the the occurrence of the keywords in each text file and get the count
	//Map each text file to its corresponding number into an arraylist
	public static HashMap<Integer, Integer> getFreqList(String[] keyWords){
		ArrayList<String> textList = new ArrayList<>();
		HashMap<Integer,Integer > freqList = new HashMap<Integer, Integer>(); 
		File folder = new File("C:/Users/vadre/OneDrive/Documents/acc notes/trial5-1/ACC_FinalProject/src/finalSearchEngine/urls"); 
        File[] files = folder.listFiles();
        for (File file : files)
        {
            String myURL = file.getName();
            myURL = myURL.substring(0, (myURL.length()-4));
            textList.add(myURL);
        }
        for (int i = 0 ; i < textList.size() ; i++) {
        	
	        String filePath = "C:/Users/vadre/OneDrive/Documents/acc notes/trial5-1/ACC_FinalProject/src/finalSearchEngine/urls/";
	        String fileName = textList.get(i);
	        String finalPath = filePath+fileName;
	        String tempFileIndex = fileName.substring(0, (fileName.length()-4));
        	int fileIndex = Integer.parseInt(tempFileIndex);
	        TST<Integer> tst = new TST<Integer>();
	        tst = SearchEngine.getTST(finalPath);
	        int counter = 0;
	        for (String str: keyWords) {	        	
	        	if (tst.contains(str)){
	        		int count = tst.get(str);
	        		counter = counter + count;        		
	        	}
	        }
	        freqList.put(fileIndex, counter);
        }
		return freqList;
	}

	//This method is responsible to sort hashmap in descending order based on the values
	public static HashMap<Integer, Integer> sortHashMap(HashMap<Integer,Integer> freqList)
	{	
		  HashMap<Integer, Integer> sortedFreqList = freqList
		          .entrySet()
		          .stream()
		          .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		          .collect(
		              toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
		                  LinkedHashMap::new)); 
		
		return sortedFreqList;		
	}
	
	//This method is used to store the frequency list hashmap used for Page Ranking
	public static void storeHashMap(HashMap<Integer, Integer> freqList, String[] keyWords) {

		Sort.mergeSort(keyWords);
		String fileName = "";
		for (String str : keyWords) {
			fileName = fileName + str + "_";
		}
		fileName = fileName + ".dat";
		String filePath = "C:/Users/vadre/OneDrive/Documents/acc notes/trial5-1/ACC_FinalProject/src/finalSearchEngine/hashmap_data/";
		String finalPath = filePath + fileName;
		try {

			FileOutputStream fileOut = new FileOutputStream(finalPath);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(freqList);
			out.close();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// This method is used to retrieve the frequency list hashmap used for Page Ranking
	@SuppressWarnings("unchecked")
	public static HashMap<Integer,Integer> retreiveHashMap(String[] keyWords) {
		
		Sort.mergeSort(keyWords);
		String fileName = "";
		for (String str : keyWords) {
			fileName = fileName + str + "_";
		}
		fileName = fileName + ".dat";
		String filePath = "C:/Users/vadre/OneDrive/Documents/acc notes/trial5-1/ACC_FinalProject/src/finalSearchEngine/hashmap_data/";
		String finalPath = filePath + fileName;
		  HashMap<Integer,Integer> freqList = new HashMap<Integer,Integer>();
		  freqList = null;
		  try{
			  FileInputStream fileIn = new FileInputStream(finalPath);
			  ObjectInputStream in = new ObjectInputStream(fileIn);
			  freqList = (HashMap<Integer, Integer>)in.readObject();
			  in.close();
			  fileIn.close();
		  } catch (Exception e){
		  e.printStackTrace();
		  }
		  return freqList;
	}
	
	//This method is responsible for all the process, main driver function
	public static void main(String[] args) {

		String mySearch = "ebay";
		String[] keyWords = SearchEngine.getKeywords(mySearch);
		Sort.mergeSort(keyWords);
		String fileName = "";
		for (String str : keyWords) {
			fileName = fileName + str + "_";
		}
		fileName = fileName + ".dat";
		boolean fileExist = false;
		File folder = new File("C:/Users/vadre/OneDrive/Documents/acc notes/trial5-1/ACC_FinalProject/src/finalSearchEngine/hashmap_data"); 
        File[] files = folder.listFiles();
		for (File file : files) {
			String myFileName = file.getName();
			if (myFileName.compareTo(fileName) == 0) {
				fileExist = true;
				break;
			}
		}	
		if (fileExist == true){
			HashMap<Integer,String> urlIndex = new HashMap<Integer, String>();
			urlIndex = SearchEngine.indexURLS();
			HashMap<Integer,Integer> freqList = new HashMap<Integer,Integer>();
			freqList = SearchEngine.retreiveHashMap(keyWords);
			System.out.println("Top Ten Search Results for \""+mySearch +"\" are:\n");
			int j = 0;
			for (HashMap.Entry<Integer, Integer> entry : freqList.entrySet()) {
				if (j < 10) {
					int urlKey = entry.getKey();
					System.out.println(urlIndex.get(urlKey)+"\n");
					j++;
				} else {
					break;
				}
			}
		} else if (fileExist == false) {
			HashMap<Integer,String> urlIndex = new HashMap<Integer, String>();
			urlIndex = SearchEngine.indexURLS();
			HashMap<Integer,Integer> freqList = new HashMap<Integer,Integer>();
			freqList = SearchEngine.getFreqList(keyWords);
			freqList = SearchEngine.sortHashMap(freqList);
			SearchEngine.storeHashMap(freqList, keyWords);
			System.out.println("Top Ten Search Results for \""+mySearch +"\" are:\n");
			int j = 0;
			for (HashMap.Entry<Integer, Integer> entry : freqList.entrySet()) {
				if (j < 10) {
					int urlKey = entry.getKey();
					System.out.println(urlIndex.get(urlKey)+"\n");
					j++;
				} else {
					break;
				}
			}	
		}
	}

}
