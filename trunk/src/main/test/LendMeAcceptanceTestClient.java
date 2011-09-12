package main.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import main.LendMeFacade;
import easyaccept.EasyAcceptFacade;

public class LendMeAcceptanceTestClient {

        public static void main(String[] args) throws Exception {

        	List<String> l = new ArrayList<String>();
        	l.add(".."+File.separator+"acceptance-tests-scripts"+File.separator+"US11.txt");
        	
              LendMeFacade lendMeFacade = new LendMeFacade();
              EasyAcceptFacade eaFacade = new EasyAcceptFacade(lendMeFacade, l);
              eaFacade.executeTests();
              System.out.println(eaFacade.getCompleteResults());

        }

}