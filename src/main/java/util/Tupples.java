/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author elahi
 */
public class Tupples {

    private String senseId = null;
    private String reference = null;
    private String domain = null;
    private String range = null;
    private String oils_boundTo = null;
    private String oils_degree = null;
   
    public Tupples(String lemonEntry, Integer index,  String reference, String domain, String range) {
        this.senseId = lemonEntry + "_sense_" + index.toString();
        this.reference = reference.replace(" ", "");
        this.domain = MatcherExample.replaceSpecial(domain);
        this.range = MatcherExample.replaceSpecial(range);

    }
    
    public Tupples(String lemonEntry, Integer index, String reference, String domain, String range, String oils_boundTo, String oils_degree) {
        this.senseId = lemonEntry + "_sense_" + index.toString();
        this.reference = reference.replace(" ", "");
        this.domain = MatcherExample.replaceSpecial(domain);
        this.range = MatcherExample.replaceSpecial(range);
        this.oils_boundTo = MatcherExample.replaceSpecial(oils_boundTo);
        this.oils_degree = MatcherExample.replaceSpecial(oils_degree);
    }

    public String getSenseId() {
        return senseId;
    }

    public String getReference() {
        return reference;
    }

    public String getDomain() {
        return domain;
    }

    public String getRange() {
        return range;
    }

    public String getOils_boundTo() {
        return oils_boundTo;
    }

    public String getOils_degree() {
        return oils_degree;
    }

   

}
