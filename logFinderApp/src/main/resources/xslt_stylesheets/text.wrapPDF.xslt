<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="text.wrap"
                xmlns:str="http://www.ora.com/XSLTCookbook/namespaces/strings"
                xmlns:text="http://www.ora.com/XSLTCookbook/namespaces/text"
                exclude-result-prefixes="text">
    <xsl:include href="str.substring-before-last.xslt"/>
    <xsl:include href="text.justify.xslt"/>


    <!--Шаблон вывода текстав области заданной ширины-->
    <xsl:template match="node() | @*" mode="text:wrap" name="text:wrap">
        <xsl:param name="input" select="normalize-space()"/>
        <xsl:param name="width" select="70"/>


        <xsl:choose>
            <xsl:when test="string-length($input) > $width">
                <xsl:text>( </xsl:text>
                <xsl:value-of select="substring($input,1,$width)"/>
                <xsl:text> )Tj &#xa; 0 -10 TD &#xa;</xsl:text>
                <xsl:call-template name="text:wrap">
                    <xsl:with-param name="input" select="substring($input,$width+1,string-length($input)-$width)"/>
                    <xsl:with-param name="width" select="$width"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>(</xsl:text>
                <xsl:value-of select="$input"/>
                <xsl:text>)Tj &#xa; 0 -10 TD &#xa;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

</xsl:stylesheet>
