<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">
        <xsl:apply-templates select="response/lst/lst/int"/>
    </xsl:template>

    <xsl:template match="int[@name]">
        <xsl:text>
</xsl:text>
        <xsl:value-of select="@name"/>
    </xsl:template>

</xsl:stylesheet>
