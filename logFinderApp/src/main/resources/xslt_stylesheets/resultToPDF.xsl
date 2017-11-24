<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="searchInfoResult">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4-portrait"
                                       page-height="29.7cm"
                                       page-width="21.0cm"
                                       margin="2cm">
                    <fo:region-body region-name="xsl-region-body"/>
                    <fo:region-after region-name="xsl-region-after"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="A4-portrait">
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block text-align="center">Page
                        <fo:page-number/>
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body" font-family="Arial">
                    <fo:block-container absolute-position="absolute" left="8cm">
                        <fo:table table-layout="fixed" width="100%" border="solid 0.5mm black" position="absolute"
                                  left="100pt" font="12pt Arial">
                            <fo:table-column column-width="50mm"/>
                            <fo:table-column column-width="50mm"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.5mm black">
                                        <fo:block>
                                            <xsl:text>Regexp</xsl:text>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 0.5mm black">
                                        <fo:block>
                                            <xsl:value-of select="searchInfo/regexp"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.5mm black">
                                        <fo:block>
                                            <xsl:text>Location</xsl:text>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 0.5mm black">
                                        <fo:block>
                                            <xsl:value-of select="search-info/location"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell border="solid 0.5mm black">
                                        <fo:block>
                                            <xsl:text>DateFrom</xsl:text>
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border="solid 0.5mm black">
                                        <fo:block>
                                            <xsl:text>Конечное время</xsl:text>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <xsl:for-each select="searchInfo/dateIntervals">
                                    <fo:table-row font="10pt Arial">
                                        <fo:table-cell border="solid 0.5mm black">
                                            <fo:block>
                                                <xsl:value-of select="dateFrom"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border="solid 0.5mm black">
                                            <fo:block>
                                                <xsl:value-of select="dateTo"/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block-container>
                    <fo:block-container position="absolute" top="10cm">
                        <fo:block text-align="center" font-size="20pt">
                            Приложение LogFinder
                        </fo:block>
                        <fo:block text-align="center" font-size="20pt">
                            Разработал : Охапкин Егор, ООО «Siblion»
                        </fo:block>
                    </fo:block-container>
                    <xsl:variable name="logsN" select="count(resultLogs)"/>
                        <fo:block-container space-before="30cm">
                            <xsl:if test="$logsN = 0">
                                <fo:block text-align="center">
                                    <xsl:value-of select="emptyResultMessage"/>
                                </fo:block>
                            </xsl:if>
                            <xsl:if test="$logsN > 0">
                            <fo:block text-align="center" font-size="20pt">
                                Найденные логи
                            </fo:block>
                            <xsl:for-each select="resultLogs">
                                <fo:block space-before="0.25cm">
                                    Log <xsl:value-of select="position()"/>
                                </fo:block>
                                <fo:block>
                                    Time moment:
                                    <xsl:value-of select="timeMoment"/>
                                </fo:block>
                                <fo:block>
                                    File:
                                    <xsl:value-of select="fileName"/>
                                </fo:block>
                                <fo:block>
                                    Message:
                                </fo:block>
                                <fo:block space-after="0.25cm" font-size="8pt">
                                    <xsl:value-of select="content"/>
                                </fo:block>
                            </xsl:for-each>
                            </xsl:if>
                        </fo:block-container>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>