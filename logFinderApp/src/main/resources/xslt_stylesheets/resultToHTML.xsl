<?xml version="1.0" encoding="utf-8" ?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ls="http://ohapegor.ru/logFinder/services/webServices/soap/schema">
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
                            <xsl:value-of select="ls:searchInfoResult/ls:searchInfo/ls:regexp"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Search location :</td>
                        <td id="location">
                            <xsl:value-of select="ls:searchInfoResult/ls:searchInfo/ls:location"/>
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
                    <xsl:for-each select="ls:searchInfoResult/ls:searchInfo/ls:dateIntervals">
                        <tr>
                            <td>
                                <xsl:value-of select="ls:dateFrom"/>
                            </td>
                            <td>
                                <xsl:value-of select="ls:dateTo"/>
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
                <xsl:variable name="logCount" select="count(ls:searchInfoResult/ls:resultLogs)"/>
                <xsl:if test="$logCount = 0">
                    <h1 align="center"><xsl:value-of select="ls:searchInfoResult/ls:emptyResultMessage"/></h1>
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
                        <xsl:for-each select="ls:searchInfoResult/ls:resultLogs">
                            <tr>
                                <td>
                                    <xsl:value-of select="ls:timeMoment"/>
                                </td>
                                <td>
                                    <xsl:value-of select="ls:fileName"/>
                                </td>
                                <td>
                                    <xsl:value-of select="ls:content"/>
                                </td>
                            </tr>
                        </xsl:for-each>
                    </table>
                </xsl:if>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>