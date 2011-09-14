package main.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.LendMeAcceptanceTestInterface;
import easyaccept.EasyAcceptFacade;

public class LendMeAcceptanceTestClient {

	public static void main(String[] args) throws Exception {

        List<String> files = new ArrayList<String>();
        for ( int i=1; i<15; i++ ){
      	  files.add(String.format(".."+File.separator+
      			  "acceptance-tests-scripts"+File.separator+"US%02d.txt", i));
        }
        LendMeAcceptanceTestInterface lendMeFacade = new LendMeAcceptanceTestInterface();
        EasyAcceptFacade eaFacade = new EasyAcceptFacade(lendMeFacade, files);
        eaFacade.executeTests();
        System.out.println(eaFacade.getCompleteResults());

  }

}