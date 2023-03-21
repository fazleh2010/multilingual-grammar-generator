package org.linkeddatafragments.evaluation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Entry implements Serializable {
  public static final long serialVersioUID = 3L;
  private String id, questions, sparql,className;
  private List<String> questionList;
  private List<String> resultList;
  private Object actualEntry;

    public Entry() {
         
    }

    
  
}
