<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="yes" />

<xsl:template match="/">
   <html>
	
	 <p><b>Paid staff:</b></p>
  
  	 <p><xsl:value-of select="Staff/@name" /></p>
  	 <p><xsl:value-of select="Staff/phone" /></p>
  	 <p><xsl:value-of select="Staff/dob" /></p>
  	 <p><xsl:value-of select="Staff/address" /></p>
  	   
	</html>
	</xsl:template>

</xsl:stylesheet>