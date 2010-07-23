/*
 * XML Type:  ArcByCenterPointType
 * Namespace: http://www.opengis.net/gml/3.2
 * Java type: net.opengis.gml.x32.ArcByCenterPointType
 *
 * Automatically generated - do not modify.
 */
package net.opengis.gml.x32;


/**
 * An XML ArcByCenterPointType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface ArcByCenterPointType extends net.opengis.gml.x32.AbstractCurveSegmentType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ArcByCenterPointType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s2D5DEDEB67075BF3EC00EA776296F2EE").resolveHandle("arcbycenterpointtypec137type");
    
    /**
     * Gets the "pos" element
     */
    net.opengis.gml.x32.DirectPositionType getPos();
    
    /**
     * True if has "pos" element
     */
    boolean isSetPos();
    
    /**
     * Sets the "pos" element
     */
    void setPos(net.opengis.gml.x32.DirectPositionType pos);
    
    /**
     * Appends and returns a new empty "pos" element
     */
    net.opengis.gml.x32.DirectPositionType addNewPos();
    
    /**
     * Unsets the "pos" element
     */
    void unsetPos();
    
    /**
     * Gets the "pointProperty" element
     */
    net.opengis.gml.x32.PointPropertyType getPointProperty();
    
    /**
     * True if has "pointProperty" element
     */
    boolean isSetPointProperty();
    
    /**
     * Sets the "pointProperty" element
     */
    void setPointProperty(net.opengis.gml.x32.PointPropertyType pointProperty);
    
    /**
     * Appends and returns a new empty "pointProperty" element
     */
    net.opengis.gml.x32.PointPropertyType addNewPointProperty();
    
    /**
     * Unsets the "pointProperty" element
     */
    void unsetPointProperty();
    
    /**
     * Gets the "pointRep" element
     */
    net.opengis.gml.x32.PointPropertyType getPointRep();
    
    /**
     * True if has "pointRep" element
     */
    boolean isSetPointRep();
    
    /**
     * Sets the "pointRep" element
     */
    void setPointRep(net.opengis.gml.x32.PointPropertyType pointRep);
    
    /**
     * Appends and returns a new empty "pointRep" element
     */
    net.opengis.gml.x32.PointPropertyType addNewPointRep();
    
    /**
     * Unsets the "pointRep" element
     */
    void unsetPointRep();
    
    /**
     * Gets the "posList" element
     */
    net.opengis.gml.x32.DirectPositionListType getPosList();
    
    /**
     * True if has "posList" element
     */
    boolean isSetPosList();
    
    /**
     * Sets the "posList" element
     */
    void setPosList(net.opengis.gml.x32.DirectPositionListType posList);
    
    /**
     * Appends and returns a new empty "posList" element
     */
    net.opengis.gml.x32.DirectPositionListType addNewPosList();
    
    /**
     * Unsets the "posList" element
     */
    void unsetPosList();
    
    /**
     * Gets the "coordinates" element
     */
    net.opengis.gml.x32.CoordinatesType getCoordinates();
    
    /**
     * True if has "coordinates" element
     */
    boolean isSetCoordinates();
    
    /**
     * Sets the "coordinates" element
     */
    void setCoordinates(net.opengis.gml.x32.CoordinatesType coordinates);
    
    /**
     * Appends and returns a new empty "coordinates" element
     */
    net.opengis.gml.x32.CoordinatesType addNewCoordinates();
    
    /**
     * Unsets the "coordinates" element
     */
    void unsetCoordinates();
    
    /**
     * Gets the "radius" element
     */
    net.opengis.gml.x32.LengthType getRadius();
    
    /**
     * Sets the "radius" element
     */
    void setRadius(net.opengis.gml.x32.LengthType radius);
    
    /**
     * Appends and returns a new empty "radius" element
     */
    net.opengis.gml.x32.LengthType addNewRadius();
    
    /**
     * Gets the "startAngle" element
     */
    net.opengis.gml.x32.AngleType getStartAngle();
    
    /**
     * True if has "startAngle" element
     */
    boolean isSetStartAngle();
    
    /**
     * Sets the "startAngle" element
     */
    void setStartAngle(net.opengis.gml.x32.AngleType startAngle);
    
    /**
     * Appends and returns a new empty "startAngle" element
     */
    net.opengis.gml.x32.AngleType addNewStartAngle();
    
    /**
     * Unsets the "startAngle" element
     */
    void unsetStartAngle();
    
    /**
     * Gets the "endAngle" element
     */
    net.opengis.gml.x32.AngleType getEndAngle();
    
    /**
     * True if has "endAngle" element
     */
    boolean isSetEndAngle();
    
    /**
     * Sets the "endAngle" element
     */
    void setEndAngle(net.opengis.gml.x32.AngleType endAngle);
    
    /**
     * Appends and returns a new empty "endAngle" element
     */
    net.opengis.gml.x32.AngleType addNewEndAngle();
    
    /**
     * Unsets the "endAngle" element
     */
    void unsetEndAngle();
    
    /**
     * Gets the "interpolation" attribute
     */
    net.opengis.gml.x32.CurveInterpolationType.Enum getInterpolation();
    
    /**
     * Gets (as xml) the "interpolation" attribute
     */
    net.opengis.gml.x32.CurveInterpolationType xgetInterpolation();
    
    /**
     * True if has "interpolation" attribute
     */
    boolean isSetInterpolation();
    
    /**
     * Sets the "interpolation" attribute
     */
    void setInterpolation(net.opengis.gml.x32.CurveInterpolationType.Enum interpolation);
    
    /**
     * Sets (as xml) the "interpolation" attribute
     */
    void xsetInterpolation(net.opengis.gml.x32.CurveInterpolationType interpolation);
    
    /**
     * Unsets the "interpolation" attribute
     */
    void unsetInterpolation();
    
    /**
     * Gets the "numArc" attribute
     */
    java.math.BigInteger getNumArc();
    
    /**
     * Gets (as xml) the "numArc" attribute
     */
    org.apache.xmlbeans.XmlInteger xgetNumArc();
    
    /**
     * Sets the "numArc" attribute
     */
    void setNumArc(java.math.BigInteger numArc);
    
    /**
     * Sets (as xml) the "numArc" attribute
     */
    void xsetNumArc(org.apache.xmlbeans.XmlInteger numArc);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static net.opengis.gml.x32.ArcByCenterPointType newInstance() {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static net.opengis.gml.x32.ArcByCenterPointType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.ArcByCenterPointType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static net.opengis.gml.x32.ArcByCenterPointType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (net.opengis.gml.x32.ArcByCenterPointType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
