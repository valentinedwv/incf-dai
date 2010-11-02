<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xalan">

<xsl:variable name="url" select="'incf-dev-local.crbs.ucsd.edu:8080'"/>

<xsl:output method="xml" encoding="UTF-8" indent="yes" xalan:indent-amount="2"/>

<xsl:template match="/">

<ListHubsResponse
    xmlns="http://www.incf.org/WaxML/">
  <Hubs>

    <Hub>
      <Name>atlas-aba</Name>
      <Title>ABA Atlas Services</Title>
      <Abstract>
        ABA Atlas Services provide access to data available from the 
        Allen Brain Atlas resource.
      </Abstract>
      <GetCapabilitiesURI>
        http://<xsl:value-of select="$url" />/atlas-aba?service=WPS&amp;request=GetCapabilities
      </GetCapabilitiesURI>
    </Hub>

    <Hub>
      <Name>atlas-emap</Name>
      <Title>EMAP Atlas Services</Title>
      <Abstract>
        EMAP Atlas Services provide access to data available from the
        Edinburgh Mouse Atlas Project resource.
      </Abstract>
      <GetCapabilitiesURI>
        http://<xsl:value-of select="$url" />/atlas-emap?service=WPS&amp;request=GetCapabilities
      </GetCapabilitiesURI>
    </Hub>

    <Hub>
      <Name>atlas-ucsd</Name>
      <Title>UCSD Atlas Services</Title>
      <Abstract>
        UCSD Atlas Services provide access to data available from the
        University of California, San Diego, Cell Centered Database (CCDB) 
        resource.
      </Abstract>
      <GetCapabilitiesURI>
        http://<xsl:value-of select="$url" />/atlas-ucsd?service=WPS&amp;request=GetCapabilities
      </GetCapabilitiesURI>
    </Hub>

    <Hub>
      <Name>atlas-whs</Name>
      <Title>WHS Atlas Services</Title>
      <Abstract>
        WHS Atlas Services provide access to data available from the
        INCF Waxholm Space resource.
      </Abstract>
      <GetCapabilitiesURI>
        http://<xsl:value-of select="$url" />/atlas-whs?service=WPS&amp;request=GetCapabilities
      </GetCapabilitiesURI>
    </Hub>

  </Hubs>
</ListHubsResponse>

</xsl:template>
</xsl:stylesheet>
