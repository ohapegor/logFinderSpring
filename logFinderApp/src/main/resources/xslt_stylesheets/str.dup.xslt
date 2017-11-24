<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:str="http://www.ora.com/XSLTCookbook/namespaces/strings"
                extension-element-prefixes="str">
   <!-- Шаблон дублированиястроки N раз-->
    <xsl:template name="str:dup">
        <xsl:param name="input"/>
        <xsl:param name="count" select="2"/>
        <xsl:choose>
            <xsl:when test="not($count) or not($input)"/>
            <xsl:when test="$count = 1">
                <xsl:value-of select="$input"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="$count mod 2">
                    <xsl:value-of select="$input"/>
                </xsl:if>
                <xsl:call-template name="str:dup">
                    <xsl:with-param name="input"
                                    select="concat($input,$input)"/>
                    <xsl:with-param name="count"
                                    select="floor($count div 2)"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>