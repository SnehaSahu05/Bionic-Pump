package selfDefinedClasses;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class GeneralMethods {
	
	public static String GetCurrentSysTime(){
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");//dd/MM/yyyy
		Date now = new Date(System.currentTimeMillis());
		String strDate = sdfDate.format(now);
		return(strDate);
		}
	
	public static double CalculateInsulin(Integer carbs, double oldBSL){
		double newBSL;
		if (carbs < 0){
			newBSL = oldBSL - 1.5;	//....to complete
		}else{
			newBSL = oldBSL + 1.5;	//....to complete
		}
		return(newBSL);
	}

}
