package org.linkeddatafragments.evaluation;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EntryComparison implements Serializable {
  public static final long serialVersioUID = 2L;
  private Entry qaldEntry;
  private Entry queGGEntry;
  private Boolean matchedFlag=false;
  private List<String> qaldResults;
  private List<String> queGGResults;
  private Double SimilarityCsore;
  
  
  public EntryComparison(Entry qaldEntry,Entry queGGEntry,Boolean matchedFlag,List<String> qaldResults,List<String> queGGResults,Double SimilarityCsore){
    this.qaldEntry=qaldEntry;
    this.queGGEntry=queGGEntry;
    this.matchedFlag=matchedFlag;
    this.qaldResults=qaldResults;
    this.queGGResults=queGGResults;
    this.SimilarityCsore=SimilarityCsore;
    
  }

    private float tp = 0, fp = 0, fn = 0, precision = 0, recall = 0, f_measure = 0;

    public EntryComparison() {
    }

  public void addTp(float newValue) {
    tp += newValue;
  }

  public void addFp(float newValue) {
    fp += newValue;
  }

  public void addFn(float newValue) {
    fn += newValue;
  }
  
}
