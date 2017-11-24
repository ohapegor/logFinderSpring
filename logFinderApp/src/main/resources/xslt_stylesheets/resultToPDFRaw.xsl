<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"
        xmlns:text="http://www.ora.com/XSLTCookbook/namespaces/text"
        xmlns:xs="http://www.w3.org/2001/XMLSchema">

    <xsl:include href="text.wrapPDF.xslt"/>
    <xsl:strip-space elements="*"/>
    <xsl:output method="text"/>


    <!-- generate PDF page structure -->


    <xsl:template match="searchInfoResult">

        <!--searchInfo-->
        <xsl:value-of select="searchInfo/regexp"/><xsl:text> ? </xsl:text>
        <xsl:value-of select="searchInfo/location"/><xsl:text> ? </xsl:text>
        <xsl:for-each select="searchInfo/dateIntervals">
            <xsl:value-of select="dateFrom"/><xsl:text>=</xsl:text><xsl:value-of select="dateTo"/><xsl:text>|</xsl:text>
        </xsl:for-each>
        <xsl:text> ? </xsl:text>

        <xsl:variable name="lineHeight" select="10"/>
        <xsl:variable name="pageHeight" select="842"/>
        <xsl:variable name="charactersPerLine" select="98"/>
        <xsl:variable name="pageWidth" select="595"/>
        <xsl:variable name="contentHeight" select="sum(for $x in child::resultLogs/content return string-length($x))"/>
        <xsl:variable name="logsN" select="count(resultLogs)"/>
        <xsl:variable name="linesN" select="ceiling($logsN*3 + ($contentHeight div $charactersPerLine))"/>
        <xsl:variable name="pageCount" select="ceiling($linesN div ($pageHeight div $lineHeight))"/>
        <xsl:variable name="totalChars" select="sum(for $x in child::resultLogs/content return string-length($x))"/>
        <xsl:variable name="linesPerPage" select="ceiling(($pageHeight - 80) div $lineHeight)"/>
        <xsl:text> ?Logs№ =  </xsl:text><xsl:value-of select="$logsN"/>
        <!--logs per page assignment-->
        <xsl:variable name="pageToObject" as="element()*">
            <xsl:for-each select="resultLogs">
                <item>
                    <xsl:value-of
                            select="ceiling(((sum(for $x in preceding-sibling::resultLogs/content return string-length($x))+string-length(content)) div $charactersPerLine + 3*(count(preceding-sibling::result-logs) + 1)) div $linesPerPage)"/>
                </item>
            </xsl:for-each>
        </xsl:variable>

        <xsl:variable name="objectsInPage" as="element()*">
            <xsl:for-each select="1 to xs:integer($pageCount)">
                <xsl:variable name="pageN" select="position()"/>
                <item>
                    <xsl:text>[</xsl:text>
                    <xsl:for-each select="1 to $logsN">
                        <xsl:variable name="n" select="position()"/>
                        <xsl:if test="xs:integer($pageToObject[$n]) = $pageN">
                            <xsl:value-of select="$n+2+$pageCount"/><xsl:text> 0 R </xsl:text>
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:text>]</xsl:text>
                </item>
            </xsl:for-each>
        </xsl:variable>


        <xsl:text disable-output-escaping="yes">
            %PDF-1.1
        %íì¦"

        1 0 obj
        &lt;&lt;
        /Type /Catalog
        /Pages 2 0 R
        >>
        endobj

        2 0 obj
        &lt;&lt;
        /Type /Pages
        /Kids [ </xsl:text>
        <xsl:for-each select="1 to xs:integer($pageCount)">
            <xsl:value-of select="position() + 2"/><xsl:text> 0 R </xsl:text>
        </xsl:for-each>
        ]

        /Count
        <xsl:value-of select="$pageCount"/>
        /MediaBox [0 0
        <xsl:value-of select="$pageWidth"/><xsl:text> </xsl:text><xsl:value-of select="$pageHeight"/><xsl:text
            disable-output-escaping="yes">]
        >>
        endobj</xsl:text>

        <xsl:for-each select="1 to xs:integer($pageCount)">
            <xsl:variable name="pageN" select="position()"/>
            <xsl:value-of select="$pageN + 2"/><xsl:text> 0 obj
        &lt;&lt;  /Type /Page
        /Parent 2 0 R
        /Resources
        &lt;&lt; /Font
        &lt;&lt; /F1
        &lt;&lt; /Type /Font
        /Subtype /Type1
        /BaseFont /Times-Roman
        >>
        >>
        >>
            /Contents </xsl:text><xsl:value-of select="$objectsInPage[$pageN]"/>
            <xsl:text disable-output-escaping="yes">
        >>
        endobj
        </xsl:text>
        </xsl:for-each>

        <xsl:for-each select="resultLogs">
            <xsl:variable name="n" select="position()"/>
            <xsl:variable name="page" select="$pageToObject[$n]"/>
            <xsl:variable name="startObjN" select="count($pageToObject[xs:integer(.) &lt; $page])"/>
            <xsl:variable name="linesFromDocBeginningToPage"
                          select="ceiling(sum(for $x in preceding-sibling::resultLogs[count(preceding-sibling::resultLogs) &lt; $startObjN ]/content return string-length($x)) div $charactersPerLine +3*count(preceding-sibling::result-logs[count(preceding-sibling::result-logs) &lt; $startObjN]))"/>
            <xsl:variable name="linesFromDocBeginningToObj"
                          select="ceiling(sum(for $x in preceding-sibling::resultLogs/content return string-length($x)) div $charactersPerLine +3*count(preceding-sibling::result-logs))"/>
            <xsl:variable name="deltaLines" select="$linesFromDocBeginningToObj - $linesFromDocBeginningToPage"/>

            <xsl:value-of select="$pageCount + 2 + position()"/><xsl:text disable-output-escaping="yes"> 0 obj
        &lt;&lt;>>
        stream
        BT
        /F1 10 Tf
            40 </xsl:text><xsl:value-of select="810 - $deltaLines*$lineHeight"/> Td&#xa;
            (Log №<xsl:value-of select="position()"/><xsl:text>)Tj &#xa; 0 -10 TD &#xa;</xsl:text>
            (Time moment:
            <xsl:value-of select="timeMoment"/><xsl:text>)Tj &#xa; 0 -10 TD &#xa;</xsl:text>
            (File name:
            <xsl:value-of select="fileName"/><xsl:text>)Tj &#xa; 0 -10 TD &#xa;</xsl:text>
            <xsl:apply-templates select="content" mode="text:wrap">
                <xsl:with-param name="width" select="$charactersPerLine"/>
                <xsl:with-param name="align" select=" 'left' "/>
                <xsl:with-param name="align-width" select="$charactersPerLine"/>
            </xsl:apply-templates>
            <xsl:text disable-output-escaping="yes">
        ET
        endstream
        endobj
        </xsl:text>
        </xsl:for-each>

        <xsl:text disable-output-escaping="yes">
        xref
        trailer
        &lt;&lt;
        /Root 1 0 R
        >>
        startxref

        %%EOF
 </xsl:text>

    </xsl:template>


</xsl:stylesheet>