import java.util.HashMap;
import java.util.Map;

public class AI_3MenMorris {
	//keep track of what positions are connected which positions
	private static int positionAdjacent[][]={{1,6},{0,2,4},{1,9},{4,7},{1,3,5},{4,8},{0,7,13},{3,6,10},{5,9,12},{2,8,15},{7,11},{10,12,14},{8,11},{6,14},{11,13,15},{9,14}};
	
	//to map each position to a winpath
	private static Map<Integer,Map<Integer,Integer>> winPath = new HashMap<Integer,Map<Integer,Integer>>();
	
	public static void initalize(){
		//to list all paths that can be used to win
		 int winPaths[][]={{0,1,2},{3,4,5},{10,11,12},{13,14,15},{0,6,13},{2,9,15},{3,7,10},{5,8,12}};
		for (int i=0;i<winPaths.length;i++){
				Map<Integer,Integer> map = new HashMap<Integer,Integer>();
				if (winPath.containsKey(winPaths[i][0]))
					map = winPath.get(winPaths[i][0]);
					
				map.put(winPaths[i][1], winPaths[i][2]);
				map.put(winPaths[i][2], winPaths[i][1]);
				
				winPath.put(winPaths[i][0],map);
				
				map = new HashMap<Integer,Integer>();
				if (winPath.containsKey(winPaths[i][1]))
					map = winPath.get(winPaths[i][1]);
					
				map.put(winPaths[i][0], winPaths[i][2]);
				map.put(winPaths[i][2], winPaths[i][0]);
				
				winPath.put(winPaths[i][1],map);
				
				map = new HashMap<Integer,Integer>();
				if (winPath.containsKey(winPaths[i][2]))
					map = winPath.get(winPaths[i][2]);
					
				map.put(winPaths[i][1], winPaths[i][0]);
				map.put(winPaths[i][0], winPaths[i][1]);
				
				winPath.put(winPaths[i][2],map);	
		}
			
	}
	
	public static int[] getAdj(int pos){
		return positionAdjacent[pos];
	}
	
	public static Map<Integer,Integer> getPath(int pos){
		if (winPath.containsKey(pos))
			return winPath.get(pos);
		return null;
		
	}

}
