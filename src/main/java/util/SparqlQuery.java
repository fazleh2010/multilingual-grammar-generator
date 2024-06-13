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
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class SparqlQuery {

    private String sparqlEndpoint = "https://dbpedia.org/sparql";
    private String dbpediaUri = null;
    private String sparql = null;
    private Set<String> entityList = new LinkedHashSet<String>();
    
    
    public SparqlQuery(String endpoint, String classUri) {
        this.sparqlEndpoint = endpoint;
        String sparqlString = this.findEntitySparql(classUri);
        this.entityList = this.resultFromEndpoint(sparqlString);
        if (!this.entityList.isEmpty()) {
            String entity = this.entityList.iterator().next();
            sparqlString = this.findClassSparql(entity);
            this.entityList = new HashSet<String>();
            this.entityList = this.filterWikidata(this.resultFromEndpoint(sparqlString));
        }
    }

    public SparqlQuery(String endpoint, String dbpediaUri, Boolean filterFlag) {
        this.sparqlEndpoint = endpoint;
        this.dbpediaUri = dbpediaUri;
        this.entityList = this.filterWikidata(resultFromEndpoint(this.makeSparql(dbpediaUri)));
    }

   
  
    public Set<String> resultFromEndpoint(String queryString) {
        Set<String> list = new HashSet<String>();

        Boolean labelFlag = false, objectFlag = false, subjFlag = false;

        if (queryString.contains("objOfProp")) {
            objectFlag = true;
        }
        if (queryString.contains("subjOfProp")) {
            subjFlag = true;
        }

        //System.out.println("queryString::" + queryString);
        //System.out.println("sparqlEndpoint::" + sparqlEndpoint);
        try {
            Query query = QueryFactory.create(queryString);

            QueryExecution qexec = QueryExecutionFactory.sparqlService(sparqlEndpoint, query);
            ResultSet results = qexec.execSelect();
            // Process the results
            while (results.hasNext()) {
                QuerySolution soln = results.nextSolution();
                // Get the value of the "label" variable
                RDFNode arg = null;
                RDFNode label = null;
                String labelString = null, resourceUri = null;

                if (objectFlag) {
                    arg = soln.get("objOfProp");
                } else if (subjFlag) {
                    arg = soln.get("subjOfProp");
                }
                Resource resource = arg.asResource();
                //String labelString = literal.getString();
                //Label label = new Label(resource.getURI());
                //labelString =label.getLabel();
                //resourceUri=label.getUri();
                //System.out.println("resourceString::"+labelString+" "+resourceUri);

                if (resource.isResource()) {
                    //System.out.println("resource: " + resource.getURI());
                    resourceUri = resource.getURI();
                    //System.out.println("resourceString::" + resourceUri + " labelString::" + labelString);
                    list.add(resourceUri);
                }

                //System.out.println(labelString+" " + resourceUri);
                //exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("list::"+list);
        return list;
    }

    private String makeSparql(String dbpediaUri) {
        this.sparql = "SELECT  ?objOfProp WHERE {" + "<" + dbpediaUri + ">" + "   <http://www.w3.org/2002/07/owl#equivalentProperty>  ?objOfProp  }";
        return sparql;

    }
    
    private String findEntitySparql(String classUri) {
        this.sparql = "SELECT DISTINCT  ?subjOfProp WHERE {?subjOfProp <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> " + "<" + classUri + ">" + " . }";
        return sparql;

    }
    
    private String findClassSparql(String entityUri) {
        this.sparql = "SELECT DISTINCT  ?objOfProp WHERE {" + "<" + entityUri + ">" + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>  ?objOfProp . }";
        return sparql;

    }

    /*private String makeClassSparql(String dbpediaUri) {
        this.sparql = "SELECT DISTINCT  ?objOfProp WHERE {?subjOfProp <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?objOfProp . }";
        return sparql;

    }*/
    
    


    public String getSparqlEndpoint() {
        return sparqlEndpoint;
    }

    public Set<String> getEntityList() {
        return entityList;
    }

    public String getWikidataPropertyFirst() {
        if(this.entityList.isEmpty()){
            return null;
        }
        return entityList.iterator().next();
    }

    public String getSparql() {
        return sparql;
    }

   

    private Set<String> filterWikidata(Set<String> list) {
        for (String wikidataUri : list) {
            if (wikidataUri.contains("http://www.wikidata.org/")) {
                entityList.add(wikidataUri);
            }
        }
        //System.out.println("entityMap::" + entityList);
        return entityList;
    }
    
    /*private Set<String> filterDBpediaWikidata(Set<String> list) {
        for (String wikidataUri : list) {
            if (wikidataUri.contains("http://www.wikidata.org/")||wikidataUri.contains("http://dbpedia.org/ontology/SportsTeam")) {
                entityList.add(wikidataUri);
            }
        }
        //System.out.println("entityMap::" + entityList);
        return entityList;
    }*/
    
     public static void main(String[] args) {
        String endpoint = "https://dbpedia.org/sparql";
        String dbpediaUri = "http://dbpedia.org/ontology/abbreviation";
        String queryString = "SELECT DISTINCT  ?objOfProp WHERE {?subjOfProp <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?objOfProp}";
        
        
        SparqlQuery dbpediaEndpoint = new SparqlQuery(endpoint,"http://dbpedia.org/ontology/SportsTeam");
        System.out.println(dbpediaEndpoint.getWikidataPropertyFirst());
        /*String sparqlString=dbpediaEndpoint.findEntitySparql("http://dbpedia.org/ontology/SportsTeam");
        //System.out.println(sparqlString);
        Set<String> entityList = dbpediaEndpoint.resultFromEndpoint(sparqlString);
        System.out.println(entityList);
        String entity=null,result=null;
        if(!entityList.isEmpty()){
             entity=entityList.iterator().next();
             sparqlString=dbpediaEndpoint.findClassSparql(entity);
             entityList =new HashSet<String>();
             entityList = dbpediaEndpoint.filterWikidata(dbpediaEndpoint.resultFromEndpoint(sparqlString));
             if(!entityList.isEmpty())
                result=entityList.iterator().next();
        }
                     System.out.println(result);
  */
        


        //Set<String> entityList = dbpediaEndpoint.resultFromEndpoint(sparqlString);
        /*List<String>wikipediaList=new ArrayList<String>();
        if(dbpediaEndpoint.getEntityList().contains("http://dbpedia.org/ontology/SportsTeam")){
            for(String key:dbpediaEndpoint.getEntityList()){
                if(key.contains("http://www.wikidata.org/")){
                    wikipediaList.add(key);
                }
            }
            
        }*/
         //System.out.println(wikipediaList);
    }

}
