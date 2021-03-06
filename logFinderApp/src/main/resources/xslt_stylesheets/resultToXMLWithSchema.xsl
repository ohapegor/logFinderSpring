<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ls="http://ohapegor.ru/logFinder/services/webServices/soap/schema">
    <xsl:import href="copy.xslt"/>
    <xsl:output method="xml" version="1.0" encoding="UTF-8"/>

    <xsl:template match="/">

        <xsl:element name="result">
            <xsl:element name="info">
                Приложегие LogFinder, Создатель Охапкин Е.В.  ООО «Siblion»
            </xsl:element>
            <xsl:copy>
                <xsl:apply-templates select="*/ls:searchInfo"/>
            </xsl:copy>
            <xsl:variable name="logCount" select="count(*/ls:resultLogs)"/>
            <xsl:if test="$logCount = 0">
            <xsl:copy>
                <xsl:apply-templates select="*/ls:emptyResultMessage"/>
            </xsl:copy>
            </xsl:if>
            <xsl:copy>
                <xsl:apply-templates select="*/ls:resultLogs"/>
            </xsl:copy>
        </xsl:element>

    </xsl:template>


</xsl:stylesheet>

