<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="2.0"
                xmlns:ls="http://ohapegor.ru/logFinder/services/webServices/soap/schema">
    <xsl:output method="text"/>

    <xsl:template match="ls:searchInfoResult">
        <xsl:text>{\rtf1\ansi\ansicpg1251\deff0\adeflang1049{\footer \qc \field{\*\fldinst  \field{\*\fldinst  PAGE } }}</xsl:text>
        <xsl:text>{\trowd\trleft5000\cellx7500\cellx10000 Regular Expression\cell</xsl:text>
        <xsl:value-of select="ls:searchInfo/ls:regexp"/><xsl:text>\cell\row\trowd\trleft5000\cellx7500\cellx10000Location\cell</xsl:text>
        <xsl:value-of select="ls:searchInfo/ls:location"/><xsl:text>\cell\row</xsl:text>
        <xsl:text>\trowd\trleft5000\cellx7500\cellx10000DateFrom\cell DateTo\cell\row</xsl:text>
        <xsl:for-each select="ls:searchInfo/ls:dateIntervals">
            <xsl:text>\trowd\trleft5000\cellx7500\cellx10000 </xsl:text><xsl:value-of select="ls:dateFrom"/><xsl:text>\cell </xsl:text>
            <xsl:value-of select="ls:dateTo"/><xsl:text>\cell\row</xsl:text>
        </xsl:for-each>
        <xsl:text>}\par{\pard\ansi\ansicpg1251\deff0\adeflang1049 \b \qc \'cf\'f0\'e8\'eb\'ee\'e6\'e5\'ed\'e8\'e5 logFinder \line \'d1\'ee\'e7\'e4\'e0\'f2\'e5\'eb\'fc: \'ce\'f5\'e0\'ef\'ea\'e8\'ed \'c5\'e3\'ee\'f0, \'ce\'ce\'ce \'abSiblion\'bb  \par } </xsl:text>
        <xsl:variable name="logCount" select="count(ls:resultLogs)"/>
        <xsl:if test="$logCount = 0">
        <xsl:text>\par{\pard\ansi\ansicpg1251\deff0\adeflang1049 \qc </xsl:text><xsl:value-of select="ls:emptyResultMessage"/><xsl:text>  \par } </xsl:text>
        </xsl:if>
        <xsl:apply-templates select="ls:resultLogs"/>
        <xsl:text> } </xsl:text>
    </xsl:template>

    <xsl:template match="ls:resultLogs">
        <xsl:text> {\pard\ansi\ansicpg1251\deff0\adeflang1049 </xsl:text>
        <xsl:text> \tab \b Log # \b0 </xsl:text><xsl:value-of select="position()"/><xsl:text> \line </xsl:text>
        <xsl:text>\b Time moment : \b0 </xsl:text>
        <xsl:value-of select="ls:timeMoment"/>
        <xsl:text>\line </xsl:text>
        <xsl:text>\b File Name : \b0 </xsl:text><xsl:value-of select="ls:fileName"/><xsl:text> \line </xsl:text>
        <xsl:text>\b Message : \b0 </xsl:text><xsl:value-of select="ls:content"/><xsl:text>\line \par } </xsl:text>
    </xsl:template>


</xsl:stylesheet>
