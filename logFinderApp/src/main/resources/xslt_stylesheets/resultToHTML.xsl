<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>SearchInfoResult</title>
                <style type="text/css">
                    #headers{

                    }

                    #searchInfo{
                    position: relative;
                    width:25%;
                    right:2%;
                    top:-100px;
                    }

                    #logs{
                    position: relative;
                    width:70%;
                    left:2%;
                    }
                </style>
            </head>
            <body>
                <div id="headers">
                    <h1 align="center">Приложение logFinder</h1>
                    <h1 align="center">Создатель: Охапкин Егор, ООО «Siblion»</h1>
                </div>
                <table id="searchInfo" border="1" align="right">
                    <caption>
                        <h3>Параметры поиска</h3>
                    </caption>
                    <tr>
                        <td>Regular expression :</td>
                        <td id="regexp">
                            <xsl:value-of select="searchInfoResult/searchInfo/regexp"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Search location :</td>
                        <td id="location">
                            <xsl:value-of select="searchInfoResult/searchInfo/location"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <b>Date Intervals</b>
                        </td>
                    </tr>
                    <tr>
                        <td>Date From</td>
                        <td>Date To</td>
                    </tr>
                    <xsl:for-each select="searchInfoResult/searchInfo/dateIntervals">
                        <tr>
                            <td>
                                <xsl:value-of select="dateFrom"/>
                            </td>
                            <td>
                                <xsl:value-of select="dateTo"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
                <xsl:variable name="logCount" select="count(searchInfoResult/resultLogs)"/>
                <xsl:if test="$logCount = 0">
                    <h1 align="center"><xsl:value-of select="searchInfoResult/emptyResultMessage"/></h1>
                </xsl:if>
                <xsl:if test="$logCount > 0">
                    <table id="logs" border="1">
                        <caption>
                            <h2>Найденные логи</h2>
                        </caption>
                        <tr>
                            <th>Time Moment</th>
                            <th>File Name</th>
                            <th>Content</th>
                        </tr>
                        <xsl:for-each select="searchInfoResult/resultLogs">
                            <tr>
                                <td>
                                    <xsl:value-of select="timeMoment"/>
                                </td>
                                <td>
                                    <xsl:value-of select="fileName"/>
                                </td>
                                <td>
                                    <xsl:value-of select="content"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </table>
                </xsl:if>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>