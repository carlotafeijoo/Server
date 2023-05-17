<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="yes" />

<xsl:template match="/">
   <html>
	 <p><b>Elderly:</b></p>
  	 <xsl:value-of select="Elderly"/>
  	 <p><xsl:value-of select="Elderly/@name" /></p>
  	 <p><xsl:value-of select="Elderly/age" /></p>
  		
   </html>
</xsl:template>
</xsl:stylesheet>