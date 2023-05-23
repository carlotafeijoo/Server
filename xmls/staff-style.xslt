<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" indent="yes" />

<xsl:template match="/">
   <html>
	
	 <p><b>Paid staff:</b></p>
  
  	 <p><xsl:value-of select="Staff/@name" /></p>
  	 <p><xsl:value-of select="Staff/phone" /></p>
  	 <p><xsl:value-of select="Staff/dob" /></p>
  	 <p><xsl:value-of select="Staff/address" /></p>
  	 <table border="1">
		   <th> Elderly`s name</th>
		   <th>age</th>
       <xsl:for-each select="Staff/elderlies/Elderly">
      <xsl:sort select="@name" />
	       <tr>
	            <td><xsl:value-of select="@name" /></td>
	            <td><xsl:value-of select="age" /></td>
	                      
	       </tr>        
      </xsl:for-each>  
		
	</table>	
  	   
	</html>
	</xsl:template>

</xsl:stylesheet>