<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ls="http://ohapegor.ru/logFinder/services/webServices/soap/schema">
    <xsl:import href="copy.xslt"/>
    <xsl:output method="xml" version="1.0" encoding="UTF-8"/>

    <xsl:template match="ls:searchInfoResult">
        <xsl:element name="result">
        <xsl:element name="info">
            Приложение LogFinder, Создатель Охапкин Е.В. ООО «Siblion»
        </xsl:element>
        <xsl:apply-templates select="ls:searchInfo"/>
            <xsl:variable name="logsCount" select="count(ls:resultLogs)"/>
            <xsl:if test="$logsCount = 0">
                <xsl:element name="emptyResultMessage">
                    <xsl:text>No logs Found</xsl:text>
                </xsl:element>
            </xsl:if>
            <xsl:if test="$logsCount > 0">
                    <xsl:apply-templates select="ls:resultLogs"/>
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@* | node()"/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>

