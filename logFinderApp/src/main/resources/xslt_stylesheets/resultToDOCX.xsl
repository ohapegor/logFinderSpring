<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">

    <xsl:template match="searchInfoResult">
        <w:document mc:Ignorable="w14 wp14" xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
                    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape"
                    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
                    xmlns:w10="urn:schemas-microsoft-com:office:word"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:o="urn:schemas-microsoft-com:office:office">
            <w:body>
                <w:tbl>
                    <w:tblPr>
                        <w:tblW w:w="4000" w:type="dxa"/>
                        <w:jc w:val="left"/>
                        <w:tblInd w:w="4500" w:type="dxa"/>
                        <w:tblBorders/>
                        <w:tblCellMar>
                            <w:top w:w="0" w:type="dxa"/>
                            <w:left w:w="108" w:type="dxa"/>
                            <w:bottom w:w="0" w:type="dxa"/>
                            <w:right w:w="108" w:type="dxa"/>
                        </w:tblCellMar>
                    </w:tblPr>
                    <w:tblGrid>
                        <w:gridCol w:w="2000"/>
                        <w:gridCol w:w="2000"/>
                    </w:tblGrid>
                    <w:tr>
                        <w:tc>
                            <w:p>
                                <w:r>
                                    <w:rPr>
                                        <w:sz w:val="16"/>
                                        <w:szCs w:val="16"/>
                                    </w:rPr>
                                    <w:t>Regular Expression</w:t>
                                </w:r>
                            </w:p>
                        </w:tc>
                        <w:tc>
                            <w:p>
                                <w:r>
                                    <w:rPr>
                                        <w:sz w:val="16"/>
                                        <w:szCs w:val="16"/>
                                    </w:rPr>
                                    <w:t>
                                        <xsl:value-of select="searchInfo/regexp"/>
                                    </w:t>
                                </w:r>
                            </w:p>
                        </w:tc>
                    </w:tr>
                    <w:tr>
                        <w:tc>
                            <w:p>
                                <w:r>
                                    <w:rPr>
                                        <w:sz w:val="16"/>
                                        <w:szCs w:val="16"/>
                                    </w:rPr>
                                    <w:t>Location</w:t>
                                </w:r>
                            </w:p>
                        </w:tc>
                        <w:tc>
                            <w:p>
                                <w:r>
                                    <w:t>
                                        <w:rPr>
                                            <w:sz w:val="16"/>
                                            <w:szCs w:val="16"/>
                                        </w:rPr>
                                        <xsl:value-of select="searchInfo/location"/>
                                    </w:t>
                                </w:r>
                            </w:p>
                        </w:tc>
                    </w:tr>
                    <w:tr>
                        <w:tc>
                            <w:p>
                                <w:r>
                                    <w:rPr>
                                        <w:sz w:val="16"/>
                                        <w:szCs w:val="16"/>
                                    </w:rPr>
                                    <w:t>DateFrom</w:t>
                                </w:r>
                            </w:p>
                        </w:tc>
                        <w:tc>
                            <w:p>
                                <w:r>
                                    <w:rPr>
                                        <w:sz w:val="16"/>
                                        <w:szCs w:val="16"/>
                                    </w:rPr>
                                    <w:t>DateTo</w:t>
                                </w:r>
                            </w:p>
                        </w:tc>
                    </w:tr>
                    <xsl:for-each select="searchInfo/dateIntervals">
                        <w:tr>
                            <w:tc>
                                <w:p>
                                    <w:r>
                                        <w:rPr>
                                            <w:sz w:val="16"/>
                                            <w:szCs w:val="16"/>
                                        </w:rPr>
                                        <w:t>
                                            <xsl:value-of select="dateFrom"/>
                                        </w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:p>
                                    <w:r>
                                        <w:rPr>
                                            <w:sz w:val="16"/>
                                            <w:szCs w:val="16"/>
                                        </w:rPr>
                                        <w:t>
                                            <xsl:value-of select="dateTo"/>
                                        </w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                    </xsl:for-each>
                </w:tbl>
                <w:p><w:r><w:rPr/></w:r><w:r><w:rPr/></w:r></w:p>
                <w:p><w:r><w:rPr/></w:r><w:r><w:rPr/></w:r></w:p>
                <w:p><w:r><w:rPr/></w:r><w:r><w:rPr/></w:r></w:p>
                <w:p>
                    <w:pPr>
                        <w:pStyle w:val="Normal"/>
                        <w:jc w:val="center"/>
                        <w:rPr>
                            <w:b/>
                            <w:b/>
                            <w:bCs/>
                            <w:lang w:val="ru-RU"/>
                        </w:rPr>
                    </w:pPr>
                    <w:r>
                        <w:rPr>
                            <w:b/>
                            <w:bCs/>
                            <w:lang w:val="ru-RU"/>
                        </w:rPr>
                        <w:t xml:space="preserve">Приложение </w:t>
                    </w:r>
                    <w:r>
                        <w:rPr>
                            <w:b/>
                            <w:bCs/>
                            <w:lang w:val="en-EN"/>
                        </w:rPr>
                        <w:t>LogFinder</w:t>
                    </w:r>
                </w:p>
                <w:p>
                    <w:pPr>
                        <w:pStyle w:val="Normal"/>
                        <w:jc w:val="center"/>
                        <w:rPr>
                            <w:b/>
                            <w:b/>
                            <w:bCs/>
                            <w:lang w:val="ru-RU"/>
                        </w:rPr>
                    </w:pPr>
                    <w:r>
                        <w:rPr>
                            <w:b/>
                            <w:bCs/>
                            <w:lang w:val="ru-RU"/>
                        </w:rPr>
                        <w:t>Создатель: Охапкин Егор, ООО «Siblion»</w:t>
                    </w:r>
                </w:p>
                <xsl:variable name="logCount" select="count(resultLogs)"/>
                <xsl:if test="$logCount = 0">
                    <w:p>
                        <w:pPr>
                            <w:pStyle w:val="Normal"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:b/>
                                <w:b/>
                                <w:bCs/>
                                <w:lang w:val="ru-RU"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:b/>
                                <w:bCs/>
                                <w:lang w:val="ru-RU"/>
                            </w:rPr>
                            <w:t><xsl:value-of select="emptyResultMessage"/></w:t>
                        </w:r>
                    </w:p>
                </xsl:if>
                <w:p><w:r><w:rPr/></w:r><w:r><w:rPr/></w:r></w:p>
                <w:p><w:r><w:rPr/></w:r><w:r><w:rPr/></w:r></w:p>
                <w:p><w:r><w:rPr/></w:r><w:r><w:rPr/></w:r></w:p>
                <xsl:for-each select="resultLogs">
                    <w:p>
                        <w:r>
                            <w:t>
                                <xsl:text>Log №</xsl:text><xsl:value-of select="position()"/>
                            </w:t>
                            <w:br/>
                            <w:t>
                                <xsl:text>Time moment : </xsl:text><xsl:value-of select="timeMoment"/>
                            </w:t>
                            <w:br/>
                            <w:t>
                                <xsl:text>File : </xsl:text><xsl:value-of select="fileName"/>
                            </w:t>
                            <w:br/>
                            <w:t>
                                <xsl:value-of select="content"/>
                            </w:t>
                            <w:br/>
                        </w:r>
                    </w:p>
                </xsl:for-each>
                <w:sectPr>
                    <w:footerReference r:id="rId2" w:type="default"/>
                    <w:type w:val="nextPage"/>
                    <w:pgSz w:w="12240" w:h="15840"/>
                    <w:pgMar w:gutter="0" w:bottom="1999" w:footer="1440" w:top="1440" w:header="0" w:right="1800"
                             w:left="1800"/>
                    <w:pgNumType w:fmt="decimal"/>
                    <w:formProt w:val="false"/>
                    <w:textDirection w:val="lrTb"/>
                    <w:docGrid w:type="default" w:charSpace="4294961151" w:linePitch="240"/>
                </w:sectPr>
            </w:body>
        </w:document>
    </xsl:template>


</xsl:stylesheet>