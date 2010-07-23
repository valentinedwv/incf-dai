/*
 * XML Type:  AbstractCurveSegmentType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.AbstractCurveSegmentType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML AbstractCurveSegmentType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface AbstractCurveSegmentType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(AbstractCurveSegmentType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("abstractcurvesegmenttype20e3type");
    
    /**
     * Gets the "numDerivativesAtStart" attribute
     */
    java.math.BigInteger getNumDerivativesAtStart();
    
    /**
     * Gets (as xml) the "numDerivativesAtStart" attribute
     */
    org.apache.xmlbeans.XmlInteger xgetNumDerivativesAtStart();
    
    /**
     * True if has "numDerivativesAtStart" attribute
     */
    boolean isSetNumDerivativesAtStart();
    
    /**
     * Sets the "numDerivativesAtStart" attribute
     */
    void setNumDerivativesAtStart(java.math.BigInteger numDerivativesAtStart);
    
    /**
     * Sets (as xml) the "numDerivativesAtStart" attribute
     */
    void xsetNumDerivativesAtStart(org.apache.xmlbeans.XmlInteger numDerivativesAtStart);
    
    /**
     * Unsets the "numDerivativesAtStart" attribute
     */
    void unsetNumDerivativesAtStart();
    
    /**
     * Gets the "numDerivativesAtEnd" attribute
     */
    java.math.BigInteger getNumDerivativesAtEnd();
    
    /**
     * Gets (as xml) the "numDerivativesAtEnd" attribute
     */
    org.apache.xmlbeans.XmlInteger xgetNumDerivativesAtEnd();
    
    /**
     * True if has "numDerivativesAtEnd" attribute
     */
    boolean isSetNumDerivativesAtEnd();
    
    /**
     * Sets the "numDerivativesAtEnd" attribute
     */
    void setNumDerivativesAtEnd(java.math.BigInteger numDerivativesAtEnd);
    
    /**
     * Sets (as xml) the "numDerivativesAtEnd" attribute
     */
    void xsetNumDerivativesAtEnd(org.apache.xmlbeans.XmlInteger numDerivativesAtEnd);
    
    /**
     * Unsets the "numDerivativesAtEnd" attribute
     */
    void unsetNumDerivativesAtEnd();
    
    /**
     * Gets the "numDerivativeInterior" attribute
     */
    java.math.BigInteger getNumDerivativeInterior();
    
    /**
     * Gets (as xml) the "numDerivativeInterior" attribute
     */
    org.apache.xmlbeans.XmlInteger xgetNumDerivativeInterior();
    
    /**
     * True if has "numDerivativeInterior" attribute
     */
    boolean isSetNumDerivativeInterior();
    
    /**
     * Sets the "numDerivativeInterior" attribute
     */
    void setNumDerivativeInterior(java.math.BigInteger numDerivativeInterior);
    
    /**
     * Sets (as xml) the "numDerivativeInterior" attribute
     */
    void xsetNumDerivativeInterior(org.apache.xmlbeans.XmlInteger numDerivativeInterior);
    
    /**
     * Unsets the "numDerivativeInterior" attribute
     */
    void unsetNumDerivativeInterior();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractCurveSegmentType newInstance() {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        /** @deprecated No need to be able to create instances of abstract types */
        public static net.opengis.gml.x32.AbstractCurveSegmentType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.AbstractCurveSegmentType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.AbstractCurveSegmentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
