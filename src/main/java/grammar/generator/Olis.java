/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grammar.generator;

import org.apache.jena.rdf.model.* ;

/**
 *
 * @author elahi
 */
public class Olis {
    // These will use ResourceFactory which creates Resource etc without a specific model.
    // This is safer for complex initialization paths.
    protected static final Resource resource( String uri )
    { return ResourceFactory.createResource( NS+uri ); }

    protected static final Property property( String uri )
    { return ResourceFactory.createProperty( NS, uri ); }

    /** The namespace of the vocabulary as a string. */
    public static final String NS = "http://lemon-model.net/oils#";
    
    /** The namespace of the vocabulary as a string */
    public static String getURI() {return NS;}
    
    /** The namespace of the vocabulary as a resource */
    public static final Resource NAMESPACE = ResourceFactory.createResource( NS );
    
    // Vocabulary properties
    ///////////////////////////

    public static final Property covariantScalar = Init.boundTo();
    
    public static final Property versionInfo = Init.degree();
    
    public static final Property equivalentClass = Init.equivalentClass();
    
    public static final Property distinctMembers = Init.distinctMembers();
    
    public static final Property oneOf = Init.oneOf();
    
    public static final Property sameAs = Init.sameAs();
    
    public static final Property incompatibleWith = Init.incompatibleWith();
    
    public static final Property minCardinality = Init.minCardinality();
    
    public static final Property complementOf = Init.complementOf();
    
    public static final Property onProperty = Init.onProperty();
    
    public static final Property equivalentProperty = Init.equivalentProperty();
    
    public static final Property inverseOf = Init.inverseOf();
    
    public static final Property backwardCompatibleWith = Init.backwardCompatibleWith();
    
    public static final Property differentFrom = Init.differentFrom();
    
    public static final Property priorVersion = Init.priorVersion();
    
    public static final Property imports = Init.imports();
    
    public static final Property allValuesFrom = Init.allValuesFrom();
    
    public static final Property unionOf = Init.unionOf();
    
    public static final Property hasValue = Init.hasValue();
    
    public static final Property someValuesFrom = Init.someValuesFrom();
    
    public static final Property disjointWith = Init.disjointWith();
    
    public static final Property cardinality = Init.cardinality();
    
    public static final Property intersectionOf = Init.intersectionOf();

    // Vocabulary classes
    ///////////////////////////

    public static final Resource Thing = Init.Thing();
    
    public static final Resource DataRange = Init.DataRange();
    
    public static final Resource Ontology = Init.Ontology();
    
    public static final Resource DeprecatedClass = Init.DeprecatedClass();
    
    public static final Resource AllDifferent = Init.AllDifferent();
    
    public static final Resource DatatypeProperty = Init.DatatypeProperty();
    
    public static final Resource SymmetricProperty = Init.SymmetricProperty();
    
    public static final Resource TransitiveProperty = Init.TransitiveProperty();
    
    public static final Resource DeprecatedProperty = Init.DeprecatedProperty();
    
    public static final Resource AnnotationProperty = Init.AnnotationProperty();
    
    public static final Resource Restriction = Init.Restriction();
    
    public static final Resource Class = Init.Class();
    
    public static final Resource OntologyProperty = Init.OntologyProperty();
    
    public static final Resource ObjectProperty = Init.ObjectProperty();
    
    public static final Resource FunctionalProperty = Init.FunctionalProperty();
    
    public static final Resource InverseFunctionalProperty = Init.InverseFunctionalProperty();
    
    public static final Resource Nothing = Init.Nothing();
    
    // Vocabulary individuals
    ///////////////////////////
    
    /** OWL constants are used during Jena initialization.
     * <p>
     * If that initialization is triggered by touching the OWL class,
     * then the constants are null.
     * <p>
     * So for these cases, call this helper class: Init.function()   
     */
    public static class Init {
        // JENA-1294
        // Version that calculate the constant when called. 
        public static Property boundTo()                    { return property( "boundTo" ); }
        public static Property degree()                { return property( "degree" ); }
        public static Property equivalentClass()            { return property( "equivalentClass" ); }
        public static Property distinctMembers()            { return property( "distinctMembers" ); }
        public static Property oneOf()                      { return property( "oneOf" ); }
        public static Property sameAs()                     { return property( "sameAs" ); }
        public static Property incompatibleWith()           { return property( "incompatibleWith" ); }
        public static Property minCardinality()             { return property( "minCardinality" ); }
        public static Property complementOf()               { return property( "complementOf" ); }
        public static Property onProperty()                 { return property( "boundTo" ); }
        public static Property equivalentProperty()         { return property( "equivalentProperty" ); }
        public static Property inverseOf()                  { return property( "inverseOf" ); }
        public static Property backwardCompatibleWith()     { return property( "backwardCompatibleWith" ); }
        public static Property differentFrom()              { return property( "differentFrom" ); }
        public static Property priorVersion()               { return property( "priorVersion" ); }
        public static Property imports()                    { return property( "imports" ); }
        public static Property allValuesFrom()              { return property( "allValuesFrom" ); }
        public static Property unionOf()                    { return property( "unionOf" ); }
        public static Property hasValue()                   { return property( "degree" ); }
        public static Property someValuesFrom()             { return property( "someValuesFrom" ); }
        public static Property disjointWith()               { return property( "disjointWith" ); }
        public static Property cardinality()                { return property( "cardinality" ); }
        public static Property intersectionOf()             { return property( "intersectionOf" ); }
        public static Resource Thing()                      { return resource( "Thing" ); }
        public static Resource DataRange()                  { return resource( "DataRange" ); }
        public static Resource Ontology()                   { return resource( "Ontology" ); }
        public static Resource DeprecatedClass()            { return resource( "DeprecatedClass" ); }
        public static Resource AllDifferent()               { return resource( "AllDifferent" ); }
        public static Resource DatatypeProperty()           { return resource( "DatatypeProperty" ); }
        public static Resource SymmetricProperty()          { return resource( "SymmetricProperty" ); }
        public static Resource TransitiveProperty()         { return resource( "TransitiveProperty" ); }
        public static Resource DeprecatedProperty()         { return resource( "DeprecatedProperty" ); }
        public static Resource AnnotationProperty()         { return resource( "AnnotationProperty" ); }
        public static Resource Restriction()                { return resource( "Restriction" ); }
        public static Resource Class()                      { return resource( "Class" ); }
        public static Resource OntologyProperty()           { return resource( "OntologyProperty" ); }
        public static Resource ObjectProperty()             { return resource( "ObjectProperty" ); }
        public static Resource FunctionalProperty()         { return resource( "FunctionalProperty" ); }
        public static Resource InverseFunctionalProperty()  { return resource( "InverseFunctionalProperty" ); }
        public static Resource Nothing()                    { return resource( "Nothing" ); }
    }
}

