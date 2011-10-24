package com.lendme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import easyaccept.EasyAcceptFacade;

public class LendMeAcceptanceTestClient {

	public static void main(String[] args) throws Exception {

        List<String> files = new ArrayList<String>();
        for ( int i=1; i<21; i++ ){
        	if ( i != 17 && i != 18 ){
        		files.add(String.format(".."+File.separator+
        				"acceptance-test-scripts" + File.separator + "US%02d.txt", i));
        	}
        	else if ( i == 18 ) continue;
        	else{
        		files.add(String.format(".."+File.separator+
        				"acceptance-test-scripts" + File.separator + "US17_18.txt", i));        		
        	}
        }
        
        LendMeAcceptanceTestInterface lendMeInterface = new LendMeAcceptanceTestInterface();
        EasyAcceptFacade eaFacade = new EasyAcceptFacade(lendMeInterface, files);
        eaFacade.executeTests();
        System.out.println(eaFacade.getCompleteResults());

  }

}