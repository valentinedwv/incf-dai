/*
 * XML Type:  AffinePlacementType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.AffinePlacementType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML AffinePlacementType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface AffinePlacementType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AffinePlacementType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("affineplacementtype4e15type");
    
    /**
     * Gets the "location" element
     */
    net.opengis.gml.x32.DirectPositionType getLocation();
    
    /**
     * Sets the "location" element
     */
    void setLocation(net.opengis.gml.x32.DirectPositionType location);
    
    /**
     * Appends and returns a new empty "location" element
     */
    net.opengis.gml.x32.DirectPositionType addNewLocation();
    
    /**
     * Gets array of all "refDirection" elements
     */
    net.opengis.gml.x32.VectorType[] getRefDirectionArray();
    
    /**
     * Gets ith "refDirection" element
     */
    net.opengis.gml.x32.VectorType getRefDirectionArray(int i);
    
    /**
     * Returns number of "refDirection" element
     */
    int sizeOfRefDirectionArray();
    
    /**
     * Sets array of all "refDirection" element
     */
    void setRefDirectionArray(net.opengis.gml.x32.VectorType[] refDirectionArray);
    
    /**
     * Sets ith "refDirection" element
     */
    void setRefDirectionArray(int i, net.opengis.gml.x32.VectorType refDirection);
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "refDirection" element
     */
    net.opengis.gml.x32.VectorType insertNewRefDirection(int i);
    
    /**
     * Appends and returns a new empty value (as xml) as the last "refDirection" element
     */
    net.opengis.gml.x32.VectorType addNewRefDirection();
    
    /**
     * Removes the ith "refDirection" element
     */
    void removeRefDirection(int i);
    
    /**
     * Gets the "inDimension" element
     */
    java.math.BigInteger getInDimension();
    
    /**
     * Gets (as xml) the "inDimension" element
     */
    org.apache.xmlbeans.XmlPositiveInteger xgetInDimension();
    
    /**
     * Sets the "inDimension" element
     */
    void setInDimension(java.math.BigInteger inDimension);
    
    /**
     * Sets (as xml) the "inDimension" element
     */
    void xsetInDimension(org.apache.xmlbeans.XmlPositiveInteger inDimension);
    
    /**
     * Gets the "outDimension" element
     */
    java.math.BigInteger getOutDimension();
    
    /**
     * Gets (as xml) the "outDimension" element
     */
    org.apache.xmlbeans.XmlPositiveInteger xgetOutDimension();
    
    /**
     * Sets the "outDimension" element
     */
    void setOutDimension(java.math.BigInteger outDimension);
    
    /**
     * Sets (as xml) the "outDimension" element
     */
    void xsetOutDimension(org.apache.xmlbeans.XmlPositiveInteger outDimension);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static net.opengis.gml.x32.AffinePlacementType newInstance() {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static net.opengis.gml.x32.AffinePlacementType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.AffinePlacementType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.AffinePlacementType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.AffinePlacementType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AffinePlacementType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AffinePlacementType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AffinePlacementType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
